
/* A servlet that processes the Browse category chosen and forwards to Result */

import java.sql.*;
import java.util.*;
import java.io.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class Browse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Browse() {
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
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		*/
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

            if(request.getParameter("title") != null) {
            	String title = request.getParameter("title");
            	request.getSession().setAttribute("query", String.format("SELECT m.* FROM movies as m "
            			                                               + "WHERE m.title LIKE '%s%%'", title));
            	request.getRequestDispatcher("/servlet/Result").forward(request, response);
            	return;
            } else if(request.getParameter("genre") != null) {
            	String genre = request.getParameter("genre");
            	request.getSession().setAttribute("query", String.format("SELECT m.* FROM movies as m, genres_in_movies "
                                                                       + "WHERE m.id = genres_in_movies.movie_id "
                                                                       + "AND genres_in_movies.genre_id = %s", genre));
            	request.getRequestDispatcher("/servlet/Result").forward(request, response);
            	return;
            } else { // no button pressed (first time page loaded)
            	ResultSet rs = statement.executeQuery("SELECT * FROM genres");
            	// ArrayList<String> genres = new ArrayList<String>();
            	HashMap<Integer, String> genres = new HashMap<Integer, String>();
            	while(rs.next()) {
            		genres.put(rs.getInt("id"), rs.getString("name"));
            		// genres.add(rs.getString("name"));
            	}
            	request.setAttribute("genres", genres);
            	request.getRequestDispatcher("/browse.jsp").forward(request, response);
            	rs.close();
            }
            
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
