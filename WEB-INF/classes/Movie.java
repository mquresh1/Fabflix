import java.util.ArrayList;
import java.util.HashMap;

public class Movie {
	private int ID;
	private String title;
	private int year;
	private String director;
	private String bannerURL;
	private String trailerURL;
	private HashMap<Integer, String> stars;
	private ArrayList<String> genres;
	
	public Movie() {
		stars = new HashMap<Integer, String>();
		genres = new ArrayList<String>();
	}
	
	// Setters
	public void setID(int ID) {this.ID = ID;}
	
	public void setTitle(String title) {this.title = title;}
	
	public void setYear(int year) {this.year = year;}
	
	public void setDirector(String director) {this.director = director;}
	
	public void setBannerURL(String bannerURL) {this.bannerURL = bannerURL;}
	
	public void setTrailerURL(String trailerURL) {this.trailerURL = trailerURL;}
	
	public void setStars(HashMap<Integer, String> stars) {this.stars = stars;}
	
	public void setGenres(ArrayList<String> genres) {this.genres = genres;}
	
	// Getters
	public int getID() {return ID;}
	
	public String getTitle() {return title;}
	
	public int getYear() {return year;}
	
	public String getDirector() {return director;}
	
	public String getBannerURL() {return bannerURL;}
	
	public String getTrailerURL() {return trailerURL;}
	
	public HashMap<Integer, String> getStars() {return stars;}
	
	public ArrayList<String> getGenres() {return genres;}
	

}
