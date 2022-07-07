package application;

public class pritntable implements Comparable<pritntable>{
	
	 String word;
	    Double prob;
	    
	    @Override
	    public int compareTo(pritntable temp) {
	        if(this.prob > temp.prob)
	            return 1;
	        else if(this.prob< temp.prob)
	            return -1;
	        else
	            return 0;
	    }

	    public pritntable(String word, Double prob) {
	        this.word = word;
	        this.prob = prob;
	    }

	    public String getWord() {
	        return word;
	    }

	    public void setWord(String word) {
	        this.word = word;
	    }

	    public Double getProbability() {
	        return prob;
	    }

	    public void setProbability(Double prob) {
	        this.prob = prob;
	    }

	    @Override
	    public String toString() {
	        return
	                "word='" + word + '\'' +
	                ", probability=" + prob +
	                '}';
	    }

	   

}
