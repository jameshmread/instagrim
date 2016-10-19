<%-- 
    Document   : picture
    Created on : 08-Oct-2016, 19:47:28
    Author     : James
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*"%>
<%@page import="java.util.LinkedList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <title>Picture</title>
    </head>
    
            <header>
                
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim !</a> </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
            </header>
       
        <ul id="navBar">
                <li><a href="/Instagrim/browse"> Browse</a> </li>
                <li><a href="/Instagrim">Home</a></li>
                <li><a href="/Instagrim/Logout">  Log Out </a></li>
                <li><a href="/Instagrim/EditProfile"> Edit Profile </a></li>
         
                <%--Login link removed as this action is not possible on this page--%>        
        </ul>
    
    <body><%%>
        <%String picID = (String)request.getAttribute("pictureID"); %>
        <%String picTitle = (String)request.getAttribute("picTitle");%>
        <%LinkedList comments = (LinkedList)request.getAttribute("comments");%>
        <%LinkedList usernames = (LinkedList)request.getAttribute("users");%>
        <%LinkedList likes = (LinkedList)request.getAttribute("likes"); %>
        <%--LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn"); --%>
        <%--String user = lg.getUsername(); --%>
        
        <a href="/Instagrim/Image/<%=picID%>">
            <img id="userPicture" src="/Instagrim/Thumb/<%=picID%>" alt="User Picture"></a>
        <h2><%=picTitle%></h2>
        <br>
        <%--request.setAttribute("deletePicID", picID); --%>
        <h2>Add a Comment</h2>
        
        <form method="POST" action="?picID=<%=picID%>&postComment=true">
            <input type="text" name="commentText">
        <input type="submit" value="Post Comment">
        </form>
            <form method="POST" action="?picID=<%=picID%>&like=true"> 
                <input type="submit" value="Like"> 
            </form>
        <%if(likes !=null){%>
        <%String like;%>
        <h3>Likes:<%=likes.size()%></h3> <%--add if statement to return the likes as a number over 3ish--%>
            <%for(int i=0; i < likes.size(); i++){
            like = (String)likes.get(i); %>
            <ul>
                <li><%=like%></li> 
            </ul>
            <%}%>
        <%%>
        <%}else{%><p>No-one likes this</p><%}%>
        <br>
        
        <h3>Comments</h3>
        <%if(comments !=null){%>
        <%String username, comment = "NULL";%>
        <%=comments.size()/2%>
            <%for(int i=0; i < comments.size(); i++){ %>         
            <ul>
                <li><%=(String)comments.get(i)%>: <%i++;%> <%=(String)comments.get(i)%></li>
            </ul>
            <%}%>
        <%%>
        <%}else{%><p>No comments yet!</p><%}%>
        
        <%--This delete button should only be visible to user who posted this--%>
        <form action="/Instagrim/delete/<%=picID%>">
            
        <input type="submit" Value="Delete">
        </form>
        <%--all this br and formatting is just temp so i can get functions sorted before style--%>
        
    </body>
</html>
