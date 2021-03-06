<%-- 
    Document   : editProfile
    Created on : 30-Sep-2016, 12:16:54
    Author     : James
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.models.PicModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/Instagrim/Styles.css" />
        <title>Profile Settings</title>
    </head>
    <body>
    <header>
        <%LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn");%>
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim !</a> </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
       
        <ul id="navBar">
                <li><a href="/Instagrim/browse"> Browse</a> </li>
                <li><a href="/Instagrim">Home</a></li>
                <li><a href="/Instagrim/profile/<%=lg.getUsername()%>"> Profile </a></li>
                <li> <a  href="/Instagrim/Logout">  Log Out </a></li>    
        </ul>
        
        <% ProfileInfo profileInfo = (ProfileInfo)session.getAttribute("ProfileInfo"); %>
        <h1>Settings</h1>
        <%String firstName = (String)profileInfo.getFirst_name();%>
        <%String lastName = (String)profileInfo.getLast_name();%>
        <%String bio = (String)profileInfo.getBio();%>
        <%String email = (String)profileInfo.getEmail();%>
        <form method="POST" action="EditProfile?deleteProfile=false">
        <h2>Edit First Name</h2>
        <input type="text" name="firstName" value="<%=firstName%>">
        <br>
        <h2>Edit Last Name </h2>
        <input type="text" name="lastName" value="<%=lastName%>">
        <br>
        <h2>Edit Bio</h2>
        <textarea name="bio" cols="40" rows="5" > 
            <%=bio%> 
        </textarea>
        <br>
        <h2>Edit Email </h2>
        <input type="email" name="email" value="<%=email%>">
        <br>
        <input type="submit" value="Apply Changes"> 
        </form>
        <br>
            <form method="POST" enctype="multipart/form-data" action="Image">
                <% session.setAttribute("profilePic", true); %>
                Edit Profile Picture <input type="file" name="profilePic"><br/>                
                <br/>
                <input type="submit" value="Edit Profile Picture">
            </form>
                <form action="/Instagrim/DeleteProfile/">
                    <input type="submit" Value="Delete">
                </form> 
                <h3>WARNING. This will delete your profile information<br>
                    However, your comments and photos will still be visible to everyone else. <br>
                        This action CANNOT be undone.</h3>
        </body>
</html>
