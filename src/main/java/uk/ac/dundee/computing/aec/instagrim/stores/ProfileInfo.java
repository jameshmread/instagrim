/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

/**
 *
 * @author James
 */
public class ProfileInfo {
    private java.util.UUID userProfilePicture =null;
    private String first_name = "NULL";
    private String last_name = "NULL";
    private String email = "NULL";
    private String bio = "Say something about yourself";
    
    public void ProfileInfo() {
      
    }
    
    public void setBio(String bio)
    {
        this.bio = bio;
    }
    public String getBio()
    {
        return this.bio;
    }
    
    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }
    public String getFirst_name(){
        return this.first_name;
    }
    
        public void setLast_name(String last_name){
        this.last_name = last_name;
    }
    public String getLast_name(){
        return this.last_name;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
        public String getProfilePicture(){
        return userProfilePicture.toString();
    }
    public void setProfilePicture(java.util.UUID uuid){
        this.userProfilePicture = uuid;
    }
}
