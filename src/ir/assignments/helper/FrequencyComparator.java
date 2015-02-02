package ir.assignments.helper;

import java.util.Comparator;

// This class is used to compare words by frequency

public class FrequencyComparator implements Comparator<Frequency>{
	public FrequencyComparator(){
		// Empty
	}
	
	public int compare(Frequency f1, Frequency f2){
		if(f1.getFrequency() > f2.getFrequency()){
			return 1;
		}
		else if(f1.getFrequency() < f2.getFrequency()){
			return -1;
		}
		// if frequencies are tied, sort words alphabetically
		else {
			return f2.getText().compareToIgnoreCase(f1.getText());
		}
	}
}