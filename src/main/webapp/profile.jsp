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
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim !</a> </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
       
        <ul id="navBar">
                <li><a href="gallery.jsp"> Browse</a> </li>
                <li><a href="/Instagrim">Home</a></li>
                <li> <a  href="/Instagrim/Logout">  Log Out </a></li>
                <li><a href="/Instagrim/EditProfile"> Edit Profile </a></li>
                
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
                <h1> Welcome <%=username%> </h1><br>
        
        <p id="bioEdit"> <%=profile.getBio()%></p>   
        <% if(profile.getProfilePicture()!=null){%>
        <%String profilePicID = profile.getProfilePicture();%>
        <a href="/Instagrim/Image/<%=profile.getProfilePicture()%> "> 
            <%-- i have finally realised how simple it is to pass params into servlets using urls --%>
               <img id="profilePic" src="/Instagrim/Image/<%=profile.getProfilePicture()%> " alt="Profile Picture" ></a></br>    
       <%}else{%>
       <p> No Profile Picture! </p>
        <%}%>
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
                String userPictureID = p.getSUUID();
        %>
        <a href="/Instagrim/pictureServlet/?picID=<%=userPictureID%>" >
            <img id="userPicture" src="/Instagrim/Image/<%=p.getSUUID()%>" alt="User Picture"></a><br/>
            <%
            
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
