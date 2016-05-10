package com.netinstructions.crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.List;

public class Spider{
	// Fields
	private static final int MAX_PAGES_TO_SEARCH = 2;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();
	
	/**
	   * Our main launching point for the Spider's functionality. Internally it creates spider legs
	   * that make an HTTP request and parse the response (the web page).
	   * 
	   * @param url
	   *            - The starting point of the spider
	   * @param searchWord
	   *            - The word or string that you are searching for
	   */
	public void search(String url, String searchWord) {
		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			String currentUrl;
			SpiderLeg leg = new SpiderLeg();
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();			
			}
			
			leg.crawl(currentUrl); //Lots of stuff happening here.
			                       //Look at the crawl method in SpiderLeg
			boolean success = leg.searchForWord(searchWord);
			if (success) {
				System.out.println(String.format("**Success** Word %s found at %s", 
						           searchWord, currentUrl));
				break;
			}
			
			this.pagesToVisit.addAll(leg.getLinks());		
		}
		
		System.out.println(String.format("**Done** Visited %s web pages(s)", this.pagesVisited.size()));		
	}	
	
	/**
	   * Similar to search function. Instead of testing whether searchWord exists 
	   * this function returns the occurrence of the searchWord.
	   * 
	   * @param url
	   *            - The starting point of the spider
	   * @param searchWord
	   *            - The word or string that you are searching for
	   */
	public void totalNumber(String url, String searchWord) {
		int totalOccurrence = 0;
		
		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			String currentUrl;
			SpiderLeg leg = new SpiderLeg();
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();			
			}
			
			leg.crawl(currentUrl); //Lots of stuff happening here.
			                       //Look at the crawl method in SpiderLeg
			boolean success = leg.searchForWord(searchWord);
			if (success) {
				int currentOccurrence = leg.countWord(searchWord);
				totalOccurrence += currentOccurrence;
				System.out.println(String.format("**Success** %s Word %s found at %s", 
						           currentOccurrence, searchWord, currentUrl));			
			}
			
			this.pagesToVisit.addAll(leg.getLinks());		
		}
		
		System.out.println(String.format("**Done** Visited %s web pages(s)", this.pagesVisited.size()));
		System.out.println(String.format("Total %s Word %s found at %s", totalOccurrence, searchWord, url));
	}	
	
	/**
	   * Returns the next URL to visit (in the order that they were found). We also do a check to make
	   * sure this method doesn't return a URL that has already been visited.
	   * 
	   * @return
	   */
	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = this.pagesToVisit.remove(0);
		} while (this.pagesVisited.contains(nextUrl));
		
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
}