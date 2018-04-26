package keimono.model;

import java.util.ArrayList;

public class Article {

	private String title;
	private String url;
	private String filename;
	private ArrayList<String> keywords;
	
	public Article(String t, String u, String f) {
		this.title = t;
		this.url = u;
		this.filename = f;
		keywords = new ArrayList<String>();
	}
	
	public void addKeywordHit(String keyword) {
		
		keywords.add(keyword);
		
	}
	
}
