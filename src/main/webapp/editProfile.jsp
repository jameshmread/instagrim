<%-- 
    Document   : editProfile
    Created on : 30-Sep-2016, 12:16:54
    Author     : James
--%>

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
          
            <form method="POST" enctype="multipart/form-data" action="Image">
                Edit Profile Picture <input type="file" name=""><br/>

                <br/>
                
            
        
        <input type="submit" value="Apply Changes">
        </form>
    </body>
</html>
