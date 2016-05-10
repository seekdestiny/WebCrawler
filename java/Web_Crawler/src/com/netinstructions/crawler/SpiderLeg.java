package com.netinstructions.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class SpiderLeg {
	// We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) "
			+ "AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = new LinkedList<String>(); //Just a list of URLs
	private Document htmlDocument; //This is our web page, or in other words, our document
	
	/**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     * 
     * @param url
     *            - The URL to visit
     * @return whether or not the crawl was successful
     */
	public boolean crawl(String url) {
	    try {
	    	Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
	    	Document htmlDocument = connection.get();
	    	this.htmlDocument = htmlDocument;
	    	
	    	if (connection.response().statusCode() == 200) { // 200 is the HTTP OK status code
	    	    System.out.println("Received web page at " + url); //indicating that everything is great.
	    	}
	    	
	    	if (!connection.response().contentType().contains("text/html")) {
	    		System.out.println("**Failure** Retrieved something other than HTML");
	    		return false;
	    	}
	    	Elements linksOnPage = htmlDocument.select("a[href]");
	    	System.out.println("Found (" + linksOnPage.size() + ") links");
	    	for (Element link: linksOnPage) {
	    		this.links.add(link.absUrl("href"));
	    	}
	    	return true;
	    } catch(IOException ioe) {
	        //We were not successful in our HTTP request
	    	System.out.println("Error in out HTTP request " + ioe);
	    	return false;
	    }		
	}	
	

    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     * 
     * @param searchWord
     *            - The word or string to look for
     * @return whether or not the word was found
     */
	public boolean searchForWord(String searchWord) {
		 // Defensive coding. This method should only be used after a successful crawl.
		if (this.htmlDocument == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
			return false;
		}
		System.out.println("Searching for the word " + searchWord + ". . .");
		String bodyText = this.htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}
	
	/**
     * Performs a target word count on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful searchForWord.
     * 
     * @param searchWord
     *            - The word or string to look for
     * @return word occurrence of searchWord on current web page
     */
	public int countWord(String searchWord) {
		String bodyText = this.htmlDocument.body().text().toLowerCase();
		int lastIndex = 0;
		int count = 0;
		while (lastIndex != -1) {
			lastIndex = bodyText.indexOf(searchWord.toLowerCase(), lastIndex);
			
			if (lastIndex != -1) {
				count++;
				lastIndex += searchWord.length();
			}		
		}
		
		return count;
	}
	
	public List<String> getLinks() {
		return this.links;
	}
}
