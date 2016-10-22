<%-- 
    Document   : browse
    Created on : 18-Oct-2016, 18:18:53
    Author     : James
--%>

<%@page import="java.util.Iterator"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.Pic"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Browse</title>
    </head>
    <body>
        <header>
            <h1 id="instagrimHeader"> <a href="/Instagrim"> InstaGrim !</a> </h1>
            <h2 id="instagrimSubheader"> Your world in Black and White</h2>
        </header>
       
        <ul id="navBar">
                <li><a href="/Instagrim/browse"> Browse</a> </li>
                <li><a href="/Instagrim">Home</a></li>
                <li><a href="/Instagrim/EditProfile"> Edit Profile </a></li>
                <li><a href="/Instagrim/Upload"> Upload </a></li>
                <li><a href="/Instagrim/Logout">  Log Out </a></li>
        </ul>
                <form method="POST" action="/Instagrim/browse">
                    <ul id="navBar">
                        <li> <input type="search" name="pictureTitle" value="Search for Pictures"></li>
                    </ul>
                </form>
         
                <%--Login link removed as this action is not possible on this page--%>        
        
    
        <h1>Explore all the pictures</h1>
        <%
            java.util.LinkedList<Pic> picList = (java.util.LinkedList<Pic>) request.getAttribute("picList");
            if (picList == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {String title = (String)request.getAttribute("searchedTitle");%>
            <%if(title!=null){%>Showing Pictures with title: <%=request.getAttribute("searchedTitle")%><%}%>
            <%Iterator<Pic> iterator;
            iterator = picList.iterator();
            while (iterator.hasNext()) {
                Pic p = (Pic) iterator.next();
                String userPictureID = p.getSUUID();
        %>
        <a href="/Instagrim/pictureServlet/<%=userPictureID%>" >
            <img id="userPicture" src="/Instagrim/Image/<%=p.getSUUID()%>" alt="User Picture"></a><br/>
            <%
            
            }
            }
       %>
    </body>
</html>
