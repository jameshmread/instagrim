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
import javax.servlet.http.HttpServletRequest;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.*;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;


/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    HttpSession session;
    ProfileInfo profile = new ProfileInfo();
    private java.util.UUID profilePicid = null;
    String username = null;
    
    public User(){
        
    }
    //setter and getter for profilepic id
    public void setProfilePicid(java.util.UUID uuid){
        this.profilePicid = uuid;
    }
    public java.util.UUID getProfilePicid(){
        return this.profilePicid;
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
        session.close();
        //We are assuming this always works.  Also a transaction would be good here !
        this.setProfileDatabaseInfo(username,first_name, last_name, email, "Place holder bio");
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
        session.close();
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
    
    public boolean usernameAlreadyExists(String username){
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = 
                session.prepare("SELECT login FROM userprofiles WHERE login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        
        if (rs.isExhausted()) {
            System.out.println("User doesnt exist");
            return false;
        } else {
            for(Row row: rs){
                if(username.equals(row.getString("login"))) return true;
                }
        }
        session.close();
        System.out.println("USER DOESNT EXIST YET");
        return false;
    }
    
    //leave this here for now, probably should be in editprofile servlet
    public void setProfileStoreInfo(String first_name, String last_name, String email, String bio,
            HttpServletRequest request){
        session = request.getSession();
        profile = (ProfileInfo)session.getAttribute("ProfileInfo"); 
        //need to pass the session into this or it cant update the store for the sesison
        System.out.println(first_name);
        System.out.println(last_name);
        System.out.println(email);
        profile.setFirst_name(first_name);
        profile.setLast_name(last_name);
        profile.setEmail(email); 
        profile.setBio(bio);
    }
    
    /*
    public void setStoreProfilePicture(java.util.UUID uuid, HttpServletRequest request){
        uuid = this.profilePicid;
        System.out.println("Profile picture changed, UUID: " + uuid);
        session = request.getSession();
        profile = (ProfileInfo)session.getAttribute("ProfileInfo"); 
        profile.setProfilePicture(uuid);
    }
    */
    
    public void setProfileDatabaseInfo(String username, String first_name, String last_name, String email, String bio){
        //remember to look at keyspaces for editing of bio######
        cluster = CassandraHosts.getCluster();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("UPDATE userprofiles SET first_name =?, last_name =?, email =?, bio =? WHERE login=?");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        first_name,last_name,email,bio,username));
        //We are assuming this always works.  Also a transaction would be good here !
        session.close();
    }
       
   
    public ProfileInfo getUserInfo(String username, ProfileInfo profileInfo){
        this.profile = profileInfo;
        
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = 
                session.prepare("SELECT first_name, last_name, email, bio, profilePicID FROM userprofiles WHERE login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        
        if (rs.isExhausted()) {
            System.out.println("No user information retrieved from database");
            
        } else {
            for(Row row: rs){
                //does this need to be in a loop? is there some other way to get the Row object? of course there is...
            profileInfo.setFirst_name(row.getString(0));
            profileInfo.setLast_name(row.getString(1));
            profileInfo.setEmail(row.getString(2));
            profileInfo.setBio(row.getString(3));
            profileInfo.setProfilePicture(row.getUUID("profilePicID"));
            //i know i know there should be a better way to do this
             System.out.println("Profile Info set in user method" + 
                     profileInfo.getFirst_name() + profileInfo.getLast_name() 
                     + profileInfo.getEmail());
                }
        }
        session.close();
        return profileInfo;
    }

    public java.util.UUID getProfilePicture(String username){
        java.util.UUID uuid = null;
        //cluster = CassandraHosts.getCluster();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("SELECT profilePicID FROM userprofiles WHERE login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        session.close();
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                uuid = row.getUUID("profilePicID");
                
            }
        }
        return uuid;
    }
    
     public void deleteProfile(String username){
      
        cluster = CassandraHosts.getCluster();
         Session session = cluster.connect("instagrim");
            System.out.println("Deleting Profile");
            PreparedStatement ps;
            ResultSet rs = null;
            BoundStatement boundStatement;
            try{            
            ps = session.prepare("DELETE FROM pics WHERE user =?");
            boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(username));
            System.out.println("deleting pics");
            ps = session.prepare("DELETE FROM likes WHERE username =?");
            boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(username));
            System.out.println("deleting likes");
            ps = session.prepare("DELETE FROM comments WHERE username =?");
            boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(username));
            System.out.println("deleting comments");
            ps = session.prepare("DELETE FROM userpiclist WHERE user =?");
            boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(username));
            System.out.println("deleting userpiclist");
            ps = session.prepare("DELETE FROM userprofiles WHERE login =?");
            boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(username));
            System.out.println("deleting profile");
            System.out.println("~~~User: " + username + " deleted.~~~");
            } catch(Exception ex){System.out.println("Unable to delete profile. Exception:  " + ex);}
            
            session.close();
            
            
    }
    
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
}
