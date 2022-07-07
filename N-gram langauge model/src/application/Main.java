package application;
	
import java.io.*;
import java.util.*;
import javafx.application.Application;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.VBox;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		 AnchorPane pane = new AnchorPane();
	        pane.prefHeight(444);
	        pane.prefWidth(664);

	        Label label = new Label("ضع  : حول الكلمة المراد استبدالها");
	        label.setLayoutX(200);
	       
	        TextArea textArea = new TextArea();
	        textArea.setLayoutX(50);
	        textArea.setLayoutY(40);
	        
	        textArea.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	        textArea.setWrapText(true);


	        VBox vBox = new VBox();
	        vBox.setLayoutX(430);
	        vBox.setLayoutY(220);
	        vBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

	       
	        vBox.getChildren().add(textArea);
	        

	        Button run = new Button("استبدال الكلمة");
	        run.prefHeight(27);
	        run.prefWidth(110);
	        run.setLayoutX(50);
	        run.setLayoutY(222);
	        
	        HashMap<String,ngramTable> corpus = readFile();
	        
	        
	        pane.getChildren().addAll(label,textArea,run,vBox);
	        
	        run.setOnAction(actionEvent -> {
	            if(corpus.size() != 0) {
	                ArrayList<pritntable> temp = getTop10(textArea.getText(), corpus);
	                vBox.getChildren().clear();
	                for (int i = temp.size() - 1, j = 1; i > 0 && i > temp.size() - 11; i--, j++) {
	                    Label l = new Label(j + ": " + temp.get(i).word + " " + String.format("%,.5f", temp.get(i).prob));
	                    l.setStyle("-fx-font-weight: bold");
	                    vBox.getChildren().add(l);
	                }
	            }
	           
	        });
	        
	        
	        Scene scene = new Scene(pane, 664, 444);
	        
	        primaryStage.setScene(scene);
	        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
		
	   }
	
	
	 public HashMap<String,ngramTable> readFile(){
	        HashMap<String,ngramTable> corpus = new HashMap<>();

	        FileChooser fileChooser = new FileChooser();
	        Stage fileStage = new Stage();
	        // Set extension filter
	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
	        fileChooser.getExtensionFilters().add(extFilter);

	        // Show open file dialog
	        File file = fileChooser.showOpenDialog(fileStage);
	        if (file != null) {


	        try (BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
	            String line;
	            line = br.readLine();
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split(",");
	                if (Integer.parseInt(values[3].trim()) > 1) {
	                    String[] tempArray =  values[0].trim().split("\\s");
	                    String firstWords = "";
	                    String lastWord = tempArray[tempArray.length-1].trim();
	                    for (int i = 0; i < tempArray.length-1; i++) {
	                        firstWords+= tempArray[i]+" ";
	                    }
	                    firstWords = firstWords.trim();

	                    if(corpus.containsKey(firstWords)){
	                        corpus.get(firstWords).getLastWord().put(lastWord, new row(Integer.parseInt(values[1].trim()), Double.parseDouble(values[2].trim()), Integer.parseInt(values[3].trim())));
	                    }
	                    else{
	                        HashMap<String,row> tempHash = new HashMap<>();
	                        tempHash.put(lastWord,new row(Integer.parseInt(values[1].trim()),Double.parseDouble(values[2].trim()),Integer.parseInt(values[3].trim())));
	                        corpus.put(firstWords,new ngramTable(tempHash));
	                    }
	                }
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        }
	        return corpus;

	    }
	 
	 public ArrayList<pritntable> getTop10(String text,HashMap<String,ngramTable> corpus){
	        ArrayList<pritntable> top10 = new ArrayList<>();
	     
	       String[] temp = text.split("\\s");
	        String s="";
	        for(int i=0;i<temp.length;i++)
	        {
	        	if(temp[i].charAt(0) != ':')
	        	{
	        		s=s+" "+temp[i];
	        	}
	        }
	     
	        
	       
	        String[] sentence = s.split("\\s");

	      	      


	        HashMap<String,row> biGram = new HashMap<>();


	        HashMap<String,row> triGram = new HashMap<>();



	        if(sentence.length >=1){
	            double biGramProbability = 1.0;
	            double triGramProbability = 1.0;
	            for (int i = 0; i < 3; i++) {
	            	 
	                if(i+2 < sentence.length){
	                	String first2Words = sentence[i]+" "+sentence[i+1];
	                    if(corpus.containsKey(first2Words)){
	                        if(corpus.get(first2Words).getLastWord().containsKey(sentence[i+2])){
	                            triGramProbability*= corpus.get(first2Words).getLastWord().get(sentence[i+2]).getProbability();
	                        }
	                    }
	                }
	                if(i+1 < sentence.length){
	                    if(corpus.containsKey(sentence[i])){
	                        if(corpus.get(sentence[i]).getLastWord().containsKey(sentence[i+1])){
	                            biGramProbability*= corpus.get(sentence[i]).getLastWord().get(sentence[i+1]).getProbability();
	                        }
	                    }
	                }
	            }
	            if(sentence.length > 1){
	                if(corpus.containsKey(sentence[sentence.length-2]+" "+sentence[sentence.length-1])){
	                    triGram = corpus.get(sentence[sentence.length-2]+" "+sentence[sentence.length-1]).getLastWord();
	                }

	            }
	            if(corpus.containsKey(sentence[sentence.length-1])){
	                biGram = corpus.get(sentence[sentence.length-1]).getLastWord();
	            }

	            for (Map.Entry<String, row> b : triGram.entrySet()) {
	                if(biGram.containsKey(b.getKey()))
	                {
	                    biGram.get(b.getKey()).setProbability(biGram.get(b.getKey()).getProbability()*triGramProbability*b.getValue().probability);
	                }
	                else {
	                top10.add(new pritntable(b.getKey(),(triGramProbability*b.getValue().probability)));
	            }

	            }

	            for (Map.Entry<String, row> b : biGram.entrySet()) {
	                top10.add(new pritntable(b.getKey(),(biGramProbability*b.getValue().probability)));
	            }

	        }
	        Collections.sort(top10);
	        return top10;
	    }

	}
	
	
