
/* A servlet to login users based on the credentials found in movieDB database */

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;
import java.util.*;

public class Login extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public String getServletInfo()
    {
    	return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException 
    {
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

//        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
//		
//		// Verify CAPTCHA.
//		boolean valid = VerifyUtils.verify(gRecaptchaResponse);
//		if (!valid) {
//			response.sendRedirect(request.getContextPath() + "/index.html?invalid=invalid");
//			return;
//		}

        response.setContentType("text/html");    // Response mime type

        try {
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
            
            String action = request.getParameter("action");
            
            // Check to see if Login submit was pressed
            if("Login".equals(action)) {
            	String email = request.getParameter("email");
                String pass = request.getParameter("pass");
                String query = "SELECT * FROM customers WHERE email = ? AND password = ?";
                PreparedStatement ps = dbcon.prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, pass);
                
                ResultSet rs = ps.executeQuery();

                // Empty ResultSet
                if(!rs.isBeforeFirst()) {
//                	request.setAttribute("invalid", "true");
                	response.sendRedirect(request.getContextPath() + "/index.html?invalid=invalid");
                } else {
                	rs.next();
                	request.getSession().setAttribute("loggedIn", Integer.toString(rs.getInt("id")));
                	response.sendRedirect(request.getContextPath() + "/servlet/Result");
                }

                rs.close();
            } else { // Logout
            	HttpSession session = request.getSession(false);
            	if(session != null) {
            	    session.invalidate();
            	}
            	response.sendRedirect(request.getContextPath() + "/index.html");	
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
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
    	doGet(request, response);
	}
}
