import java.util.HashMap;

public class Star {
	private int ID;
	private String firstName;
	private String lastName;
	private String dob;
	private String photoURL;
	private HashMap<Integer, String> movies;
	
	public Star() {
		movies = new HashMap<Integer, String>();
	}
	
	// Setters
	public void setID(int ID) {this.ID = ID;}
	
	public void setFirstName(String firstName) {this.firstName = firstName;}
	
	public void setLastName(String lastName) {this.lastName = lastName;}
	
	public void setDOB(String dob) {this.dob = dob;}
	
	public void setPhotoURL(String photoURL) {this.photoURL = photoURL;}
	
	public void setMovies(HashMap<Integer, String> movies) {this.movies = movies;}
	
	// Getters
	public int getID() {return ID;}
	
	public String getFirstName() {return firstName;}
	
	public String getLastName() {return lastName;}
	
	public String getDOB() {return dob;}
	
	public String getPhotoURL() {return photoURL;}
	
	public HashMap<Integer, String> getMovies() {return movies;}
}
