package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.awt.Checkbox;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/*Author: Jarett Wright
 * Class: Software Development 1
 * Due Date: 12/10/2020
 * Description: This program takes a .txt file of Edgar Allen Poe's "The Raven", and scans the contents to determine
 * the most used words and the frequency at which they occur. The results are printed with the rank in descending order.
 * 
 */

/**
 * 
 * @author Jarett Wright
 * Main Class file
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		// Begin GUI
		/**
		 * Try and catch block to start gui
		 * then connect to database, read file and store results
		 */
		try {
			GridPane layout = new GridPane(); //GUI Layout type
			//Labels to display Text
			Label text1 = new Label("Rank        Word             Number of Instances");
			layout.setConstraints(text1, 1, 1);
			Label text2 = new Label("====     ====        ===================");
			layout.setConstraints(text2, 1, 2);
			Label rank1 = new Label("1)            the                                  56");
			layout.setConstraints(rank1, 1, 3);
			Label rank2 = new Label("2)            and                                 30");
			layout.setConstraints(rank2, 1, 4);
			Label rank3 = new Label("3)            I                                      27");
			layout.setConstraints(rank3, 1, 5);
			Label rank4 = new Label("4)            my                                  24");
			layout.setConstraints(rank4, 1, 6);
			Label rank5 = new Label("5)            of                                    21");
			layout.setConstraints(rank5, 1, 7);
			
			//Set Default Scene
			layout.getChildren().addAll(text1,text2,rank1,rank2,rank3,rank4,rank5);
			Scene scene = new Scene(layout,400,200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Expected Text Analyzer Results:");
			primaryStage.setScene(scene);
			primaryStage.show();
			

			

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		launch(args);
		// Launch Database
		Connection c = null;
	    Statement stmt = null;
	    //Try and Catch Block for opening database and creating table
	    try {
	    	  Class.forName("org.sqlite.JDBC");
	    	  c = DriverManager.getConnection("jdbc:sqlite:test1.db");
	    	  System.out.println("Opened database successfully.");
	    	  
	    	  stmt = c.createStatement();
	    	  
	    	  // Create Table Person
	    	  String sql = "CREATE TABLE IF NOT EXISTS word"
						+ " (wordId interger PRIMARY KEY NOT NULL, "
						+ " wordText string NOT NULL,"
						+ " occurence interger NOT NULL"
						+");";
	    	  stmt.executeUpdate(sql);
	    	  System.out.println("Word Table Created.");
	    	
	    }catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);}
		
		    
		
		//System.out.println("before scanner");
		
		
		
		/**
		 * Open scanner in try/catch block
		 * Read file TheRaven.txt
		 */
		// Open txt file and scanner
		Scanner scan = null;
		try {
			scan = new Scanner(new File("C:/Module 2 work/TheRaven.txt"));
	    } catch (FileNotFoundException ex) {
		       ex.printStackTrace();  
		}
        
		//System.out.println("after scanner");
		/**
		 * Initialize Hashmap ravenMap for storing input data
		 * Add words not yet present with count value of 1
		 * If word is already listed, delete and replace then increase the count value for that word
		 */
		
		// Hashmap to store words and instances
        Map<String,Integer> ravenMap = new HashMap<String, Integer>(); 
        while (scan.hasNext())
        {
            String word = scan.next(); 
            if(ravenMap.containsKey(word) == false) // If word doesn't exist in map add it with value of 1
            	ravenMap.put(word,1);
            else // Locate word, remove and replace it, then ++ the count
            {
                int count = (int)(ravenMap.get(word));
                ravenMap.remove(word);  
                ravenMap.put(word,count+1); 
            }
        }
        
        // Collect Entries
        Set<Map.Entry<String, Integer>> set = ravenMap.entrySet(); 
        
        /**
         * Create Arraylist NEW wordList
         * Sort wordList by descending count values
         * 
         */
        
        // Create and Sort arraylist
        List<Map.Entry<String, Integer>> wordList = new ArrayList<Map.Entry<String, Integer>>(set);
        Collections.sort( wordList, new Comparator<Map.Entry<String, Integer>>() 
        {
        	// Sort by descending values
            public int compare( Map.Entry<String, Integer> a, Map.Entry<String, Integer> b ) 
            {
                return (b.getValue()).compareTo( a.getValue() ); 
            }
        } );
        
        /**
         * For loop to sort through wordList data
         * Display Results in console
         */
        
        // Output Results
        int numCount = 0;
        Words word = new Words(0, "", 0);
        
//        System.out.println("\nRank\tWord\t\tNumber of Instances");
//        System.out.println("====\t====\t\t===================");
        
        for(Map.Entry<String, Integer> i:wordList){
        	//System.out.println(numCount + ")\t" + i.getKey() + "\t\t\t" + i.getValue());
        	numCount++;
        	word = new Words (numCount, i.getKey(), i.getValue());
        	insertWord(word);
        }
        
        
        
        // Output Database Results  
        String sqlGetInfo = "SELECT wordId, wordText, occurence FROM Word ORDER BY occurence DESC LIMIT 20;";
		ResultSet rs = stmt.executeQuery(sqlGetInfo);
		// loop through results
		System.out.println("\nRank\tWord\t\tNumber of Instances");
        System.out.println("====\t====\t\t===================");
		while(rs.next()) {
			System.out.println(rs.getInt("wordId") + "\t" +
							   rs.getString("wordText") + "\t\t\t" +
							   rs.getInt("occurence")
							   );
		}
        	

	    
        
       
        /**
         * End of program
         */
	

	

	}
	
	



//Insert Person Object Into DB Method
public static void insertWord(Words word) throws ClassNotFoundException {
	Class.forName("org.sqlite.JDBC");
	Connection c2 = null;
	 try { c2 = DriverManager.getConnection("jdbc:sqlite:test1.db");
	String sqlInsert = "INSERT INTO word(wordId, wordText, occurence) VALUES(?,?,?)";
	PreparedStatement pstmt = c2.prepareStatement(sqlInsert);
	pstmt.setInt(1, word.getWordID());
	pstmt.setString(2, word.getWordText());
	pstmt.setInt(3, word.getOccurence());

	pstmt.executeUpdate();
	pstmt.close();
	c2.close();
	 }catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		
		    }
	
}}



