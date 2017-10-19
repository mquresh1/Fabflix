

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.*;

import org.json.JSONArray;

public class AutoComplete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AutoComplete() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		/*String loginUser = "root";
        String loginPasswd = "alexmojulian";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        */
		PrintWriter out = response.getWriter();
        response.setContentType("application/json, charset=UTF-8");

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
            
    		String title = request.getParameter("term");
    		
    		if(title == null || title.isEmpty())
    			return;
    		
    		String[] words = title.split(" ");
    		for(int i = 0; i < words.length; i++) {
    			if(i == words.length - 1) {
    				words[i] = "+" + words[i] + "*";
    			} else {
    				words[i] = "+" + words[i];
    			}
    		}
    		
    		String search = String.join(" ", words);
//    		String query = String.format("SELECT title FROM movies "
//    								   + "WHERE MATCH(title) AGAINST('%s' IN BOOLEAN MODE) LIMIT 10", search);
    		String query = "SELECT title FROM movies "
    					 + "WHERE MATCH(title) AGAINST(? IN BOOLEAN MODE) LIMIT 10";
    		PreparedStatement ps = dbcon.prepareStatement(query);
    		ps.setString(1, search);
//    		ResultSet rs = statement.executeQuery(query);
    		ResultSet rs = ps.executeQuery();
    		
    		JSONArray js = new JSONArray();
    		while(rs.next()) {
    			js.put(rs.getString(1));
    		}
    		String json = js.toString();
    		//PrintWriter out = response.getWriter();
    		out.write(json);
           
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
