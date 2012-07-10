package org.zengine.uils;

/**
 * This class provides various methods for string manipulation.
 * 
 * @author Kim
 *
 */
public class StringUtils {
	
	/**
	 * Removes all whitespace characters such as spaces, tabs etc.
	 */
	
	public static String removeWhitespace(String text) {
		return text.replaceAll("\\s", "");
	}
	
	/**
	 * Removes all digit characters from a string.
	 */
	
	public static String removeDigits(String text) {
		return text.replaceAll("\\d", "");
	}
	
	/**
	 * This method act as a backspace would. When called, it returns
	 * the string with it's last character removed.
	 */
	
	public static String backspace(String text) {
		
		if(text == null)
			return null;
		else if(text.length() < 2)
			return "";
		else
			return text.substring(0, text.length()-1);	
	}
	
	

}
