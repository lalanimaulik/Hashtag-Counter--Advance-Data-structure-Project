package ads_Project_package;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashMap;

public class hashtagcounter {
	
	static Heap_Methods hm = new Heap_Methods();
	
	// Hashtable for all the hashtags 
	static Hashtable<String,Fibonacci_Heap_Node> storeHeapPointers =new Hashtable<String,Fibonacci_Heap_Node>();
	static BufferedWriter bw;
	
	public static void main(String args[]){
		
		long startTime = System.nanoTime();
//		String fileName = "";
//		
//		if(args.length > 0){
//			fileName = args[0];
//		}
		
        // The name of the file to open.
        String fileName = "C:/Users/Maulik_Lalani/Downloads/testfiles/testfiles/input_1000000.txt";
        
        // This will reference one line at a time
        String line = null;
        String[] hashtag = new String[22];
        
        try {
  
        	// Code for reading from the input file
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
			
            // Code for writing to the output file. 
            File file = new File("output_file.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			// read line by line from input file and break the line into hashtag and frequency.
            while((line = bufferedReader.readLine()) != null) {
            	
            	hashtag = new String[22];
            	hashtag = line.split(" ");
            	
            	//If hashtag and frequency 
            	if(hashtag.length==2){
            		insertHashtag(hashtag[0],Integer.parseInt(hashtag[1]));
            	}
            	//If STOP or query 
            	else{
            		if(hashtag[0].equals("STOP")){
            			break;
            		}
            		else{			
            			executeQueries(Integer.parseInt(hashtag[0]));
            		}
            	}
            }   
            // Always close files.
            bufferedReader.close();     
            bw.close();
    		long endTime = System.nanoTime();
    		System.out.println("Took "+(endTime - startTime)/1000000 + " ms");
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}

	/**
	 * This method calls the insert methods of Heap_Methods class based on new insert or update.
	 * @author Maulik_Lalani
	 * @param hashtag
	 * @param frequency
	 */
	public static void insertHashtag(String hashtag, int frequency){
		
		// If hashtag is not present then create new node and insert it. Also add hashtag to the map of all the hashtags. 
		if(!storeHeapPointers.containsKey(hashtag)){
			
			Fibonacci_Heap_Node newNode = new Fibonacci_Heap_Node(frequency,hashtag);
			newNode = hm.insertNewNode(newNode);
			//insert new hashtag into the map
			storeHeapPointers.put(hashtag,newNode);
		}
		// Hashtag is already present so increase the key
		else{
			hm.increaseKey(storeHeapPointers.get(hashtag),frequency);
		}
	}
	
	
	/**
	 * This method fetches the query results and remove nodes from the heap and then reinserts all the nodes.
	 * @author Maulik_Lalani
	 * @param query
	 */
	public static void executeQueries(int query){
		
		//this array list stores all the removed nodes.
		LinkedHashMap<String,Fibonacci_Heap_Node> queryResult =new LinkedHashMap<String,Fibonacci_Heap_Node>();
		
		//remove nodes from the heap
		while(query>0){
			query--;
			Fibonacci_Heap_Node node = hm.removeMax();
			//removedNodes.add(node);
		
			queryResult.put(node.hashtag, node);
			storeHeapPointers.remove(node.hashtag);
		}
		// write results to output file 
		writeToOutputFile(queryResult);
			
		//reinsert all the removed nodes to the heap
		for (String tag : queryResult.keySet()) {
			insertHashtag(tag,queryResult.get(tag).data);
		}
	}
	
	/**
	 * This method writes the one query result to the output file.
	 * @author Maulik_Lalani
	 * @param queryResult
	 */
	public static void writeToOutputFile(LinkedHashMap<String,Fibonacci_Heap_Node> queryResult){
		
		try {
			int counter =0;
			for (String tag : queryResult.keySet()) {
				
				String hashtag = tag;
				if(counter==queryResult.size()-1){
					bw.write(hashtag.substring(1, hashtag.length()));
				}
				else{
					bw.write(hashtag.substring(1, hashtag.length())+",");
				}
				counter++;
			}
			bw.write("\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
