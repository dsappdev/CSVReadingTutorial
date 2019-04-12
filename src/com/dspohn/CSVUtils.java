                        
package com.dspohn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * I have the general idea here but I need to refine it more.
 * Need to add a clear expectation of what the output should
 * look like.
 * 
 * 
 * @author Doug
 */
public class CSVUtils {
	
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    public static void main(String[] args) throws Exception {
        String csvFile = "src/country2.csv";
//        String csvFile = "src/CSVCountry.csv";
//        String csvFile = "src/country.csv";
        Scanner scanner = new Scanner(new File(csvFile));
        
        while(scanner.hasNext()) {            
            List<String> line = parseLine(scanner.nextLine());
            System.out.println("Country [id= " + line.get(0) + 
                    ", code= " + line.get(1) + 
                    " , name=" + line.get(2) + 
                    "]");
        }
            scanner.close();
	}
	
    public static List<String> parseLine(String csvLine) {
        return parseLine(csvLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }
	
    public static List<String> parseLine(String csvLine, char separators) {
        return parseLine(csvLine, separators, DEFAULT_QUOTE);
    }
	
    public static List<String> parseLine(String csvLine, 
            char separators, char customQuote) {
        List<String> result = new ArrayList<String>();
		
        if(csvLine == null && csvLine.isEmpty()) {
            return result;
        }
		
        if(customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }
		
        if(separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }
		
        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;
		
        char[] chars = csvLine.toCharArray();
		
        for(char ch : chars) {
            // If inQutes is true then it will begin adding 
            if(inQuotes) {
                startCollectChar = true;
                // When it finds the last double quote, it assumes it is done.
                if(ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                    // Here is where I needed to add the double quote for 
                    // when the string has has a double quoted added which 
                    // was not there originally.
                } else {
                    if(ch == '\"') {
                        if(!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }
                }
            } else {
                // When the opening double quote is found
                if (ch == customQuote) {                    
                    inQuotes = true;
                    // As long as the first character is not a double quote and 
                    // ...wait, why are they checking if customQuote == '\"'? 
                    // We wouldn't be here if it wasn't, right?
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }
                    // If startCollectChar is true then add a double quote.
                    if (startCollectChar) {
                        curVal.append('"');
                    }
                } else if (ch == separators) {
                    result.add(curVal.toString());
                    curVal = new StringBuffer();
                    startCollectChar = false;
                } else if (ch == '\r') {
                    continue;
                } else if (ch == '\n') {
                    break;
                } else {
                    curVal.append(ch);
                }
            }
        }
        result.add(curVal.toString());
        return result;
	}
}