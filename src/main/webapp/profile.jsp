<%-- 
    Document   : profile
    Created on : 20-Sep-2016, 21:26:49
    Author     : James
--%>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
              <%LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");%>
              <%String username = "NULL"; %>
              
              <title>Profile</title>       
        <link rel="stylesheet" type="text/css" href="/Instagrim/Styles.css" />
        
    </head>
    <body>
        <% if(lg != null) {
         if (lg.getlogedin()) {%>
        <%username = lg.getUsername(); 
        } 
        else {
            username = "No Username found";
                } }%>
        Welcome <%=username%>
        
        <img src = "/imageLocationHere.jpg" alt="Profile Picture" style="width:300px;height:300px;"> 
        
        <h2> A Little bit about yourself</h2>
        
                <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    
    </body>
    
</html>
