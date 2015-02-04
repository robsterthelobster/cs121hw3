// Robin Chen    #95812659
// Valentin Yang #30062256

// Credits to an answer to a post on StackOverflow from the user Penelope the Duck about setting up a string tokenizer.
// Link: http://stackoverflow.com/questions/13432094/how-to-use-string-tokenizer-when-reading-in-from-a-file
// Credits to answers to a post on StackOverflow from Amber and Omega about spliting on non-alphanumeric characters
// Link: http://stackoverflow.com/questions/11332772/java-string-split-on-all-non-alphanumeric-except-apostrophes
// Credits to Kyle Copeland and his comment in a Piazza post about using Collections.sort to sort arrayLists

package ir.assignments.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 * A collection of utility methods for text processing.
 */
public class SubdomainUtilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		String lineOfText;
		
		ArrayList<String> listOfSubdomains = new ArrayList<String>();
		
		try {
			// Create a file reader & buffered reader
			FileReader fileReader = new FileReader(input);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			// Read input file line by line until the reader reaches EOF
			while((lineOfText = bufferedReader.readLine()) != null){
				
				listOfSubdomains.add(lineOfText);

				// Normalize the characters within the line of text to lower case.
				lineOfText = lineOfText.toLowerCase();
				
//				for(String s : listOfSubdomains){
//					System.out.println(s);
//				}
			}
			
			// Close buffer reader & file reader
			bufferedReader.close();
			fileReader.close();			
			
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		return listOfSubdomains;
	}
	
	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		// Keep track of total(sum of all word frequencies in the list) and unique counts (size of list) 
		int totalCount = 0;
		int uniqueCount = frequencies.size();
		
		for(int i = 0; i < frequencies.size(); ++i){
			totalCount += frequencies.get(i).getFrequency();
		}
		
		// Print out titles depending whether if input list is 1) word frequencies OR 2) 2-gram frequencies.
		// Two-grams contain two words separated with a space (" ")
		if(frequencies.get(0).getText().contains(" ")){
			System.out.println("Total 2-gram count: " + totalCount);
			System.out.println("Unique 2-gram count: " + uniqueCount);
		}
		else {
			System.out.println("Total item count: " + totalCount);
			System.out.println("Unique item count: " + uniqueCount);
		}
		
		System.out.println("");
		
		// Sort the frequencies in decreasing order of frequencies & alphabetically
		Collections.sort(frequencies, Collections.reverseOrder(new FrequencyComparator()));
		
		// Print out all texts and their frequencies
		for(int i = 0; i < frequencies.size(); ++i){
//			String text = frequencies.get(i).getText();
//			int textFrequency = frequencies.get(i).getFrequency();
//			System.out.println(text + ": " + textFrequency);
			System.out.println(frequencies.get(i).toString());
		}
	}
}
