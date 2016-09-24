/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

/**
 *
 * @author James Read
 */
public class ProfileInfo {
    private String username=null;
    private String userBio=null;
    private String userProfilePicture=null;
    public ProfileInfo(){
}
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String newUsername){
        this.username=newUsername;
    }
    public String getUserBio(){
        return this.userBio;
    }
    public void setUserBio(String newUserBio){
        this.userBio=newUserBio;
    }
    public String getProfilePicture(){
        //server get profile picture
        //return profile picture
        //should return a string as thats what is put in the <img tag>
        return userProfilePicture;
    }
    public void setProfilePicture(){
        //upload picture stored and then set
        //choose existing picture from server and set
    }
}  
