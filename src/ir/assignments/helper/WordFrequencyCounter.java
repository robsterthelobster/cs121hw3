// Robin Chen    #95812659
// Valentin Yang #30062256

package ir.assignments.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeWordFrequencies(List<String> words) {
		// TODO Write body!
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		for(String word : words){
			boolean add = true;
			for(Frequency f : frequencies){
				if(f.getText().equals(word)){
					f.incrementFrequency();
					add = false;
				}
			}
			if(add){
				Frequency f = new Frequency(word, 1);
				frequencies.add(f);
			}
		}
		return customSort(frequencies);
	}
	
	// custom sort for frequencies list
	private static List<Frequency> customSort(ArrayList<Frequency> frequencies){
		// alpha sort
		Collections.sort(frequencies, new Comparator<Frequency>(){

			@Override
			public int compare(Frequency f1, Frequency f2) {
				return f1.getText().compareTo(f2.getText());
			}
			
		});
		// size sort
		Collections.sort(frequencies, new Comparator<Frequency>(){

			@Override
			public int compare(Frequency f1, Frequency f2) {
				return f2.getFrequency() - f1.getFrequency();
			}
			
		});
		return frequencies;
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
