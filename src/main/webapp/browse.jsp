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
            <script>
                function findPicNames(title){
                    var xhttp = new XMLHttpRequest();
                    var url = "/Instagrim/preBrowse/?pictureTitle=" + title;
                   
                    xhttp.onreadystatechange = function(){
                        if (title.length === 0) {
                            document.getElementById("autoFill").innerHTML = "No Suggestions";
                            System.out.println("text=null");
                            return;
                        }else
                        {
                        //http://www.w3schools.com/xml/ajax_xmlhttprequest_response.asp
                            if (this.readyState === 4 && this.status === 200) { //java script expects === instead of ==  ?? 
                                document.getElementById("autoFill").innerHTML = this.responseText;
                                System.out.println("response");
                            }
                        }
                };
                 xhttp.open("GET", url, true);
                 xhttp.send();
            };
            
            </script>
       
        <ul id="navBar">
                <li><a href="/Instagrim/browse"> Browse</a> </li>
                <li><a href="/Instagrim">Home</a></li>
                <li><a href="/Instagrim/EditProfile"> Edit Profile </a></li>
                <li><a href="/Instagrim/upload"> Upload </a></li>
                <li><a href="/Instagrim/Logout">  Log Out </a></li>
        </ul>
                <form method="GET" action="/Instagrim/browse">
                     <ul id="navBar">
                            <li> <input id="picSearch" type="search" name="pictureTitle" onKeyup="findPicNames(this.value)" value="Search for Pictures"></li>
                        </ul>
                </form>
                Search Suggestions: <div id="autoFill"></div>
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
