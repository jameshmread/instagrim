package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import static com.datastax.driver.core.DataType.text;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.Timestamp;
import java.util.Date;
import java.sql.*;
import java.util.LinkedList;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import static org.imgscalr.Scalr.*;
import org.imgscalr.Scalr.Method;


import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.stores.comment;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    Cluster cluster;
    boolean enteringProfilePic;
    public void PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void setEnteringProfilePic(boolean profilePic){
        this.enteringProfilePic = profilePic;
    }
    
    public boolean getEnteringProfilePic(){
        return this.enteringProfilePic;
    }
    
    public void insertPic(byte[] b, String type, String name, String user, String title, String filter) {
        try {
            Convertors convertor = new Convertors();

            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();
            
            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

            output.write(b);
            byte []  thumbb = picresize(picid.toString(),types[1], filter);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(),types[1], filter);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instagrim");
            //Below calls the user model to enter a uuid related to the user so a profile pic can be returned
            
            if(this.getEnteringProfilePic()){
                setDatabaseProfilePicture(user, picid);
                //setting the profile store happens in image servlet as that has a request
                
                this.setEnteringProfilePic(false); //re-set etering profile pic
            }
            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name,title) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name, title));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }
    
    public void setDatabaseProfilePicture(String username, java.util.UUID uuid){
        cluster = CassandraHosts.getCluster();
        Session session = cluster.connect("instagrim");
                PreparedStatement ps = session.prepare("UPDATE userprofiles SET profilePicID =? WHERE login=?");
        System.out.println("ProfilePicture Set as: " + uuid);
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        uuid,username));
        session.close();
    }

    public byte[] picresize(String picid,String type, String filter) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage thumbnail = createThumbnail(BI, filter);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }
    
    public byte[] picdecolour(String picid,String type, String filter) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processed = createProcessed(BI, filter);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }
    
    public static BufferedImage createThumbnail(BufferedImage img, String filter) {
        switch(filter){
                case "Light":
                        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
                        break;
                case "Dark":
                        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
                        break;
                default: System.out.println("Thumbnail Filter Error"); return null;
            }       
        return pad(img, 2);
    }
    
    public static BufferedImage createProcessed(BufferedImage img, String filter) {
        int Width=img.getWidth()-1;
         switch(filter){
                case "Light":
                        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_BRIGHTER);
                        System.out.println("#### LIGHT ####");
                        break;
                case "Dark":
                       img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
                       System.out.println("#### DARK ####");
                       break;
                default: System.out.println("Picture Filter Error"); return null;
            }
        return pad(img, 4);
    }
   // change this method and the following method to return the owner of the pictures
    public java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid, user from userpiclist where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                String owner = row.getString("user");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                pic.setOwner(owner); //this is a list of objets so i can add the fields all day
                Pics.add(pic);
            }
        }
        return Pics;
    }
    
    public LinkedList<Pic> getAllPics(){
         java.util.LinkedList<Pic> allPictures = new java.util.LinkedList<>();
         cluster = CassandraHosts.getCluster();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("SELECT picid, user from pics LIMIT 500");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute(boundStatement.bind());
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                String username = row.getString("user");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                pic.setOwner(username);
                allPictures.add(pic);
            }
        }
        return allPictures;
    }

    public Pic getPic(int image_type, java.util.UUID picid) {
        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;
         
            if (image_type == Convertors.DISPLAY_IMAGE) {
                
                ps = session.prepare("select image,imagelength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picid));

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");
                
                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }
                    
                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        p.setPic(bImage, length, type);

        return p;

    }
    //allowing a string in here is easier as only one conversion to uuid has to be done (inside this function)
    public String getPicTitle(String picID){ 
        String title =null;
        java.util.UUID uuid = java.util.UUID.fromString(picID); //need to convert back to uuid for database
        cluster = CassandraHosts.getCluster();
         Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("select title from pics where picID =?");
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            uuid));
            
            
            for (Row row : rs) {
                title = (String)row.getString("title");
            }
            
            System.out.println("Title taken at Picmodel: " + title);
        session.close();
        return title;
    }
    
    public void deletePicture(String picID, String username){
        java.util.UUID uuid = java.util.UUID.fromString(picID); //need to convert back to uuid for database
        
        cluster = CassandraHosts.getCluster();
         Session session = cluster.connect("instagrim");
            //deletes from pics and userpiclist so it cant be referenced
            System.out.println("Deleting picture");
            Date picDate = getDateFromPic(picID); //this needs to be called beforefirst delete or it'll never be found
           /* PreparedStatement ps = session.prepare("delete from pics where picID =?");
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            uuid));
            */
           //there is no user notification that they dont have permission to delete the picture
           //however, using the two PK's means that a user cannot delete anothers photo
            PreparedStatement psUserPicList = session.prepare("DELETE FROM userpiclist WHERE user =? AND pic_added =?");
            ResultSet rs1 = null;
            BoundStatement boundStatementUPL = new BoundStatement(psUserPicList);
            rs1 = session.execute( // this is where the query is executed
                    boundStatementUPL.bind( // here you are binding the 'boundStatement'
                            username, picDate));
            //both statements combined should delete the picture from the database entirely
            session.close();
    }
    
    public Date getDateFromPic(String picID){
        java.util.UUID uuid = java.util.UUID.fromString(picID);
        Date picDate = new Date();
        
        cluster = CassandraHosts.getCluster();
         Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("select interaction_time from pics where picID =?");
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            uuid));
            
            
            for (Row row : rs) {
                picDate = (Date)row.getDate("interaction_time");
                //cannot try and get a string straight from result set
                //need to create a row using the RS

            }
            System.out.println("Pic Date: " + picDate);
        
        return picDate;
    }
    
    public void insertComment(String comment, String user, String picID){
        java.util.UUID uuid = java.util.UUID.fromString(picID); //need to convert back to uuid for database
        Date commentID = new Date(); //creates a unique comment id as the primary key
        cluster = CassandraHosts.getCluster();
         Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("INSERT into comments (commentText, userCommenting, picID, commentID) values(?,?,?,?)");
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            comment,user,uuid,commentID));
           
        session.close(); 
        System.out.println("SUCCESS! Comment Inserted: " + comment + " From User: " + user);
    }
    
    //refactoring for third time, using a comment object and returning a list of those
    //how i didnt think of this until now, i have no idea
    public java.util.LinkedList<comment> getCommentList(String picID){
        java.util.UUID uuid = java.util.UUID.fromString(picID);
        //java.util.LinkedList<String> userAndComments = new java.util.LinkedList<>();
        //java.util.LinkedList<String> user = new java.util.LinkedList<>();
        
        
        LinkedList<comment> commentList = new LinkedList<>();
        
        cluster = CassandraHosts.getCluster();
         Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("SELECT userCommenting, commentText FROM comments WHERE picID =?");
            //need to know which user made which comment 
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            uuid));
            
            for (Row row : rs) {
               comment comment = new comment();
               comment.setCommentText(row.getString("commentText"));
               System.out.println("Comment text: " + row.getString("commentText"));
               comment.setUserCommenting(row.getString("userCommenting"));
               commentList.add(comment);
            }
            
        return  commentList;
    }
     /* dont really need this now as returning username and comments together
    public java.util.LinkedList<String> getCommentsUser(String picID){
        java.util.UUID uuid = java.util.UUID.fromString(picID);
        java.util.LinkedList<String> user = new java.util.LinkedList<>();
        cluster = CassandraHosts.getCluster();
         Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("SELECT userCommenting FROM comments WHERE picID =?");
            //need to know which user made which comment 
            //this will probably break when i add multiple users, keep an eye... it did
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            uuid));
            
            for (Row row : rs) {
                user.add(row.getString("userCommenting")); //seems too easy....
            }
        
        return  user;
    }
    */
    public void setLike(String username, String picID){
        java.util.UUID uuid = java.util.UUID.fromString(picID); //need to convert back to uuid for database
        //Date likeTime = new Date();

            cluster = CassandraHosts.getCluster();
            Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("INSERT into likes (username, picID) values(?,?)");
            // order of likes is not essential
            //you cannot like a photo twice so the pk of username and picID is unique
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            username,uuid));
            
        session.close(); 
        System.out.println("SUCCESS! LIKED by: " + username);
    }
    
    public void setUnlike(String username, String picID){
         java.util.UUID uuid = java.util.UUID.fromString(picID); //need to convert back to uuid for database
            cluster = CassandraHosts.getCluster();
            Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("DELETE FROM likes WHERE username =? AND picID =?");
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            username,uuid));
           
        session.close(); 
        System.out.println("SUCCESS! UNLIKED by: " + username);
    }
    
    public LinkedList<String> getLikes(String picID){
        UUID uuid = UUID.fromString(picID);
        LinkedList<String> likes = new LinkedList<>();
        cluster = CassandraHosts.getCluster();
            Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("SELECT username FROM likes WHERE picID=?");
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            uuid));
         for (Row row : rs) {
                likes.push(row.getString("username")); 
                System.out.println("Adding Like to list");
            }
        session.close(); 
        System.out.println("SUCCESS! Returned likes from database");
        return likes;
    }
    
     public boolean userLikedPicture(String username, String picID){
        UUID uuid = UUID.fromString(picID);
        String returnedUsername = null;
        cluster = CassandraHosts.getCluster();
            Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("SELECT * FROM likes WHERE picID=?");
            ResultSet rs = null;
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            uuid));
         for (Row row : rs) {
                if(username.equals(row.getString("username"))){ System.out.println("USER LIKED THIS, DISLIKING"); return true;}
            }
         session.close();
         System.out.println("USER HAS NOT LIKED THIS, LIKING");
         return false;
    }
}
