

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONObject;

/**
 * Servlet implementation class MobileLogin
 */
public class MobileLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Receive String information from Android and convert to JSON
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    }    
	    JSONObject obj = new JSONObject(sb.toString());
	    
	    String email = (String) obj.get("email");
	    String pass = (String) obj.get("password");
	    
	    String loginUser = "root";
        String loginPasswd = "alexmojulian";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        try {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
//            Statement statement = dbcon.createStatement();
            String query = "SELECT * FROM employees WHERE email = ? AND password = ?";
            PreparedStatement ps = dbcon.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, pass);
            
            ResultSet rs = ps.executeQuery();
            
//            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM customers WHERE email = '%s' AND password = '%s'", email, pass));
            
            // Empty ResultSet
            if(!rs.isBeforeFirst()) {
            	response.getWriter().write("fail");
            } else {
            	response.getWriter().write("pass");
            }

//            statement.close();
            dbcon.close();
        } catch (SQLException ex) {
        	while (ex != null) {
        		System.out.println ("SQL Exception:  " + ex.getMessage ());
        		ex = ex.getNextException ();
            }  // end while
        } catch(java.lang.Exception ex) {
        	ex.printStackTrace();
        	return;
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
