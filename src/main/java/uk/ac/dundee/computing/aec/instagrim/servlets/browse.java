
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;


/**
 *
 * @author James Read
 */
@WebServlet(name = "browse", urlPatterns = 
        
        {"/browse",
         "/browse/*",
         "/preBrowse/*"
        })

public class browse extends HttpServlet {
        private Cluster cluster;
        private HashMap commandsMap = new HashMap();
        private PicModel pm = new PicModel();
        

    /**
     * @see HttpServlet#HttpServlet()
     */
    public browse() {
        super();
        commandsMap.put("browse", 1);
        commandsMap.put("preBrowse", 2);
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
            response.sendError(404);
            return;
        }
        switch (command) {
            case 1: 
                System.out.println("Args 2: " + request.getParameter("pictureTitle"));
                if(request.getParameter("pictureTitle")==null){
                    getBrowseContent(request, response);}
                else{
                    getBrowseContent(request, response, (String)request.getParameter("pictureTitle"));}
                break;
            case 2:{
                System.out.println("DO get recieved XML request: " + request.getParameter("pictureTitle"));
                getSearchSuggestions(request,response,request.getParameter("pictureTitle"));
                }
                break;
            default:
                response.sendError(404);
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
    }

    /**
     * HReturns the browse content for all pictures with a specific title
     * looking back, i would have liked to add hashtags and use this to search them
     * but the principle is identical
     *
     * @param request servlet request
     * @param response servlet response
     * @param pictureTitle the picture title being searched for
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void getBrowseContent(HttpServletRequest request, HttpServletResponse response, String pictureTitle) 
    throws ServletException, IOException{
        
        pm = new PicModel();
        System.out.println("Picture title to search for: " + pictureTitle);
        
        pm.setCluster(cluster);
        LinkedList<Pic> pics = pm.getPicsWithTitle(pictureTitle);
       
        request.setAttribute("searchedTitle", pictureTitle);
        request.setAttribute("picList", pics);
        
        RequestDispatcher rd = request.getRequestDispatcher("browse.jsp");
        rd.forward(request, response);
    }
    
    /**
     * browses all pictures uploaded to the database 
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void getBrowseContent(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException{
            
        PicModel pm = new PicModel();
        pm.setCluster(cluster);
        request.setAttribute("picList", pm.getAllPics());
        RequestDispatcher rd = request.getRequestDispatcher("browse.jsp");
        rd.forward(request, response);
    }

    /**
     * Is called using AJAX and returns a list of possible titles to the html
     *
     * @param request servlet request
     * @param response servlet response
     * @param searchString the current search  string the user has typed in, updated every keyup
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void getSearchSuggestions(HttpServletRequest request, HttpServletResponse response, String searchString) 
    throws ServletException, IOException{
        
        pm.setCluster(cluster);
        LinkedList<String> returned = pm.getPictureTitles(searchString);
        System.out.println("Returned suggestions: " + returned);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter pw = response.getWriter();
        pw.print(returned);
        pw.flush();
        pw.close();
     }

        /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "handles browsing through all pics, searching for titles and contains ajax called function";
    }// </editor-fold>
}
