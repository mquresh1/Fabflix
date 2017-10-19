import java.util.HashMap;

public class Cart {
	private HashMap<Integer, Integer> cart;
	
	public Cart() {
		cart = new HashMap<Integer, Integer>();
	}
	
	public HashMap<Integer, Integer> getCart() {return cart;}
	
	public boolean emptyCart() {return cart.isEmpty();}
	
	public void addToCart(int movieID) {
		if(cart.get(movieID) != null) {
			cart.put(movieID, cart.get(movieID) + 1);
		} else
			cart.put(movieID, 1);
	}
	
	public void removeFromCart(int movieID) {
		if(cart.get(movieID) != null) {
			cart.remove(movieID);
		}
	}
	
	public void setMovieCount(int movieID, int count) {
		if(count <= 0)
			removeFromCart(movieID);
		else
			cart.put(movieID, count);
	}
	
	public int getTotalCount() {
		int count = 0;
		for(Integer c : cart.values())
			count += c;
		
		return count;
	}

}
