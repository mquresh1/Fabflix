

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

public class DashboardLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DashboardLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Output stream to STDOUT
        PrintWriter out = response.getWriter();
    	
    	//String loginUser = "root";
        //String loginPasswd = "alexmojulian";
        //String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type

        try {
        	//Class.forName("org.gjt.mm.mysql.Driver");
        	//Class.forName("com.mysql.jdbc.Driver").newInstance();

            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        	Context initCtx = new InitialContext();

        	Context envCtx = (Context) initCtx.lookup("java:comp/env");
        	if (envCtx == null)
        	    out.println("envCtx is NULL");

        	Random random = new Random();
        	int num = random.nextInt(2) + 1;
        	DataSource ds;
        	if(num == 1)            
        	    ds = (DataSource) envCtx.lookup("jdbc/Master");
        	else
        	    ds = (DataSource) envCtx.lookup("jdbc/Slave");

        	if (ds == null)
        	    out.println("ds is null.");

        	Connection dbcon = ds.getConnection();
        	if (dbcon == null)
        	    out.println("dbcon is null.");
            // Declare our statement
//            Statement statement = dbcon.createStatement();
            
            String action = request.getParameter("action");
            
            // Check to see if Login submit was pressed
            if("Login".equals(action)) {
            	String email = request.getParameter("email");
                String pass = request.getParameter("pass");
                String query = "SELECT * FROM employees WHERE email = ? AND password = ?";
                PreparedStatement ps = dbcon.prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, pass);
                
//                String query = String.format("SELECT * FROM employees WHERE email = '%s' AND password = '%s'", 
//                							 email, pass);

                // Perform the query
//                ResultSet rs = statement.executeQuery(query);
                ResultSet rs = ps.executeQuery();

                // Empty ResultSet
                if(!rs.isBeforeFirst()) {
                	response.sendRedirect(request.getContextPath() + "/_dashboard.html?invalid=invalid");
                } else {
                	rs.next();
                	request.getSession().setAttribute("loggedIn", "loggedIn");
                	response.sendRedirect(request.getContextPath() + "/dashboardMenu.jsp");
                }

                rs.close();
            } else { // Logout
            	HttpSession session = request.getSession(false);
            	if(session != null) {
            	    session.invalidate();
            	}
            	response.sendRedirect(request.getContextPath() + "/_dashboard.html");	
            }

//            statement.close();
            dbcon.close();
        } catch (SQLException ex) {
        	while (ex != null) {
        		System.out.println ("SQL Exception:  " + ex.getMessage ());
        		ex = ex.getNextException ();
            }  // end while
        } catch(java.lang.Exception ex) {
        	out.println("<HTML>" + 
        				"<HEAD><TITLE>" + 
        				"MovieDB: Error" + 
        				"</TITLE></HEAD>\n<BODY>" + 
        				"<P>SQL error in doGet: " +
                        ex.getMessage() + "</P></BODY></HTML>");
        	return;
        }
        out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
