package com.netinstructions.crawler;

import java.util.Scanner;

public class SpiderTest{
	/**
     * This is our test. It creates a spider (which creates spider legs) and crawls the web.
     * 
     * @param args
     *            - not used
     */
	
	public static void main(String[] args) {
	    Spider spider = new Spider();
	    Scanner scanner = new Scanner (System.in);
	    System.out.println("Enter the url to be crawled: ");
	    String url = scanner.nextLine();
	    System.out.println("Enter the number of keywords you need: ");
	    int number = scanner.nextInt();
	    spider.wordDensityAnalyze(url);
	    spider.printDictionary(number);
	}
}