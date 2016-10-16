/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.*;
import uk.ac.dundee.computing.aec.instagrim.stores.*;

/**
 *
 * @author James
 */
@WebServlet(name = "pictureServlet", urlPatterns = {"/pictureServlet", "/pictureServlet/*"})
public class pictureServlet extends HttpServlet {
        Cluster cluster;
        public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
        
    }
  // private HashMap CommandsMap = new HashMap();
    public pictureServlet(){
       ///// CommandsMap.put("Image", 1);
       // CommandsMap.put("Images", 2);
       // CommandsMap.put("Thumb", 3);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String pictureIDToGo = (String)request.getParameter("picID");
        System.out.println("Picture serverlet recieved the PIC ID as(PARAM): " + pictureIDToGo);
        
        PicModel pm = new PicModel();
        pm.setCluster(cluster);
        String picTitle = (String)pm.getPicTitle(pictureIDToGo);
        LinkedList<String> comments = pm.getCommentList(pictureIDToGo);
        LinkedList<String> users = pm.getCommentsUser(pictureIDToGo);
        LinkedList<String> likes = pm.getLikes(pictureIDToGo);
        //i could cut out the middle man here but to get it working, just keep it in two separate expressions
        request.setAttribute("comments", comments);
        request.setAttribute("users", users);
        request.setAttribute("pictureID", pictureIDToGo); 
        request.setAttribute("picTitle", picTitle);
        request.setAttribute("likes", likes);
        System.out.println("Pic Title is: " + picTitle);
        RequestDispatcher rd = request.getRequestDispatcher("/picture.jsp");
        rd.forward(request, response);
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("pictureServlet.doPost() called"); 
        HttpSession session = request.getSession();
        LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn");
        String picID = (String)request.getParameter("picID");
        PicModel pm = new PicModel();
        pm.setCluster(cluster);
            
        if("true".equals(request.getParameter("delete"))){
            System.out.println("Delete Parameter: " + request.getParameter("delete"));
            System.out.println("PictureID to delete: " + request.getParameter("picID"));
            
            pm.deletePicture(picID, lg.getUsername());
            System.out.println("Picture deleted");
            response.sendRedirect("/Instagrim/profile");
        }else 
        if("true".equals(request.getParameter("postComment"))){ 
            String commentText = request.getParameter("commentText");
            String username = lg.getUsername();
            //insert comment into database
            pm.insertComment(commentText, username, picID);
            request.setAttribute("picID", picID);
            response.sendRedirect("/Instagrim/pictureServlet/?picID=" + picID);
            //use redirects for post, request dispatchers for get 
            // http://www.javapractices.com/topic/TopicAction.do?Id=181
        }else
            if("true".equals(request.getParameter("like"))){
                String username = lg.getUsername();
                //get the specific user if they have liked this, dont itterate through all the users
                //who have ever liked this, you idiot
                //if user has not liked this, set like, else if liked set unlike
                if(!pm.userLikedPicture(username, picID)) pm.setLike(username, picID);
                else pm.setUnlike(username, picID);
                request.setAttribute("picID", picID);
                response.sendRedirect("/Instagrim/pictureServlet/?picID=" + picID);
            }
         
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
