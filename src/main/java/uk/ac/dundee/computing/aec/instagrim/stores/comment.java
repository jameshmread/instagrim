/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;
import java.util.*;
/**
 *
 * @author James
 */
public class comment {
    private String commentText;
    private String userCommenting;
    private UUID pictureID;
    private Date commentID;
    
    public comment(){
        
    }
    
    public void setCommentText(String text){
        this.commentText = text;
    }
    public String getCommentText(){
        return this.commentText;
    }
    
    public void setUserCommenting(String username){
        this.userCommenting = username;
    }
    public String getUserCommenting(){
        return this.userCommenting;
    }
    
    public void setpicID(UUID pictureID){
        this.pictureID = pictureID;
    }
    public String getPicID(){
        return this.pictureID.toString();
    }
    
    public void setCommentID(Date timestamp){
        this.commentID = timestamp;
    }
    
}
