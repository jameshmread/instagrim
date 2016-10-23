
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
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
import uk.ac.dundee.computing.aec.instagrim.models.*;
import uk.ac.dundee.computing.aec.instagrim.stores.*;

/**
 *
 * @author James Read
 */
@WebServlet(name = "pictureServlet", urlPatterns = 
        {"/pictureServlet", 
         "/pictureServlet/*",
         "/delete/*",
        })
public class pictureServlet extends HttpServlet {
        
    private Cluster cluster;
    private HashMap commandsMap = new HashMap();
    private LoggedIn lg;
    private HttpSession session;
       
    public pictureServlet(){
        super();
        commandsMap.put("pictureServlet", 1);
        commandsMap.put("delete", 3);
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
            
        System.out.println("PICTURE SERVLET CALLED");
        HttpSession session = request.getSession();
        lg = (LoggedIn)session.getAttribute("LoggedIn");
        String username = lg.getUsername();
        
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
            {
                System.out.println("GET CONTENT: " + args[2]);
                getContent(request, response, args[2]);
            }
            break;
            case 2:
                //getContent(request, response);
                
                break;
            case 3:
                deletePicture(request, response, args[2], username);
                break;
            default:
                response.sendError(500);
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
        System.out.println("pictureServlet.doPost() called"); 
        HttpSession session = request.getSession();
        lg = (LoggedIn)session.getAttribute("LoggedIn");
        String picID = (String)request.getParameter("picID");
        PicModel pm = new PicModel();
        pm.setCluster(cluster);
        
        if("true".equals(request.getParameter("postComment"))){ 
            String commentText = request.getParameter("commentText");
            String username = lg.getUsername();
            
            pm.insertComment(commentText, username, picID);
            request.setAttribute("picID", picID);
            response.sendRedirect("/Instagrim/pictureServlet/" + picID);
            //use redirects for post, request dispatchers for get 
            // http://www.javapractices.com/topic/TopicAction.do?Id=181
        }else
            if("true".equals(request.getParameter("like"))){
                String username = lg.getUsername();
                //get the specific user if they have liked this, dont itterate through all the users
                //if user has not liked this, set like, else if liked set unlike
                if(!pm.userLikedPicture(username, picID)) pm.setLike(username, picID);
                else pm.setUnlike(username, picID);
                request.setAttribute("picID", picID);
                response.sendRedirect("/Instagrim/pictureServlet/" + picID);
            }
         
    }

    /**
     * Handles deleting a picture, only accepts delete if userame = the user who owns the picture
     *
     * @param request servlet request
     * @param response servlet response
     * @param picID the picture id to delete
     * @param username the username who is deleting the picture (security check)
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void deletePicture(HttpServletRequest request, HttpServletResponse response, String picID, String username)
    throws ServletException, IOException{
           PicModel pm = new PicModel();
        pm.setCluster(cluster);
            System.out.println("PictureID to delete: " + picID);
            
            pm.deletePicture(picID, username);
            System.out.println("Picture deleted");
            response.sendRedirect("/Instagrim/profile/"+lg.getUsername());
        
      }

    /**
     * Handles getting the page content for the picture.jsp
     *
     * @param request servlet request
     * @param response servlet response
     * @param picID the picture id which will be displayed
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void getContent(HttpServletRequest request, HttpServletResponse response, String picID) 
    throws IOException, ServletException{
        
        String pictureIDToGo = (String)picID;
        System.out.println("Picture serverlet recieved the PIC ID as(ARG): " + pictureIDToGo);
        
        PicModel pm = new PicModel();
        pm.setCluster(cluster);
        String picTitle = (String)pm.getPicTitle(pictureIDToGo);
        LinkedList<comment> comments = pm.getCommentList(pictureIDToGo);
        LinkedList<String> likes = pm.getLikes(pictureIDToGo);
        
        request.setAttribute("pictureID", pictureIDToGo);
        request.setAttribute("picTitle", picTitle);
        request.setAttribute("likes", likes);
        request.setAttribute("comments", comments);
        
        System.out.println("Pic Title is: " + picTitle);
        RequestDispatcher rd = request.getRequestDispatcher("/picture.jsp");
        rd.forward(request, response);
    }
    
        /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles the picture pages content along with likes/comments and deleting pictures";
    }// </editor-fold>

}
