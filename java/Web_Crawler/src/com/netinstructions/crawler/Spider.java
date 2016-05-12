package com.netinstructions.crawler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Comparator;

public class Spider{
	// Fields
	private static final int MAX_PAGES_TO_SEARCH = 1;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();
	private Map<String, Integer> dictionary = new HashMap<String, Integer>();
	
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
	   * Count all words density and store results in the dictionary 
	   * 
	   * @param url
	   *            - The starting point of the spider
	   */
	public void wordDensityAnalyze(String url) {		
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
			leg.countKeyWords(this.dictionary);			
			System.out.println(String.format("Words counted at %s", currentUrl));
			this.pagesToVisit.addAll(leg.getLinks());		
		}
		
		System.out.println(String.format("**Done** Visited %s web pages(s)", this.pagesVisited.size()));
	}	
	
	/**
	   * print the content of the dictionary sorted by word value in dictionary map.
	   * number of printed word can be decided by user.
	   * 
	   * @param topKWord
	   *            - First K words user are interested in
	   */
	
	public void printDictionary(int topKWord) {
		Map<String, Integer> sortedMap = sortByComparator(dictionary);
		for (Map.Entry<String, Integer> entry: sortedMap.entrySet()) {
			if (topKWord == 0) break;
			System.out.println("[Key] : " + entry.getKey()
			                              + " [Value] : " + entry.getValue());
			topKWord--;
		}	
	}
	
	/**
	 * sort a unsorted map by value
	 * return a sorted map
	 * 
	 * @param unsortMap
	 * @return
	 */
	private Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {
		//Convert Map to List
		List<Map.Entry<String, Integer>> list = 
				new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
		
		//Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}			
		});
		
		//Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedMap;	
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