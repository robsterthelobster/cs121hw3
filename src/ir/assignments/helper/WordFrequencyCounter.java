// Valentin Yang #30062256
// Credits to an answer to a StackOverflow post from ScArcher2 on how to iterate and retrieve key/value pairs in a map

package ir.assignments.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * @param <E>
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static <E> List<Frequency> computeWordFrequencies(List<String> words) {
		// Create arrayList to store list of word frequencies
		ArrayList<Frequency> listOfFrequencies = new ArrayList<Frequency>();
		
		// Use a map to keep track of words and their frequencies
		Map<String, Integer> map = new HashMap<String, Integer>();		
		
		// This string will keep track of the current word in the list
		String currentWord;
		
		// If the input list is null, an empty list is returned.
		if(words == null){
			return listOfFrequencies;
		}
	
		// For all the words in the list of words
		for(int i = 0; i < words.size(); ++i){
			// Receive a word from the list of words
			currentWord = words.get(i);
			
			// If the current word is not already on the list of frequencies, add to the list
			if(map.containsKey(currentWord)){
				map.put(currentWord, map.get(currentWord) + 1);
			}
			// Otherwise, if the current word is in the list of frequencies, increment its count
			else{
				map.put(currentWord, 1);
			}
		}
		
		
		// Transfer the words and their frequencies onto a List
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			listOfFrequencies.add(new Frequency(entry.getKey(), entry.getValue()));
		}
		
		return listOfFrequencies;
	}
	
	/**
	 * Runs the word frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		File file = new File(args[0]);
		List<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computeWordFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
