//@@author a0124946

package parser;

import org.joda.time.format.DateTimeFormat;

public class EventParser {
	private static final String SYMBOL_WHITESPACE = " ";
	private static final String SYMBOL_EMPTY = "";
	private static final int COMMAND_WORD_INDEX = 0;
	private static final int ARRAY_MINIMUM_LENGTH = 2; 
	
	/**
	 * Parses the event phrase from a user Input
	 * @param userInput
	 * @return event phrase, with date, time and command word removed.
	 */
	public static String parseEvent(String userInput){
		String inputEvent = removeCommandWord(userInput);
		String resultingEvent = removeDateTime(inputEvent);
		return resultingEvent.trim();
	}
	
	private static String removeCommandWord(String userInput){
		String[] inputArray = userInput.split(SYMBOL_WHITESPACE);
		String remainingText = SYMBOL_EMPTY;
		if(inputArray.length >= ARRAY_MINIMUM_LENGTH) {
			for(int i = 0; i < inputArray.length; i++) {
				if (i != COMMAND_WORD_INDEX){
					remainingText += inputArray[i];
				} 
				
				if(i != inputArray.length) {
					remainingText += SYMBOL_WHITESPACE;
				}
			}
		}
		return remainingText;
	}
	
	private static String removeDateTime(String userInput){
		String extractedEvent = SYMBOL_EMPTY;
		String[] inputArray = userInput.split(SYMBOL_WHITESPACE);
		
		for(int i = 0 ; i <inputArray.length ; i++){
			if(isNotDate(inputArray[i]) && isNotTime(inputArray[i])){
				extractedEvent += inputArray[i];
				extractedEvent += SYMBOL_WHITESPACE;
			} 
		}
		return extractedEvent;
	}
	
	private static boolean isNotDate(String testString){
		boolean noDate = true;
		for(String eachKeyword : KeywordConstraints.KW_FORMAT_DATE_WITH_YEAR){
			try{
				DateTimeFormat.forPattern(eachKeyword).parseLocalTime(testString);
				return false;
			} catch (IllegalArgumentException e){
			
			}
		}
		
		for(String eachKeyword : KeywordConstraints.KW_FORMAT_DATE_WITHOUT_YEAR){
			try{
				DateTimeFormat.forPattern(eachKeyword).parseLocalTime(testString);
				return false;
			} catch (IllegalArgumentException e){
			
			}
		}
		return noDate;
	}
	
	private static boolean isNotTime(String testString){
		boolean noTime = true;
		for(String eachKeyword : KeywordConstraints.KW_FORMAT_TIME){
			try{
				DateTimeFormat.forPattern(eachKeyword).parseLocalTime(testString);
				return false;
			} catch (IllegalArgumentException e){
			
			}
		}
		return noTime;
	}
	
}