

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class Dash extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dash() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private boolean validYear(String year) {
    	try {
    		int y = Integer.parseInt(year);
    		if(0 <= y && y <= 9999)
    			return true;
    		
    		return false;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    // Prints the metadata for one table
 	private void printTableMetadata(String table, ResultSet result, PrintWriter out) throws SQLException {
 		out.println("<HTML>" + 
 					"<HEAD><TITLE>" + 
 					"Metadata" + 
 					"</TITLE></HEAD>\n<BODY>"); 
 		// Print table name
 		out.println("--- " +  table + " ---");
 		// Get metadata for table
 		ResultSetMetaData tableMetadata = result.getMetaData();
 		
 		// Print table information
 		for(int i = 1; i <= tableMetadata.getColumnCount(); i++) {
 			out.println("<p>Type of column " + i + " is " + tableMetadata.getColumnTypeName(i) + "<p>");
 		}
 		out.println();
 		out.println("</BODY></HTML>");
 	}
 	
 	public void getMetadata(Connection dbcon, PrintWriter out) throws SQLException {
 		// Get database metadata
 		DatabaseMetaData md = dbcon.getMetaData();
 		
 		// Get table names
 		ResultSet tables = md.getTables(null, null, "%", null);
 		
 		Statement statement = dbcon.createStatement();
 		ResultSet result;
 		
 		// Iterate over tables and get metadata for each
 		while(tables.next()) {
 			result = statement.executeQuery("SELECT * FROM " + tables.getString(3));
 			
 			// Get metadata; print metadata for table
 			printTableMetadata(tables.getString(3), result, out);
 		}
 	} 

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Output stream to STDOUT
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
//            Statement statement = dbcon.createStatement();
            
            String action = request.getParameter("action");
            
            // Check to see if Add Star submit was pressed
            if("Add Star".equals(action)) {
            	String starFirst = "";
            	String starLast = request.getParameter("star").trim();
            	String dateOfBirth = request.getParameter("year").trim();
            	String photoURL = request.getParameter("photourl").trim();
            	
            	if(starLast.split(" ").length > 1) {
            		String[] name =  starLast.trim().split(" ");
            		starFirst = name[0];
            		starLast = name[1];
            	}
                // Perform the insertion
            	PreparedStatement ps = dbcon.prepareStatement("INSERT INTO stars (first_name, last_name, dob, photo_url) "
                										   + "VALUES (?, ?, DATE?, ?)");
            	ps.setString(1, starFirst);
            	ps.setString(2, starLast);
            	ps.setString(3, dateOfBirth);
            	ps.setString(4, photoURL);
            	int ins = ps.executeUpdate();
//                int ins = statement.executeUpdate(String.format("INSERT INTO stars (first_name, last_name, dob, photo_url) "
//                											  + "VALUES ('%s', '%s', DATE'%s', '%s')", starFirst, starLast, dateOfBirth, photoURL));
                
                out.println("<HTML>" + 
        					"<HEAD><TITLE>" + 
        					"Star Insert Confirmation" + 
        					"</TITLE></HEAD>\n<BODY>" + 
        					"<P>Star Inserted: " +
        					Integer.toString(ins) + " lines changed.</P></BODY></HTML>");

            } else if("Add Movie".equals(action)){ // Add Movie submit was pressed
            	String addMovie = "{call add_movie (?, ?, ?, ?, ?, ?)}";
            	CallableStatement cstmt	= dbcon.prepareCall(addMovie);
            	
            	String movieTitle = request.getParameter("movietitle");
            	String year = request.getParameter("year");
            	String director = request.getParameter("director");
            	String starFirst = "";
            	String starLast = request.getParameter("star");
            	String genre = request.getParameter("genre");
            	
            	if(movieTitle.trim().equals(""))
            		movieTitle = " ";
            	if(director.trim().equals(""))
            		director = " ";
            	if(starLast.trim().split(" ").length > 1) {
            		String[] name =  starLast.trim().split(" ");
            		starFirst = name[0];
            		starLast = name[1];
            	}
            	if(!validYear(year)) {
            		response.sendRedirect(request.getContextPath() + "/addMovie.jsp?invalid=invalid");
            		return;
            	}
            	
            	cstmt.setString(1, movieTitle);
            	cstmt.setInt(2, Integer.parseInt(year));
            	cstmt.setString(3, director);
            	cstmt.setString(4, starFirst);
            	cstmt.setString(5, starLast);
            	cstmt.setString(6, genre);
            	
            	ArrayList<String> statusMsgs = new ArrayList<String>();
            	boolean results = cstmt.execute();
            	while(results) {
            		ResultSet rs = cstmt.getResultSet();
            		while(rs.next()) {
            			statusMsgs.add(rs.getString(1));
            		}
            		results = cstmt.getMoreResults();
            		if(!results)
            			rs.close();
            	}
            	request.setAttribute("statusMsgs", statusMsgs);
            	cstmt.close();
            	dbcon.close();
            	
            	request.getRequestDispatcher("/employeeConfirmation.jsp").forward(request, response);
            	
            } else if("metadata".equals(action)) { // Print out metadata
            	getMetadata(dbcon, out);
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
