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
              <%ProfileInfo profile = (ProfileInfo) request.getAttribute("ProfileInfo");%>
              <%String firstName = profile.getFirst_name(); %>
              <%String username = "NULL"; %>
              <%boolean userVisiting = (boolean)request.getAttribute("userVisiting");%>
              <%String otherUsersName = (String)request.getAttribute("otherUsersName");%>
              
              <title>Profile</title>       
        <link rel="stylesheet" type="text/css" href="/Instagrim/Styles.css" />
        
    </head>
    
        <header>
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim !</a> </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
       
        <ul id="navBar">
                <li><a href="/Instagrim/browse"> Browse</a> </li>
                <li><a href="/Instagrim">Home</a></li>
                <li><a href="/Instagrim/Logout">  Log Out </a></li>
                <li><a href="/Instagrim/EditProfile"> Edit Profile </a></li>
         
                <%--Login link removed as this action is not possible on this page--%>        
        </ul>
    
        <%if(userVisiting){%> <%%>
        <h1>Visiting: <%=firstName%>'s Profile </h1>
        <%}else{%>
        <h1> Welcome <%=firstName%> </h1><br>
        <%}%>
        
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
