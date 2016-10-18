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
        <h1>Explore all the pictures</h1>
        <%
            java.util.LinkedList<Pic> picList = (java.util.LinkedList<Pic>) request.getAttribute("picList");
            if (picList == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
            Iterator<Pic> iterator;
            iterator = picList.iterator();
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
    </body>
</html>
