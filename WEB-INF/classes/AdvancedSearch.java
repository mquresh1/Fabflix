
/* A servlet to take advanced search input create appropriate query */

import java.io.*;
import java.sql.*;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;

public class AdvancedSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdvancedSearch() {
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

		String q;
		String select = "SELECT m.*";
		String from = "FROM movies as m";
		String where = "WHERE";
		int categoryCount = 0;

		try {
			if(!request.getParameter("movietitle").trim().isEmpty()) {
				where += String.format(" m.title LIKE '%%%s%%'", request.getParameter("movietitle"));
				categoryCount++;
			}
			if(!request.getParameter("starfirst").trim().isEmpty() || !request.getParameter("starlast").trim().isEmpty()) {
				// add from
				from += ", stars as s, stars_in_movies as sim";
				// add where
				if(categoryCount > 0)
					where += " AND";
				where += " m.id = sim.movie_id AND s.id = sim.star_id";
				if(!request.getParameter("starfirst").trim().isEmpty()) {
					where += String.format(" AND s.first_name LIKE '%%%s%%'", request.getParameter("starfirst"));
				}
				if(!request.getParameter("starlast").trim().isEmpty()) {
					where += String.format(" AND s.last_name LIKE '%%%s%%'", request.getParameter("starlast"));
				}
				categoryCount++;
			}
			if(!request.getParameter("year").trim().isEmpty()) {
				if(categoryCount > 0)
					where += " AND";
				where += String.format(" m.year = %s", request.getParameter("year"));
				categoryCount++;
			}
			if(!request.getParameter("director").trim().isEmpty()) {
				if(categoryCount > 0)
					where += " AND";
				where += String.format(" m.director LIKE '%%%s%%'", request.getParameter("director"));
				categoryCount++;
			}
			
			if(categoryCount == 0)
				q = select + " " + from;
			else
				q = select + " " + from + " " + where;
			
			request.getSession().setAttribute("query", q); 
			request.getRequestDispatcher("/servlet/Result").forward(request, response);		

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
