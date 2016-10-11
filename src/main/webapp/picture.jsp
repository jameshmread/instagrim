<%-- 
    Document   : picture
    Created on : 08-Oct-2016, 19:47:28
    Author     : James
--%>

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
        <a href="/Instagrim/Image/<%=picID%>" >
            <img id="userPicture" src="/Instagrim/Thumb/<%=picID%>"></a>
        <h2><%=picTitle%></h2>
        
    </body>
</html>
