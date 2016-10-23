
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;

/**
 *
 * @author AEC, James Read
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {
    
    private Cluster cluster=null;
    private User us;
    private PicModel pm;
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }


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
        RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
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
        
        us=new User();
        us.setCluster(cluster);
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String confirmPassword=request.getParameter("confirmPassword");
        
        String first_name=request.getParameter("first_name");
        String last_name=request.getParameter("last_name");
        String email=request.getParameter("email");
                
        //checks if ANY of the mandatory fields are invalid, sends response back and prevents registration
        if(!password.equals(confirmPassword) || password.isEmpty() 
                || username == null || username.isEmpty() || us.usernameAlreadyExists(username)){
            
            if(!password.equals(confirmPassword)) request.setAttribute("passwordError","noMatch");
            if(password.isEmpty()) request.setAttribute("passwordError", "empty");
            if(username ==null || username.isEmpty()) request.setAttribute("usernameError", "empty");
            if(us.usernameAlreadyExists(username)) request.setAttribute("usernameError", "exists");
            
            RequestDispatcher rd=request.getRequestDispatcher("register.jsp");            
            rd.forward(request, response);
        }
        else {
            us.RegisterUser(username, password, first_name, last_name, email); //creates a user in database
            pm = new PicModel();
            pm.setCluster(cluster);
            java.util.UUID uuid = java.util.UUID.randomUUID();
            pm.setDatabaseProfilePicture(username, uuid); //needed to create a place holder profile picture        
            
            response.sendRedirect("/Instagrim");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles the registration process for the "
                + "user and includes mandatory field validation";
    }// </editor-fold>

}
