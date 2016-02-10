package com.cherokeelessons.com.scraper.phoenix;

import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

public class NewsRxUtils {
	
	public static String guessTitleCase(String in) {
		in = WordUtils.capitalizeFully(in)
				.trim();
		/*
		 * capitalize known specials that should always be capitalized
		 */
		String[] casingExceptions = { "LLC", "L.L.C", "II", "III", "IV", "VI", "VII", "VIII", "IX", "of", "in", "on" };
		String pattern, notProperCase;
		for (int i = 0; i < casingExceptions.length; i++) {
			notProperCase = casingExceptions[i];
			pattern = "(?<=\\b)"
					+ Pattern.quote(WordUtils.capitalizeFully(notProperCase))
					+ "(?=\\b)";
			in=in.replaceAll(pattern, notProperCase);
		}
		/*		
		 * capitalize single letters followed by "." or " "
		 */
		for (char ii = 'a'; ii <= 'z'; ii++) {
			in = in.replaceAll("(?<=\\b)"
					+ String.valueOf(ii) + "(?=\\b)", String.valueOf(ii)
					.toUpperCase() );
		}
		/*
		 * if word starts with number, make all caps
		 */
		char[] letters=in.toCharArray();
		boolean startOfWord=false;
		boolean forceCap=false;
		char letter;
		boolean isDigit;
		boolean isLetter;
		boolean isSpace;
		for (int ix=0; ix<letters.length; ix++){
			letter=letters[ix];
			isDigit=Character.isDigit(letter);
			isLetter=Character.isLetter(letter);
			isSpace=Character.isWhitespace(letter);
			//these test are order dependent!
			if (isSpace){
				//we are between words
				//reset force cap and start of flags
				forceCap=false;
				startOfWord=false;
			}
			if (!startOfWord && (!isSpace)) {
				//we are at the start of an alphanumericsymbol sequence
				startOfWord=true;
				//if digit is first letter of sequence
				//enable force upper case until end of sequence
				forceCap=isDigit;
			}
			if (forceCap && isLetter){
				letters[ix]=Character.toUpperCase(letter);
			}			
		}
		/*
		 * look for alpha right after '[' or '(' at first start of word and make cap
		 */
		char let1;
		char let2;
		char let3;
		for (int ix=0; ix<letters.length-2; ix++){
			let1=letters[ix];
			let2=letters[ix+1];
			let3=letters[ix+2];
			if (Character.isWhitespace(let1) && (let2=='['||let2=='(') && Character.isLetter(let3)) {
				letters[ix+2]=Character.toUpperCase(let3);
			}
		}
		in=String.valueOf(letters);
		/*
		 * fix up Corp, Inc missing trailing "." at end of line
		 */
		if (in.endsWith("Corp")) in+=".";
		if (in.endsWith("Inc")) in+=".";
		
		return in;
	}

}
