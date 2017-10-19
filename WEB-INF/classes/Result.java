
/* A servlet that retrieves MovieDB information based on the query requested */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class Result extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String limit = " LIMIT 10 OFFSET %d";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Result() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    // Checks to see if the session attribute "loggedIn" is set and redirect to login if it is not
    private boolean loggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	if(request.getSession().getAttribute("loggedIn") != null)
    		return true;
    	
    	return false;
    }
    
    public ArrayList<Movie> movieSublist(HttpServletRequest request, Connection dbcon, Statement statement, int rowCount) throws SQLException {
    	String query = (String) request.getSession().getAttribute("query");
    	/* ADD THIS CODE TO SORT BY TITLE AND YEAR */
    	String order = (String) request.getSession().getAttribute("order");
    	if(order != null) {
	    	if("titleASC".equals(order)) {
	    		query += " ORDER BY m.title ASC";
	    	} else if("titleDESC".equals(order)) {
	    		query += " ORDER BY m.title DESC";
	    	} else if("yearASC".equals(order)) {
	    		query += " ORDER BY m.year ASC";
	    	} else if("yearDESC".equals(order)) {
	    		query += " ORDER BY m.year DESC";
	    	}
    	} else {
    		query += " ORDER BY m.id";
    	}
    	query += String.format(limit, rowCount);
        
        // Perform the query
        ResultSet rs = statement.executeQuery(query);

        ArrayList<Movie> movies = new ArrayList<Movie>();
        while (rs.next()) {
        	Movie movie = new Movie();
        	movie.setID(rs.getInt("id"));
        	movie.setTitle(rs.getString("title"));
        	movie.setYear(rs.getInt("year"));
        	movie.setDirector(rs.getString("director"));
        	movie.setBannerURL(rs.getString("banner_url"));
        	movie.setTrailerURL(rs.getString("trailer_url"));
        	// add stars
        	PreparedStatement ps = dbcon.prepareStatement("SELECT s.id, s.first_name, s.last_name "
        											   + "FROM stars as s, stars_in_movies as sim "
        											   + "WHERE s.id = sim.star_id AND sim.movie_id = ?");
        	ps.setInt(1, rs.getInt("id"));
        	ResultSet r = ps.executeQuery();
        	
        	HashMap<Integer, String> stars = new HashMap<Integer, String>();
        	while(r.next()) {
        		stars.put(r.getInt("id"), r.getString("first_name") + " " + r.getString("last_name"));
        	}
        	movie.setStars(stars);
        	// add genres
        	ps = dbcon.prepareStatement("SELECT genres.name "
                    				  + "FROM genres, genres_in_movies "
                    				  + "WHERE genres.id = genres_in_movies.genre_id "
                    				  + "AND genres_in_movies.movie_id = ?");
        	ps.setInt(1, rs.getInt("id"));
        	r = ps.executeQuery();
        	
        	ArrayList<String> genres = new ArrayList<String>();
        	while(r.next()) {
        		genres.add(r.getString("name"));
        	}
        	movie.setGenres(genres);
        	r.close();
        	movies.add(movie);
        }
        rs.close();
        return movies;
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!loggedIn(request, response)) {
			response.sendRedirect(request.getContextPath() + "/index.html");
			return;
		}
		
//		String loginUser = "root";
//        String loginPasswd = "alexmojulian";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type

        try {
//        	Class.forName("com.mysql.jdbc.Driver").newInstance();
//
//            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        	
        	PrintWriter out = response.getWriter();
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
            
            int rowCount = 0;
            if(request.getParameter("previous") != null) {
            	rowCount = Integer.parseInt(request.getParameter("rowCount"));
            	// Won't go negative
            	if(rowCount > 0) {
            		rowCount -= 10;
            		request.setAttribute("rowCount", Integer.toString(rowCount));
            	} else {
            		request.setAttribute("rowCount", Integer.toString(0));
            	}
            } else if(request.getParameter("next") != null) {
            	rowCount = Integer.parseInt(request.getParameter("rowCount"));
            	// Add a check here to prevent overflow
            	rowCount += 10;
            	request.setAttribute("rowCount", Integer.toString(rowCount));
            } else if(request.getParameter("orderTitle") != null) {
            	if("ASC".equals(request.getParameter("orderTitle")))
            		request.getSession().setAttribute("order", "titleASC");
            	else
            		request.getSession().setAttribute("order", "titleDESC");
            	rowCount = 0;
            	request.setAttribute("rowCount", Integer.toString(rowCount));
            } else if(request.getParameter("orderYear") != null) {
            	if("ASC".equals(request.getParameter("orderYear")))
            		request.getSession().setAttribute("order", "yearASC");
            	else
            		request.getSession().setAttribute("order", "yearDESC");
            	rowCount = 0;
            	request.setAttribute("rowCount", Integer.toString(rowCount));
            } else if(request.getParameter("searchsubmit") != null) {
            	String search = request.getParameter("search");
            	// Search input was not empty
            	if(!search.trim().isEmpty()) {
            		rowCount = 0;
                	request.setAttribute("rowCount", Integer.toString(rowCount));
                	
            		String option = request.getParameter("searchoption");
            		String q = null;

        			// Category is Star
        			if(option.equals("star")) {
        				String[] name = search.trim().split(" +");
        				// first OR last
        				if(name.length < 2) {
            				q = String.format("SELECT m.* "
            								+ "FROM movies as m, stars as s, stars_in_movies as sim "
            								+ "WHERE m.id = sim.movie_id AND s.id = sim.star_id AND "
            								+ "(s.first_name LIKE '%%%s%%' OR s.last_name LIKE '%%%<s%%')", name[0]);
        				} else { // first AND last
            				q = String.format("SELECT m.* "
    										+ "FROM movies as m, stars as s, stars_in_movies as sim "
    										+ "WHERE m.id = sim.movie_id AND s.id = sim.star_id AND "
    										+ "s.first_name LIKE '%%%s%%' AND s.last_name LIKE '%%%s%%'", name[0], name[1]);
        				}
        			} else { // Every other category
        				if(option.equals("title")) {
        					q = String.format("SELECT m.* FROM movies as m WHERE m.title LIKE '%%%s%%'", search);
        				} else if(option.equals("director")) {
        					q = String.format("SELECT m.* FROM movies as m WHERE m.director LIKE '%%%s%%'", search);
        				} else if(option.equals("year")) {
        					q = String.format("SELECT m.* FROM movies as m WHERE m.year = %s", search);
        				}
        			}
        			request.getSession().setAttribute("query", q);
            	} else
            		return;
            } else { // no button pressed (first time page loaded)
            	if(request.getSession().getAttribute("query") == null)
            		request.getSession().setAttribute("query", "SELECT m.* FROM movies as m");
            	if(request.getParameter("home") != null)
            		request.getSession().setAttribute("query", "SELECT m.* FROM movies as m");
            	
            	// Add to Cart was pressed
            	if(request.getParameter("addToCart") != null) {
            		rowCount = Integer.parseInt(request.getParameter("rowCount"));
            		request.setAttribute("rowCount", rowCount);
            	} else {
            		request.setAttribute("rowCount", Integer.toString(rowCount));
            		request.getSession().removeAttribute("order");
            	}
            }
            
            ArrayList<Movie> movies = movieSublist(request, dbcon, statement, rowCount);
            request.setAttribute("movies", movies);
            request.getRequestDispatcher("/home.jsp").forward(request, response);

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
        	ex.printStackTrace();
        	return;
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
