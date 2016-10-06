/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.stores.*;
import uk.ac.dundee.computing.aec.instagrim.models.*;
/**
 *
 * @author James
 */
@WebServlet(name = "profile", urlPatterns = {"/profile" ,"/profile/*"})
public class Profile extends HttpServlet {

    Cluster cluster=null;
    HttpSession session;
    //User us;
    PicModel picModel;

    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster(); 
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
                //System.out.println("Returned Profile pic");                       
        //User user = new User();
        //String usernameRequest = request.getParameter("username");
        //String passwordRequest = request.getParameter("password");
        session = request.getSession();
        //ProfileInfo profileInfo = new ProfileInfo();
        LoggedIn loggedIn = (LoggedIn)session.getAttribute("LoggedIn");
        //ProfileInfo profileInfo = (ProfileInfo)session.getAttribute("ProfileInfo");
        //picModel = (PicModel)session.getAttribute("PicModel"); //###for when i have to move this stuff into model
        String username = loggedIn.getUsername();
        
               getProfilePic(request, response);
               DisplayImageList(username, request, response);
               //getUserPictures(request, response);
                           
            
        
        System.out.println(username);
        if(loggedIn.getlogedin()){
            System.out.println("user should be logged in");
        RequestDispatcher rd = request.getRequestDispatcher("profile.jsp");        
        rd.forward(request, response);
        
        }
        else
        response.sendRedirect("/Instagrim");
            System.out.println("user clearly not logged in");
        
                
        
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
        /*
        //POSSIBLY MOVE TO DOGET METHOD
        User user = new User();
        String usernameRequest = request.getParameter("username");
        String passwordRequest = request.getParameter("password");
        
        boolean userExists = user.IsValidUser(usernameRequest, passwordRequest);
        
        session = request.getSession();
        if(userExists){
        RequestDispatcher rd = request.getRequestDispatcher("/profile/"+usernameRequest);
        }
        else
        response.sendRedirect("/Instagrim/Redirect");
        */
    }
    
    public void getProfilePic(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        System.out.println("Returned profile pic");
        
    }
        //taken from Image.java servlet
    
        private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User);
        RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp"); // do i need this?
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);

    }
    
    public void getUserPictures(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
         System.out.println("Attempting to get user pictures");
         response.setContentType("text/html");
         PrintWriter pw = response.getWriter();
         
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics"); //Pics
            if (lsPics == null) {
        
        System.out.println("request for pics:" + lsPics);
        
        } else {
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) {
                
                Pic p = (Pic) iterator.next();
                //pw.println("<img id='userPic' src='" + iterator.next() + "' alt='userImage' style='width:100px;height:100px'>");
                System.out.print("Iterating");
                pw.println("<p><a href='/Instagrim/Image/" + p.getSUUID() + "<img id='userPic' src='/Instagrim/Thumb/" + p.getSUUID() + "' alt'userImage' </a></p><br/>");

            }
            }
            pw.flush();
        RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp"); // /UsersPics.jsp
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);  
        
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
