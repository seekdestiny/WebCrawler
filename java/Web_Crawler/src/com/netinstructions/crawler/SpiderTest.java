package com.netinstructions.crawler;

public class SpiderTest{
	/**
     * This is our test. It creates a spider (which creates spider legs) and crawls the web.
     * 
     * @param args
     *            - not used
     */
	
	public static void main(String[] args) {
	    Spider spider = new Spider();
	    //spider.search("http://www.bestbuy.com/", "digital camera");
	    //spider.totalNumber("http://www.sears.com", "digital camera");
	    String url = "http://www.sears.com";
	    //String url = "http://www.amazon.com/Cuisinart-CPT-122-Compact-2-Slice-Toaster/dp/B009GQ034C/ref=sr11?s=kitchen&ie=UTF8&qid=1431620315&sr=1-1&keywords=toaster";
	    spider.wordDensityAnalyze(url);
	    spider.printDictionary(100);
	}
}