<%-- 
    Document   : profile
    Created on : 20-Sep-2016, 21:26:49
    Author     : James
--%>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.servlets.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
              <%LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");%>
              <%ProfileInfo profile = (ProfileInfo) session.getAttribute("ProfileInfo");%>
              <%String username = "NULL"; %>
              <%String firstName, lastName, email = "NULL"; %>
              <title>Profile</title>       
        <link rel="stylesheet" type="text/css" href="/Instagrim/Styles.css" />
        
    </head>
    
        <header>
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim ! </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
       
        <ul id="navBar">
                <li><a href="gallery.jsp"> Browse</a> </li>
                <li><a href="/Instagrim">Home</a></li>
                <li> <a  href="Login" method="GET">  Log Out </a></li>
                <li><a href="editProfile.jsp"> Edit Profile </a></li>
                
                <% if(profile != null) { %>
                <% firstName = profile.getFirst_name(); %>
                <% lastName = profile.getLast_name(); %>
                <% email = profile.getEmail(); %>
                <p><%=firstName%></p>
                <p><%=lastName%></p>
                <p><%=email%></p>
                <%}%> 
                
                <%--Login link removed as this action is not possible on this page--%>        
        </ul>
    <body>
        
        
        
        <% if(lg != null) {
         if (lg.getlogedin()) {%>
        <%username = lg.getUsername(); 
        } 
        else {
    %><p>No Username found</p><%
                } }%>
                Welcome <%=username%> <br>
        
        <p id="bioEdit"> <%=profile.getBio()%></p>    
        <%session.setAttribute("picID", profile.getProfilePicture());%>
            <input type="image" action="/Instagrim/pictureServlet/<%profile.getProfilePicture();%>" method="GET" name="picID">
            <a href="/Instagrim/pictureServlet"> 
               <img id="profilePic" src="/Instagrim/Thumb/<%=profile.getProfilePicture()%> " alt="Profile Picture"></a></br>    
       
        
        <%-- MAIN BODY SHOWING USERS PICTURES HERE --%>
        <%
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) {
                Pic p = (Pic) iterator.next();

        %>
        <a href="/Instagrim/Image/<%=p.getSUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a><br/><%
            
            }
            }
       %>
        <%-- if there is a way to forward the SUIDD to page, then i may be able to take the java out --%>
                <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    
    </body>
    
</html>
