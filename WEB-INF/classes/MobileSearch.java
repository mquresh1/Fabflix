

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONObject;
import org.json.JSONArray;

public class MobileSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Receive String information from Android
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    }    
	    String title = sb.toString();
	    
	    String loginUser = "root";
        String loginPasswd = "alexmojulian";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        try {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
//            Statement statement = dbcon.createStatement();
            
            // Do fulltext search of movies and create JSONArray from ResultSet
            String[] words = title.split(" ");
    		for(int i = 0; i < words.length; i++) {
    			if(i == words.length - 1) {
    				words[i] = "+" + words[i] + "*";
    			} else {
    				words[i] = "+" + words[i];
    			}
    		}
    		
    		String search = String.join(" ", words);
    		String query = "SELECT title FROM movies "
					 + "WHERE MATCH(title) AGAINST(? IN BOOLEAN MODE) LIMIT 10";
    		PreparedStatement ps = dbcon.prepareStatement(query);
    		ps.setString(1, search);
    		ResultSet rs = ps.executeQuery();
    		
    		JSONArray js = new JSONArray();
    		if(!rs.isBeforeFirst())
    			js.put("NO RESULTS FOUND");
    		else {
	    		while(rs.next()) {
	    			js.put(rs.getString(1));
	    		}
    		}
    		String json = js.toString();
    		PrintWriter out = response.getWriter();
    		out.write(json);

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
