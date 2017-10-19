

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class AddCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddCart() {
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
		
	    try {
        	String movie = null;
        	Cart cart;
	        if(request.getParameter("addToCart") != null) {
	        	movie = request.getParameter("addToCart");
	        	
	        	// Check to see if Cart attribute exists
	        	if(request.getSession().getAttribute("cart") != null)
	        		cart = (Cart) request.getSession().getAttribute("cart");
	        	else
	        		cart = new Cart();
	        	
        		cart.addToCart(Integer.parseInt(movie));
        		request.getSession().setAttribute("cart", cart);
	        	
	        } else if(request.getParameter("removeFromCart") != null) {
	        	movie = request.getParameter("removeFromCart");
	        	if(request.getSession().getAttribute("cart") != null) {
	        		cart = (Cart) request.getSession().getAttribute("cart");
	        		cart.removeFromCart(Integer.parseInt(movie));
	        		request.getSession().setAttribute("cart", cart);
	        	}
	        	request.getRequestDispatcher("/servlet/Checkout?checkout=true").forward(request, response);
	        	return;
	        }
	        // Called from Single Movie Page
	        if(request.getParameter("mp") != null) {
	        	String url = String.format("/servlet/MoviePage?movie=%s", movie);
	        	request.getRequestDispatcher(url).forward(request, response);
	        } else {
	        	// Set attribute for rowCount to be forwarded back to Result
	        	String rowCount = request.getParameter("rowCount");
	        	request.setAttribute("rowCount", rowCount);
	        	request.getRequestDispatcher("/servlet/Result").forward(request, response);
	        }
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
