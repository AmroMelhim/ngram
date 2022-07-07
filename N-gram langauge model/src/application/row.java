package application;

public class row {
	
	  int count;
	    double probability;
	    int tokenNum;


	    public row(int count,double probability, int tokenNum) {
	        this.count = count;
	        this.probability = probability;
	        this.tokenNum = tokenNum;
	    }


	    public int getCount() {
	        return count;
	    }

	    public void setCount(int count) {
	        this.count = count;
	    }

	    public double getProbability() {
	        return probability;
	    }

	    public void setProbability(double probability) {
	        this.probability = probability;
	    }

	    public int getDirection() {
	        return tokenNum;
	    }

	    public void setDirection(int tokenNum) {
	        this.tokenNum = tokenNum;
	    }

	    @Override
	    public String toString() {
	        return
	                ", count=" + count +
	                        ", probability=" + probability +
	                        ", Number of Tokens="+tokenNum+
	                        '}';
	    }

}
