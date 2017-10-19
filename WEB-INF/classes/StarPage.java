

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class StarPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StarPage() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    // Checks to see if the session attribute "loggedIn" is set and redirect to login if it is not
    private boolean loggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	if(request.getSession().getAttribute("loggedIn") != null)
    		return true;
    	
    	return false;
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!loggedIn(request, response)) {
			response.sendRedirect(request.getContextPath() + "/index.html");
			return;
		}
		PrintWriter out = response.getWriter();
		/*String loginUser = "root";
        String loginPasswd = "alexmojulian";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";*/

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
            Statement statement = dbcon.createStatement();
            
            String q = String.format("SELECT * FROM stars WHERE id = %s", request.getParameter("star"));
            ResultSet rs = statement.executeQuery(q);
            Star star = new Star();
            if(rs.next()) {
            	star.setID(rs.getInt("id"));
            	star.setFirstName(rs.getString("first_name"));
            	star.setLastName(rs.getString("last_name"));
            	star.setDOB(rs.getString("dob"));
            	star.setPhotoURL(rs.getString("photo_url"));
            	// add movies
            	Statement statement2 = dbcon.createStatement();
            	ResultSet r = statement2.executeQuery(String.format("SELECT m.id, m.title "
            													  + "FROM movies as m, stars_in_movies as sim "
            													  + "WHERE sim.star_id = %s AND sim.movie_id = m.id", request.getParameter("star")));
            	HashMap<Integer, String> movies = new HashMap<Integer, String>();
            	while(r.next()) {
            		movies.put(r.getInt("id"), r.getString("title"));
            	}
            	star.setMovies(movies);
            	r.close();
            }
            request.setAttribute("star", star);
            request.getRequestDispatcher("/star.jsp").forward(request, response);
            
            rs.close();
            statement.close();
            dbcon.close();
        } catch (SQLException ex) {
        	while (ex != null) {
        		System.out.println ("SQL Exception:  " + ex.getMessage ());
        		ex = ex.getNextException ();
            }  // end while
        } catch(java.lang.Exception ex) {
        	System.out.println(ex);
        	System.out.println ("Exception:  " + ex.getMessage ());
        	return;
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
