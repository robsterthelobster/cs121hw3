// Robin Chen    #95812659
// Valentin Yang #30062256

package ir.assignments.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommonWords {
	ArrayList<String> stopwords;
	BufferedWriter writer;
	List<Frequency> frequencies;
	
	public CommonWords(){
		stopwords = Utilities.tokenizeFile((new File("stopwords.txt")));
		System.out.println(stopwords.size());
		frequencies = new ArrayList<Frequency>();
	}
	
	public void compute(List<String> words){
		frequencies = WordFrequencyCounter.computeWordFrequencies(words);
	}
	
	public void writeToFile(){
		//Utilities.printFrequencies(frequencies);
		int count = 0;
		try {
			writer = new BufferedWriter(new FileWriter(new File("CommonWords.txt"), true));
			for(Frequency f : frequencies){
				String word = f.getText();
				System.out.println(stopwords.contains(word));
				if(!stopwords.contains(word)){
					writer.write(f.getText()+" : "+f.getFrequency() + "\n");
					count++;
				}
				if(count >= 100){
					break;
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
