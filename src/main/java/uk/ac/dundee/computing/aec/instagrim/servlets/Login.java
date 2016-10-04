/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
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
 * @author Administrator
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    Cluster cluster=null;

    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session;
        
        String logOut = null;
        logOut = request.getParameter("profile");
        session=request.getSession();
        
        LoggedIn lg= new LoggedIn();
        lg.setLogedout();
        session.invalidate();
        
        RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
        rd.forward(request,response);
        System.out.println("<header> Logged Out </header>");
        
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
        
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        System.out.println("Login Using post method on Login.java Servlet");
        
        User us=new User();
        us.setCluster(cluster);
        
        boolean isValid=us.IsValidUser(username, password);
        HttpSession session=request.getSession();
        
        System.out.println("Session in servlet "+session);
        
        
        if (isValid){
            us.getUserInfo(username);
            LoggedIn lg= new LoggedIn();
            lg.setLogedin();
            lg.setUsername(username);
            request.setAttribute("LoggedIn", lg);  
            session.setAttribute("LoggedIn", lg);
            System.out.println("Session in servlet "+session);
            
            ProfileInfo profileInfo = new ProfileInfo();
            profileInfo.setFirst_name(us.getUserInfo(username)[0]);
            profileInfo.setLast_name(us.getUserInfo(username)[1]);
            profileInfo.setEmail(us.getUserInfo(username)[2]);
            //THIS STUFF USES THE MODEL AND RETURNS USER INFO FROM DATABASE WHEN SEARCHING USING USERNAME
            
            //request.setAttribute("ProfileInfo", profileInfo);
            session.setAttribute("ProfileInfo", profileInfo);
            //need to call model server.getifo then set the store to those return values
            
            RequestDispatcher rd=request.getRequestDispatcher("index.jsp"); //index.jsp
	    rd.forward(request,response);
            
        }else{
            
            response.sendRedirect("/Instagrim/login.jsp");
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
