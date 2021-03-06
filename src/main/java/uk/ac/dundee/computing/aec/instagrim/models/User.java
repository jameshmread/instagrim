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
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;


/**
 *
 * @author AEC, James Read
 */
public class User {
    private Cluster cluster;
    private Session session;
    private ProfileInfo profile = new ProfileInfo();
    private java.util.UUID profilePicid = null;
    private String username = null;
    
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
        try{
        session = cluster.connect("instagrim");
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
        } 
        else {
            for(Row row: rs){
                if(username.equals(row.getString("login"))) return true;
                }
        }
       }catch(Exception e)
       {
           System.out.println("Could not check for existing username " + e);
           
           return false;
       }
       finally{
            if(session!=null) session.close();
        }
        System.out.println("USER DOESNT EXIST YET");
        return false;
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
       try{
        cluster = CassandraHosts.getCluster();
        session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("UPDATE userprofiles SET first_name =?, last_name =?, email =?, bio =? WHERE login=?");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        first_name,last_name,email,bio,username));
        //We are assuming this always works.  Also a transaction would be good here !
       }catch(Exception e)
       {
           System.out.println("Could not update user info " + e);
       }
       finally{
            if(session!=null) session.close();
        }
    }
       
   
    public ProfileInfo getUserInfo(String username, ProfileInfo profileInfo){
        this.profile = profileInfo;
        try{
        session = cluster.connect("instagrim");
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
            profileInfo.setFirst_name(row.getString(0));
            profileInfo.setLast_name(row.getString(1));
            profileInfo.setEmail(row.getString(2));
            profileInfo.setBio(row.getString(3));
            profileInfo.setProfilePicture(row.getUUID("profilePicID"));
            
             System.out.println("Profile Info set in user method" + 
                     profileInfo.getFirst_name() + profileInfo.getLast_name() 
                     + profileInfo.getEmail());
                }
        }return profileInfo;
        //dont need session.close here as finally block is always executed
        }catch(Exception e)
       {
           System.out.println("Could return user info " + e);
       }
       finally{
            if(session!=null) session.close();   
        }
        return profileInfo;
        
    }

    public java.util.UUID getProfilePicture(String username){
        java.util.UUID uuid = null;
        //sometimes i had to use cassandrahosts.getcluster and sometimes not
        try{
        session = cluster.connect("instagrim");
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
        }catch(Exception e)
       {
           System.out.println("Could not update user info " + e);
       }
       finally{
            if(session!=null) session.close();
        }
        return uuid;
    }
    
     public void deleteProfile(String username){
      
        cluster = CassandraHosts.getCluster();
        session = cluster.connect("instagrim");
            System.out.println("Deleting Profile");
            PreparedStatement ps;
            ResultSet rs = null;
            BoundStatement boundStatement;
            try{
            ps = session.prepare("DELETE FROM userpiclist WHERE user =?");
            boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(username));
            System.out.println("deleting userpiclist");
            ps = session.prepare("DELETE FROM userprofiles WHERE login =?");
            boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(username));
            
            System.out.println("~~~User: " + username + " deleted.~~~");
            } catch(Exception ex){System.out.println("Unable to delete profile. Exception:  " + ex);}
            
            session.close();
            
            
    }
    
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
}
