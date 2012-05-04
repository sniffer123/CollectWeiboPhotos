package com.pigandtiger.utility;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	
	public static final String pickByRegx(final String pattern, final String content) throws IllegalArgumentException {
		Matcher m = Pattern.compile(pattern).matcher(content);
		if (m.find()) {
			return m.group(1);
		} else {
			throw new IllegalArgumentException("Can not pick up the value by the pattern:" + pattern + ",content: " + content);
		}
	}
	
	public static final ArrayList<String> pickMutilByRegx(final String pattern, final String content) throws IllegalArgumentException {
		ArrayList<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile(pattern).matcher(content);
		if (m.find()) {
			for(int i = 1; i <= m.groupCount(); i++){
				list.add(m.group(i));
			}
		} else {
			throw new IllegalArgumentException("Can not pick up the value by the pattern:" + pattern + ",content: " + content);
		}
		
		return list;
	}
	
	public static final ArrayList<String> pickGroupByRegx(final String pattern, final String content) throws IllegalArgumentException {
		final ArrayList<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile(pattern,Pattern.MULTILINE ).matcher(content);
		while (m.find()) {
			list.add(m.group(1));
		}
		return list;
		
	}
	
	public static final ArrayList<ArrayList<String>> pickMultiGroupByRegx(final String pattern, final String content) throws IllegalArgumentException {
		final ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		Matcher m = Pattern.compile(pattern,Pattern.MULTILINE ).matcher(content);
		while (m.find()) {
			ArrayList<String> subList = new ArrayList<String>();
			for( int i = 1; i <= m.groupCount(); i++){
				subList.add(m.group(i));
			}
			list.add(subList);
		}
		return list;
		
	}
	
}
