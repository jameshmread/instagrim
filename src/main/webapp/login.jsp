<%-- 
    Document   : login.jsp
    Created on : Sep 28, 2014, 12:04:14 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim - Login</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />

    </head>
    <body>
        <header>
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim !</a> </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
       
        <ul id="navBar">
                
                <li><a href="/Instagrim/Register">Register</a></li>
                
                <li><a href="/Instagrim">Home</a></li>
                <%-- no profile nor upload --%>
                <%-- Removed 'your images' as no user will be logged in to view 'their' images on this page--%>
                <%--Login/logout link removed as this action is not possible on this page--%>        
        </ul>
        
        <article>
            <h3>Login</h3>
            <form method="POST"  action="Login">
                <ul>
                     
                    <li>User Name <input type="text" name="username"></li>
                    <li>Password <input type="password" name="password"></li>
                   
                </ul>
                <br/>
                <input type="submit" value="Login">
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
