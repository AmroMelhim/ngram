package application;

import java.util.HashMap;

public class ngramTable {
	  HashMap<String,row> last;

	    public ngramTable(HashMap<String, row> last) {
	        this.last = last;
	    }

	    public HashMap<String, row> getLastWord() {
	        return last;
	    }

	  

}
