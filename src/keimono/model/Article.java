package keimono.model;

import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Article {

	
	
	private final StringProperty title;
	private StringProperty url;
	private StringProperty filename;
	private IntegerProperty hitcount;
	private ArrayList<String> keywords;
	
	public Article(String t, String u, String f) {
		this.title = new SimpleStringProperty(t);
		this.url = new SimpleStringProperty(u);
		this.filename = new SimpleStringProperty(f);
		keywords = new ArrayList<String>();
	}
	
	public void addKeywordHit(String keyword) {
		
		keywords.add(keyword);
		
	}
	
	//Called by crawlerController before sending to results page
	public void setCount(){
		this.hitcount = new SimpleIntegerProperty(keywords.size());
	}
	
	public String getTitle(){
		return title.get();
	}
	public void setTitle(String t){
		this.title.set(t);
	}
	public StringProperty titleProperty(){
		return title;
	}
	
	public String getURL(){
		return url.get();
	}
	public void setURL(String t){
		this.url.set(t);
	}
	public StringProperty urlProperty(){
		return url;
	}
	
	public String getFilename(){
		return filename.get();
	}
	public void setFilename(String t){
		this.filename.set(t);
	}
	public StringProperty filenameProperty(){
		return filename;
	}
	
	public int getHitcount(){
		return hitcount.get();
	}
	public void setHitcount(int t){
		this.hitcount.set(t);
	}
	public IntegerProperty hitcountProperty(){
		return hitcount;
	}
	
}
