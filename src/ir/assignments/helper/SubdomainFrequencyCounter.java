// Robin Chen    #95812659
// Valentin Yang #30062256
// Credits to an answer to a StackOverflow post from ScArcher2 on how to iterate and retrieve key/value pairs in a map

package ir.assignments.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SubdomainFrequencyCounter {

	private SubdomainFrequencyCounter() {}
	
	/**
	 * @param words A list of words.
	 * @return A list of subdomain frequencies, ordered by decreasing frequency.
	 */
	public static <E> List<Frequency> computeSubdomainFrequencies(List<String> words) {
		// Create arrayList to store list of subdomain frequencies
		ArrayList<Frequency> listOfSubdomains = new ArrayList<Frequency>();
		
		// Use a map to keep track of subdomains and their frequencies
		Map<String, Integer> map = new HashMap<String, Integer>();		
		
		// This string will keep track of the current word in the list
		String currentSubdomain;
		
		// If the input list is null, an empty list is returned.
		if(words == null){
			return listOfSubdomains;
		}
	
		// For all the words in the list of words
		for(int i = 0; i < words.size(); ++i){
			// Receive a word from the list of words
			currentSubdomain = words.get(i);
			
			// If the current word is not already on the list of frequencies, add to the list
			if(map.containsKey(currentSubdomain)){
				map.put(currentSubdomain, map.get(currentSubdomain) + 1);
			}
			// Otherwise, if the current word is in the list of frequencies, increment its count
			else{
				map.put(currentSubdomain, 1);
			}
		}
		
		
		// Transfer the words and their frequencies onto a List
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			listOfSubdomains.add(new Frequency(entry.getKey(), entry.getValue()));
		}
		
		return listOfSubdomains;
	}
	
	/**
	 * Runs the subdomain frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		File file = new File(args[0]);
		List<String> words = SubdomainUtilities.tokenizeFile(file);
		List<Frequency> frequencies = computeSubdomainFrequencies(words);
		SubdomainUtilities.printFrequencies(frequencies);
	}
}
