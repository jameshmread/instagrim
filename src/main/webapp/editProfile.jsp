<%-- 
    Document   : editProfile
    Created on : 30-Sep-2016, 12:16:54
    Author     : James
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.models.PicModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <title>Profile Settings</title>
    </head>
    <body>
        <h1>Settings</h1>
       
        <form method="POST" action="EditProfile">
        <h2>Edit First Name</h2>
        <input type="text" name="firstName">
        <br>
        <h2>Edit Last Name </h2>
        <input type="text" name="lastName">
        <br>
        <h2>Edit Bio</h2>
        <input type="text" name="bio">
        <br>
        <h2>Edit Email </h2>
        <input type="text" name="email">
        <br>
        <input type="submit" value="Apply Changes"> 
        <%-- will need to validate input when fields are empty. 
        basically a few if's check if empty field, then get from session store [DO THIS IN SERVLET]--%>
        </form>
        
            <form method="POST" enctype="multipart/form-data" action="Image">
                <% session.setAttribute("profilePic", true); %>
                Edit Profile Picture <input type="file" name="profilePic"><br/>                
                <%-- could have the 'set entering profile pic as a store element in profileInfo --%>
                <br/>
                <input type="submit" value="Edit Profile Picture">
        
        </form>
                <form action="EditProfile?deleteProfile=true" method="POST">
            
        <input type="submit" Value="Delete">
                </form> 
                <h3>WARNING. This action CANNOT be undone</h3>
    </body>
</html>
