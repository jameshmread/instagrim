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
    private String Username, userProfilePicture, first_name, last_name, email = null;
    private String bio= "Say something about yourself";
    
    
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
    
    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }
    public String getFirst_name(){
        return first_name;
    }
    
        public void setLast_name(String last_name){
        this.last_name = last_name;
    }
    public String getLast_name(){
        return last_name;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
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
