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
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author James
 */
@WebServlet(name = "profile", urlPatterns = {"/profile"})
public class profile extends HttpServlet {

    Cluster cluster=null;

    
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
                String logOut = null;
        logOut = request.getParameter("LogOut");
        HttpSession session=request.getSession();
        
        LoggedIn lg= new LoggedIn();
        lg.setLogedout();
        session.invalidate();
            RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
	    rd.forward(request,response);
            System.out.println("<header> Logged Out </header>");
        if(logOut !=null)
        {
            request.setAttribute(logOut, lg);
            
            session.invalidate();
            //RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
	    rd.forward(request,response);
            
    }
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
        String logOut = null;
        logOut = request.getParameter("LogOut");
        HttpSession session=request.getSession();
        
        LoggedIn lg= new LoggedIn();
        lg.setLogedout();
        if(logOut !=null)
        {
            request.setAttribute(logOut, lg);
            //cluster.close();
            session.invalidate();
            //RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
	    //rd.forward(request,response);
            
        }
        */
        
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
