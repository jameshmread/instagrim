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
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.*;
import uk.ac.dundee.computing.aec.instagrim.models.*;
/**
 *
 * @author James
 */
@WebServlet(name = "profile", 
        urlPatterns = {
         "/profile" ,
         "/profile/*",
         "/DeleteProfile/"
        })

public class profile extends HttpServlet {

    Cluster cluster=null;
    HttpSession session;
    ProfileInfo profileInfo = new ProfileInfo();
    private HashMap commandsMap = new HashMap();
    
    public profile(){
        super();
        commandsMap.put("profile", 1);
        commandsMap.put("DeleteProfile", 2);
}
    
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
        
        String args[] = Convertors.SplitRequestPath(request);
        int command;
        try {
            command = (Integer) commandsMap.get(args[1]);
        } catch (Exception et) {
            response.sendError(500);
            return;
        }
        switch (command) {
            case 1:
                getProfile(request, response);
                break;
            case 2:
                System.out.println("Delete profile called");
                deleteProfile(request, response);
                break;
                      
            default:
                response.sendError(500);
        }
        
                //System.out.println("Returned profile pic");                       
        //User user = new User();
        //String usernameRequest = request.getParameter("username");
        //String passwordRequest = request.getParameter("password");
      
        
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
       
    }
    
    public void getProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        session = request.getSession();
        LoggedIn loggedIn = (LoggedIn)session.getAttribute("LoggedIn");
        //ProfileInfo profileInfo = (ProfileInfo)session.getAttribute("ProfileInfo");
        String username = loggedIn.getUsername();
        
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(username);
        request.setAttribute("Pics", lsPics); 
        //removed call to method displayImageList
        //as it requestDispatched then profile had to request dispatch
        
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

    private void deleteProfile(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
            System.out.println("Deleting user");
            HttpSession session = request.getSession();
            LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn");
            String username = lg.getUsername();
            User us = new User();
            if(username !=null){
            us.deleteProfile(username);
            lg.setLogedout();
            
            response.sendRedirect("/Instagrim");
            }else{
                System.out.println("Unable to delete user.");
            }
     }

}
