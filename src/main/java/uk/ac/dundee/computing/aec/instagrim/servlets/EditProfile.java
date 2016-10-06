/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.ProfileInfo;
/**
 *
 * @author James
 */
@WebServlet(name = "EditProfile", urlPatterns = {"/EditProfile"})
public class EditProfile extends HttpServlet {

        Cluster cluster =null;
        

  

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
           
        HttpSession session = request.getSession();
           
            User us = new User();
            
            //cluster = CassandraHosts.getCluster();
            //cluster.connect("instagrim");
            
        String firstName = (String)request.getParameter("firstName");
        String lastName = (String)request.getParameter("lastName");
        String bio = (String)request.getParameter("bio");
        String email = (String)request.getParameter("email");
        
        ProfileInfo profile = (ProfileInfo)session.getAttribute("ProfileInfo");
        
        //String oldUsername = profile.getUsername();
        //String oldBio = profile.getBio();
        
            //changing the store for the session 
            us.setProfileStoreInfo(firstName, lastName, email, bio);
            us.setProfileDatabaseInfo(firstName, lastName, email, bio);
            //loggedIn.setUsername(username); might leave change username out all together
            //profile.setProfilePicture(); ####
            //Update database ###
            
            session.setAttribute("ProfileInfo", profile);
            
            System.out.println("Session in servlet "+session);
            RequestDispatcher rd=request.getRequestDispatcher("profile.jsp"); //index.jsp
	    rd.forward(request,response);
        
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
