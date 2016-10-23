
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
import uk.ac.dundee.computing.aec.instagrim.stores.*;
/**
 *
 * @author James
 */
@WebServlet(name = "EditProfile", urlPatterns = 
        {"/EditProfile",
        })
public class EditProfile extends HttpServlet {

        Cluster cluster =null;
        ProfileInfo profile;
        LoggedIn lg;
  

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
        RequestDispatcher rd = request.getRequestDispatcher("editProfile.jsp");
        rd.forward(request, response);
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * Handles the editing of user profile from editprofile.jsp
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Editprofile servlet dopost called");
           
        
            HttpSession session = request.getSession();
            profile = (ProfileInfo)session.getAttribute("ProfileInfo");
            User us = new User();
            lg = new LoggedIn();
            String firstName,lastName,bio,email = null;
        //request.getParts(); should probably change the above to this as it would be more maintainable
        firstName = (String)request.getParameter("firstName");
        lastName = (String)request.getParameter("lastName");
        bio = (String)request.getParameter("bio");
        email = (String)request.getParameter("email");
        /*the following is if the user enters blank information, 
            the profile is just 'updated' with the existing info
        */
        if(firstName == null) firstName = profile.getFirst_name();
        if(lastName == null) lastName = profile.getLast_name();
        if(bio == null) bio = profile.getBio();
        if(email == null) email = profile.getEmail();
        
   
        lg = (LoggedIn)session.getAttribute("LoggedIn");
        
        setProfileStoreInfo(firstName, lastName, email, bio, request);
        us.setProfileDatabaseInfo(lg.getUsername(),firstName, lastName, email, bio);            
            
        session.setAttribute("ProfileInfo", profile);
            
        System.out.println("Session in servlet "+session);

        response.sendRedirect("/Instagrim/profile/" + lg.getUsername());
        
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     * Handles the editing of user profile from editprofile.jsp
     *
     * @param first_name users first name
     * @param last_name users last name
     * @param email users email address
     * @param bio users bio
     * @param request servlet request
     * 
     */
    public void setProfileStoreInfo(String first_name, String last_name, String email, String bio,HttpServletRequest request){
        HttpSession session = request.getSession();
        profile = (ProfileInfo)session.getAttribute("ProfileInfo"); 
        System.out.println(first_name);
        System.out.println(last_name);
        System.out.println(email);
        profile.setFirst_name(first_name);
        profile.setLast_name(last_name);
        profile.setEmail(email); 
        profile.setBio(bio);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles the editProfile.jsp data, for editing the users profile";
    }// </editor-fold>

}
