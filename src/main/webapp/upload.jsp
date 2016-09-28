<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
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
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim ! </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
       
        <ul id="navBar">
                <li><a href="gallery.jsp"> Browse</a> </li>
                <li><a href="register.jsp">Register</a></li>
                <li><a href="/Instagrim/Images/majed">Sample Images</a></li>
                <li class="footer"><a href="/Instagrim">Home</a></li>
                <li><a href="/Instagrim"> Log Out </a></li>
                <%-- no profile nor upload --%>
                <%-- Removed 'your images' as no user will be logged in to view 'their' images on this page--%>
                <%--Login link removed as this action is not possible on this page--%>        
        </ul>
 
        <article>
            <h3>File Upload</h3>
            <form method="POST" enctype="multipart/form-data" action="Image">
                File to upload: <input type="file" name="upfile"><br/>

                <br/>
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
