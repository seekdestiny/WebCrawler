package com.netinstructions.crawler;

import java.util.*;

public class Spider{
	// Fields
	private static final int MAX_PAGES_TO_SEARCH = 10;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();
	
	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = this.pagesToVisit.remove(0);
		} while (this.pagesVisited.contains(nextUrl));
		
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
	
}