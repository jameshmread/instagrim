<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
    </head>
    <body>
        <header>
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim ! </a></h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
       
        <ul id="navBar">
                
                <li><a href="/Instagrim/Login">Login</a></li> 
                <li><a href="/Instagrim">Home</a></li>
                <%-- no profile nor upload --%>
                <%-- Removed 'your images' as no user will be logged in to view 'their' images on this page--%>
                <%--Login/logout link removed as this action is not possible on this page--%>        
        </ul>
       
        <article>
            <h2>Register as user</h2>
            <form method="POST"  action="Register">
                <ul id="register">
                    <li id="register">User Name <input type="text" name="username"></li>
                    <li id="error"> <% if((String)request.getAttribute("usernameError") == "true") {%> Invalid username <%} else {} %></li>
                    <li id="register">Password <input type="password" name="password"></li>
                    <li id="register">Confirm Password <input type="password" name="confirmPassword"></li>
                    
                    <li id="error"> <% if((String)request.getAttribute("passwordError") == "true") {%> Passwords do not match <%} else {} %></li>
                    <%-- IF time store the users info apart from mismatched passwords (convenience) --%>
                    <li id="register">First Name <input type="text" name="first_name"></li>
                    <li id="register">Last Name <input type="text" name="last_name"></li>
                    <li id="register">Email <input type="text" name="email"></li>
                </ul>
                <br/>
                <input type="submit" value="Register"> 
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
