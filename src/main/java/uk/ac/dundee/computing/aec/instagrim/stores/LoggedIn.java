/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.stores;

/**
 *
 * @author Administrator
 */
public class LoggedIn {
    private boolean logedin=false;
    private String Username=null;
    private String bio=null;
    private String userProfilePicture=null;
    
    public void LogedIn(){
        
    }
    
    public void setUsername(String name){
        this.Username=name;
    }
    public String getUsername(){
        return Username;
    }
    public void setBio(String bio)
    {
        this.bio = bio;
    }
    public String getBio()
    {
        return bio;
    }
    
        public String getProfilePicture(){
        //server get profile picture
        //return profile picture
        //should return a string as thats what is put in the <img tag>
        //or use a file stream shich takes from database
        return userProfilePicture;
    }
    public void setProfilePicture(){
        //upload picture stored and then set
        //choose existing picture from server and set
    }
    
    public void setLogedin(){
        logedin=true;
    }
    public void setLogedout(){
        logedin=false;
    }
    
    public void setLoginState(boolean logedin){
        this.logedin=logedin;
    }
    public boolean getlogedin(){
        return logedin;
    }
}
