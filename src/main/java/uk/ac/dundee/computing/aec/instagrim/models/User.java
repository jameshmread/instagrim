/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.*;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    String username = null;
    
    public User(){
        
    }
    
    public boolean RegisterUser(String username, String Password){
        this.username = username;
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password) Values(?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username,EncodedPassword));
        //We are assuming this always works.  Also a transaction would be good here !
        
        return true;
    }
    
    public boolean IsValidUser(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
               
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
   
    
    return false;  
    }
    
    //create find/return user method here which is called by profile.java
    //this method returns the username of the user
    public String getUser(Session session)
    {       
        //NEEDS TO BE CHANGED/LOOKED AT
        
        session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("SELECT FROM userprofiles WHERE login="+ username);
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind());
        
        return username;
    }
    
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
}
