<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>

<!DOCTYPE html>
<html>
    <head>
        <% LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn"); %>
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <header>
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim ! </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
        
        
            <ul id="navBar">
                <li><a href="gallery.jsp"> Browse</a> </li>
                <% if(lg != null)
                {%>
                <li><a href="profile.jsp"> Profile</a> </li>
                <li><a href="upload.jsp"> Upload</a></li>
                <li> <a  href="profile" method="GET">  Log Out </a></li>   
                <%}%>
                    <%
                        if (lg != null) {
                           // String UserName = lg.getUsername();
                            if (lg.getlogedin()) {
                    %>

                <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
                    <%}
                            }else{
                                %>
                 <li><a href="register.jsp">Register</a></li>
                <li><a href="login.jsp">Login</a></li>
                <li class="footer"><a href="/Instagrim">Home</a></li>
                <%}%>
            </ul>
        
        
        <footer>
            <ul>
                
                <li>&COPY; Andy C</li>
            </ul>
        </footer>
    </body>
</html>
