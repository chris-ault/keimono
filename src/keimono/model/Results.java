package keimono.model;

import java.util.ArrayList;

//Results should be an array of relevant sites
//filled with relevance information
public class Results {
	
	private String title;
	private String baseURL;
	private String fullURL;
	private int wordCount;
	private int hitCount;
	private String fileName;
	
	public void addSite(String filename,String title){
		siteResults.add(site("test","test","test","test",1,2))
		//site.title = title;
	}
	
	public keimono.model.site site(String fi, String t, String b, String fu, int w, int h){
		fileName = fi;
		title = t;
		baseURL = b;
		fullURL = fu;
		wordCount = w;
		hitCount = h;
		return site(null, null, null, null, 0, 0);
	}
	
}

