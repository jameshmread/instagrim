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
        request.setAttribute("pictureID", pictureIDToGo); 
        request.setAttribute("picTitle", picTitle);
        
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
        if( "true".equals(request.getParameter("delete"))){
            System.out.println("Delete Parameter: " + request.getParameter("delete"));
            System.out.println("PictureID to delete: " + request.getParameter("picID")); 
            
            String pictureIDRecieved = (String)request.getParameter("picID");
            //get delete working then worry about the code duplication of request picID
            PicModel pm = new PicModel();
            pm.setCluster(cluster);
            
            pm.deletePicture(pictureIDRecieved, lg.getUsername());
            System.out.println("Picture deleted");
            //RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
            //rd.forward(request, response);
            response.sendRedirect("/Instagrim/profile");
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
