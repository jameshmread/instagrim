<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
    Author     : Administrator
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*"%>
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
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim !</a> </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
       <%LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn");
        String username = lg.getUsername(); %>
        <ul id="navBar">
                <li><a href="/Instagrim/browse">Browse</a> </li>
                <li><a href="/Instagrim/profile/<%=username%>">Profile</a> </li>
                <li><a href="/Instagrim/Register">Register</a></li>
                <li><a href="/Instagrim/Images/majed">Sample Images</a></li>
                <li class="footer"><a href="/Instagrim">Home</a></li>
                <li><a href="/Instagrim/Logout"> Log Out </a></li>
                <%-- no profile nor upload --%>
                <%-- Removed 'your images' as no user will be logged in to view 'their' images on this page--%>
                <%--Login link removed as this action is not possible on this page--%>        
        </ul>
 
        <article>
            <h3>Post a Photo</h3>
            <form method="POST" enctype="multipart/form-data" action="Image">
                <% session.setAttribute("profilePic", false); %>
                File to upload: <input type="file" name="upfile"><br/>
                
                <br/>
                <input type="text" name="title"> Set Picture Title
                <br/>
                    <input type="radio" name="filter" value="Light"> Light<br>
                    <input type="radio" name="filter" value="Dark"> Dark<br>
                <%-- prevent picture upload title or it'll cause some bother --%>
                <input type="submit" value="Press"> to upload the file!
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
