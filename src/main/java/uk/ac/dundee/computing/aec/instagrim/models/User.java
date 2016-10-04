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
import java.util.Arrays;
import java.util.Vector;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.*;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    HttpSession session;
    ProfileInfo profile = new ProfileInfo();
    
    String username = null;
    
    public User(){
        
    }
    
    public boolean RegisterUser(String username, String Password, String first_name, String last_name, String email){
        
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
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,first_name,last_name,email) Values(?,?,?,?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username,EncodedPassword,first_name,last_name,email));
        //We are assuming this always works.  Also a transaction would be good here !
        setUserInfo(first_name, last_name, email);
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
    
    
    public void setUserInfo(String first_name, String last_name, String email){
        System.out.println(first_name);
        System.out.println(last_name);
        System.out.println(email);
        profile.setFirst_name(first_name);
        profile.setLast_name(last_name);
        profile.setEmail(email);
        
        
    }
    
    //FLESH OUT THIS METHOD WHERE SEARCHES USERNAME AND RETURNS THE FIRST/LAST NAME EMAIL AND STUFF
    public String[] getUserInfo(String username){
        //this.username = username;
                 
          String[] userInformation = new String[3];
          
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("SELECT first_name, last_name, email FROM userprofiles WHERE login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        
        if (rs.isExhausted()) {
            System.out.println("No user information retrieved from database");
            
        } else {
            for(Row row: rs){
             userInformation[0] = row.getString(0);
             userInformation[1] = row.getString(1);
             userInformation[2] = row.getString(2);
             System.out.println(userInformation[0]+ userInformation[1] + userInformation[2]);
                }
        }
        return userInformation;
    }
    
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
}
