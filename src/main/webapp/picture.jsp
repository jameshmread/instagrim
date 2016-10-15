<%-- 
    Document   : picture
    Created on : 08-Oct-2016, 19:47:28
    Author     : James
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*"%>
<%@page import="java.util.LinkedList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>put pic title here</title>
    </head>
    <body><%%>
        <%String picID = (String)request.getAttribute("pictureID"); %>
        <%String picTitle = (String)request.getAttribute("picTitle");%>
        <%LinkedList comments = (LinkedList)request.getAttribute("comments");%>
        <%---LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn"); --%>
        <%--String user = lg.getUsername(); --%>
        
        <a href="/Instagrim/Image/<%=picID%>">
            <img id="userPicture" src="/Instagrim/Thumb/<%=picID%>" alt="User Picture"></a>
        <h2><%=picTitle%></h2>
        <br>
        <%--request.setAttribute("deletePicID", picID); --%>
        <h2>Add a Comment</h2>
        
        <form method="POST" action="pictureServlet?picID=<%=picID%>&postComment=true">
            <input type="text" name="commentText">
        <input type="submit" value="Post Comment">
        </form>
        <br>
        
        <h3>Comments</h3><br>
        <%if(comments.iterator()!=null){%>
            <%while(comments.iterator().hasNext()) %>
            <%{%> <%--comments.iterator().next(); probably wont need this then comments.element.tostring--%>
                  <%String comment = comments.iterator().next().toString();%>
            <ul>
                <li>Comment:<%=comment%></li>
            </ul>
            <%}%>
        <%}%>
        <%else{%><p>No comments yet!</p><%}%>
        
        <%--This delete button should only be visible to user who posted this--%>
        <form method="POST" action="pictureServlet?picID=<%=picID%>&delete=true">
            
        <input type="submit" Value="Delete">
        </form>
        <%--all this br and formatting is just temp so i can get functions sorted before style--%>
        
    </body>
</html>
