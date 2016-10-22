/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.models.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;


/**
 *
 * @author James
 */
@WebServlet(name = "browse", urlPatterns = {"/browse"})
public class browse extends HttpServlet {
        Cluster cluster;
   

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
        
        PicModel pm = new PicModel();
        request.setAttribute("picList", pm.getAllPics());
        
        
        
        RequestDispatcher rd = request.getRequestDispatcher("/browse.jsp");
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
        
        //THIS SHOULD BE IN DO GET
        System.out.println("Browse dopost called");
        
        String pictureTitle = (String)request.getParameter("pictureTitle");
        System.out.println("Picture title to search for: " + pictureTitle);
        PicModel pm = new PicModel();
        pm.setCluster(cluster);
        LinkedList<Pic> pics = pm.getPicsWithTitle(pictureTitle);
        //search pm for get pic via title return, forward picture object onto the
        //jsp page
        request.setAttribute("searchedTitle", pictureTitle);
        request.setAttribute("picList", pics);
        RequestDispatcher rd = request.getRequestDispatcher("browse.jsp");
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
