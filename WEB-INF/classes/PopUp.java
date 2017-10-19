

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import org.json.JSONArray;

public class PopUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PopUp() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		/*String loginUser = "root";
        String loginPasswd = "alexmojulian";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        */
        
        response.setContentType("text/html");
        
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
            
    		String id = request.getParameter("id");
    		String q = String.format("SELECT * FROM movies WHERE id = %s", id);
            ResultSet rs = statement.executeQuery(q);
            Movie movie = new Movie();
            if(rs.next()) {
            	movie.setID(rs.getInt("id"));
            	movie.setTitle(rs.getString("title"));
            	movie.setYear(rs.getInt("year"));
            	movie.setDirector(rs.getString("director"));
            	movie.setBannerURL(rs.getString("banner_url"));
            	movie.setTrailerURL(rs.getString("trailer_url"));
            	// add stars
            	Statement statement2 = dbcon.createStatement();
            	ResultSet r = statement2.executeQuery(String.format("SELECT s.id, s.first_name, s.last_name "
            													 + "FROM stars as s, stars_in_movies as sim "
            													 + "WHERE s.id = sim.star_id AND sim.movie_id = %d", rs.getInt("id")));
            	HashMap<Integer, String> stars = new HashMap<Integer, String>();
            	while(r.next()) {
            		stars.put(r.getInt("id"), r.getString("first_name") + " " + r.getString("last_name"));
            	}
            	movie.setStars(stars);
            	// add genres
            	r = statement2.executeQuery(String.format("SELECT genres.name "
            			                               + "FROM genres, genres_in_movies "
            			                               + "WHERE genres.id = genres_in_movies.genre_id "
            			                               + "AND genres_in_movies.movie_id = %d", rs.getInt("id")));
            	ArrayList<String> genres = new ArrayList<String>();
            	while(r.next()) {
            		genres.add(r.getString("name"));
            	}
            	movie.setGenres(genres);
            	r.close();
            }
            //String s = "<h1>This is a title</h1>";
    		String s = String.format("<table id='popup'>" +
    				"<tr>" + 
    				"<td id='movie_img'>" + 
    					"<img src='%s'><br>" + 
    					"<form action='%s/servlet/AddCart' method='get'>" +
    					"<button type='submit' name='addToCart' value='%s'>Add to Cart</button>" +
    					"</form>" + 
    		        "</td>" + 
    		        "<td>" + 
    					"ID: %s<br>" + 
    		            "Title: %s<br>" + 
    		            "Year: %s<br>" + 
    		            "Director: %s<br>" + 
    		            "Genres: ", 
    		            movie.getBannerURL(),
    		            request.getContextPath(),
    		            movie.getID(),
    		            movie.getID(),
    		            movie.getTitle(),
    		            movie.getYear(),
    		            movie.getDirector());
    		
    		for (int i = 0;  i < movie.getGenres().size(); i++){
    			if (i == movie.getGenres().size() -1){
    				s = s + movie.getGenres().get(i) + "<br>Stars: "; 
    			}
    			else{
    				s = s + movie.getGenres().get(i) + ", ";
    			}
    		}
    		int starSize = movie.getStars().size() -1;
    		int i = 0;
    		for (Integer key :  movie.getStars().keySet()){
    			
    			if (i == starSize){
    				s = s + movie.getStars().get(key) + "<br>"; 
    			}
    			else{
    				s = s + movie.getStars().get(key) + ", ";
    				i++;
    			}
    		}
    		s = s + String.format("<a href='%s'>Trailer</a>" + 
    				"</td>" + 
   		         "</tr>" + 
   		         "</table>", movie.getTrailerURL());
    		//PrintWriter out = response.getWriter();
    		out.write(s);
    		
    		
    		
            statement.close();
            dbcon.close();
        } catch (SQLException ex) {
        	while (ex != null) {
        		System.out.println ("SQL Exception:  " + ex.getMessage ());
        		ex = ex.getNextException ();
            }  // end while
        } catch(java.lang.Exception ex) {
        	System.out.println(ex);
        	ex.printStackTrace();
        	return;
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
