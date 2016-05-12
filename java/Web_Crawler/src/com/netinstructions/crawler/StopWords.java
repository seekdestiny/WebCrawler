package com.netinstructions.crawler;

import java.util.HashSet;
import java.util.Set;

//class for handling some stop words
class StopWords {
	private static final Set<String> stopWords = new HashSet<String>() {{
		add("the"); add("of"); add("and"); add("to"); add("it");
		add("this"); add("out"); add("a"); add("&"); add("your");
		add("on"); add("by"); add("in"); add("i"); add("we");
		add("for"); add("my"); add("with"); add("you"); add("is");
		add("can"); add("or"); add("all"); add("yes"); add("no");
	}};
	
	public static boolean isStopWords(String keyWord) {
		return stopWords.contains(keyWord);
	}
	
	public static boolean isNumeric(String keyWord) {
		return keyWord.matches("-?\\d+(\\.\\d+)?");
	}
}