package com.ihengtu.xmpp.core.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class XmppTextFilterHelper {

	/**
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String chatStringFilter(String str) throws PatternSyntaxException{     
		String PatterChinese = "[^A-Za-z0-9|!|,|.|:|;|\\*|%|；|。|，|：|！|、|@|#|\\$|\\^|~|\\&|\\(|\\)|（|）" +
				"|\\{|\\}|\\?|？|\\[|\\]|【|】|\\+|\\-|”|“|\"|\r|\n|\\s*|_|=|/|\u4e00-\u9fa5]";
		Pattern pChinese = Pattern.compile(PatterChinese);
	    Matcher   m1   =   pChinese.matcher(str);      
	    return   m1.replaceAll("");
   }
	
	public static String loginAccountStringFilter(String str) throws PatternSyntaxException{
		String PatterChinese = "[^A-Za-z0-9|_]";
		Pattern pChinese = Pattern.compile(PatterChinese);
	    Matcher   m1   =   pChinese.matcher(str);      
	    return   m1.replaceAll("").trim();
	}
}
