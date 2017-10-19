

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Checkout() {
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

	    	DataSource ds = (DataSource) envCtx.lookup("jdbc/Master");

	    	if (ds == null)
	    	    out.println("ds is null.");

	    	Connection dbcon = ds.getConnection();
	    	if (dbcon == null)
	    	    out.println("dbcon is null.");
	        // Declare our statement
//	        Statement statement = dbcon.createStatement();
	        ResultSet rs = null;
	
	        if(request.getParameter("checkout") != null) {
	        	// displaying items at checkout
	        	HashMap<Movie, Integer> items = new HashMap<Movie, Integer>();
	        	if(request.getSession().getAttribute("cart") != null) {
	        		Cart cart = (Cart) request.getSession().getAttribute("cart");
	        		HashMap<Integer, Integer> movies = cart.getCart();
	        		for(Map.Entry<Integer, Integer> movie : movies.entrySet()) {
	        			// id, title, year, director, bannerURL
	        			Movie m = new Movie();
	        			PreparedStatement ps = dbcon.prepareStatement("SELECT * FROM movies WHERE id = ?");
	        			ps.setInt(1, movie.getKey());
	        			rs = ps.executeQuery();
//	        			rs = statement.executeQuery(String.format("SELECT * FROM movies WHERE id = %d", movie.getKey()));
	        			if(rs.next()) {
	        	        	m.setID(rs.getInt("id"));
	        	        	m.setTitle(rs.getString("title"));
	        	        	m.setYear(rs.getInt("year"));
	        	        	m.setDirector(rs.getString("director"));
	        	        	m.setBannerURL(rs.getString("banner_url"));
	        	        	items.put(m, movie.getValue());
	        			}
	        		}
	        	}
	        	request.setAttribute("items", items);
	        	request.getRequestDispatcher("/checkout.jsp").forward(request, response);
	        } else if(request.getParameter("order") != null) {
	        	// checking credit cards and performing purchase
	        	String firstName, lastName, ccNum, exp;
	        	firstName = request.getParameter("ccfirstname");
	        	lastName = request.getParameter("cclastname");
	        	ccNum = request.getParameter("ccnumber");
	        	exp = request.getParameter("ccexpiration");
	        	PreparedStatement ps = dbcon.prepareStatement("SELECT * FROM creditcards as cc "
													  		+ "WHERE cc.id = ? AND cc.first_name = ? "
													  		+ "AND cc.last_name = ? AND cc.expiration = DATE?");
	        	ps.setString(1, ccNum);
	        	ps.setString(2, firstName);
	        	ps.setString(3, lastName);
	        	ps.setString(4, exp);
	        	rs = ps.executeQuery();
//	        	rs = statement.executeQuery(String.format("SELECT * FROM creditcards as cc "
//	        											+ "WHERE cc.id = '%s' AND cc.first_name = '%s' "
//	        											+ "AND cc.last_name = '%s' AND cc.expiration = DATE'%s'", 
//	        											  ccNum, firstName, lastName, exp));
	        	
	        	// No matching card information
                if(!rs.isBeforeFirst()) {
                	response.sendRedirect("/servlet/Checkout?checkout=true");
                } else { // proceed with order
                	// add to sales table here and redirect to confirmation page
                	
                	if(request.getSession().getAttribute("cart") != null) {
                		Cart cart = (Cart) request.getSession().getAttribute("cart");
    	        		
    	        		if(!cart.emptyCart()){
    	        			HashMap<Integer, Integer> movies = cart.getCart();
    	        			String custID = (String) request.getSession().getAttribute("loggedIn");
    	        			
    	        			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    	                	LocalDate localDate = LocalDate.now();
    	                	String date = dtf.format(localDate);
    	                	
    	                	for(Map.Entry<Integer, Integer> movie : movies.entrySet()) {
    	                		for(int i = 0; i < movie.getValue(); i++) {
    	                			String u  = "INSERT INTO sales (customer_id, movie_id, sale_date) "
											  + "VALUES (?, ?, ?)";
    	                			ps.setInt(1, Integer.parseInt(custID));
    	                			ps.setInt(2, movie.getKey());
    	                			ps.setString(3, date);
    	                			ps.executeUpdate();
    	                			
//    	                			int r = statement.executeUpdate(String.format("INSERT INTO sales (customer_id, movie_id, sale_date) "
//    	                													 + "VALUES (%s, %s, '%s')", custID, movie.getKey(), date));
    	                		}
    	                	}
    	                	// reset cart
    	                	cart = new Cart();
    	                	request.getSession().setAttribute("cart", cart);
    	                	// redirect to confirmation
    	                	response.sendRedirect(request.getContextPath() + "/confirmation.jsp");
    	                	return;
    	        		}
                	}
                	response.sendRedirect("/servlet/Checkout?checkout=true");
                }
	        } 
	        
    		if(rs != null)
    			rs.close();
//	        statement.close();
	        dbcon.close();
	    } catch (SQLException ex) {
	    	request.getRequestDispatcher("/servlet/Checkout?checkout=true").include(request, response);
	    } catch(java.lang.Exception ex) {
	    	System.out.println(ex);
	    	System.out.println ("Exception:  " + ex.getMessage ());
	    	ex.printStackTrace();
	    	return;
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
