package cosineDistance;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Team7
 */
public class CosineDistance {
    private HashMap<String, Integer> hm=new HashMap<String, Integer>();
    private int num=0;// member variable that keep track of number of word
    private int lines=0;//member variable that keep track of number of line of the file
    
    /**
     * Constructor that takes a file 
     * @param filename 
     */
    public CosineDistance(File filename){
        
        try {
            Scanner scan= new Scanner(filename);
            while (scan.hasNextLine()) {
                lines=lines+1;
                String line = scan.nextLine();
                String[] words = line.split("	");
                //put the word into hashmap
                hm.put(words[0], Integer.parseInt(words[1]));
                }
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot find the file");
        }
    }
    /**
     * pulic method to get the number of word in the hashmap
     * @return 
     */
    public int numOfWordsNoDups(){
        return hm.size();
    }
    
    /**
     * public method to get the number of lines of the file
     * @return 
     */
    public int numberOfLines(){
        return lines;
    }
    
    /**
     * public method to get the number of word 
     * @return 
     */
    public int numOfWords(){
        return num;
    }
    
    /**
     * public method to get should content of the hash map
     */
    public void display(){
        System.out.println(hm.toString());
    }
    /**
     * method to calculate the euclideanNorm of the test
     * @return the euclidean Norm value
     */
    public double euclideanNorm(){
        double length=0.0;
        Iterator iter = hm.entrySet().iterator(); 
        while (iter.hasNext()){            
            Map.Entry eachword = (Map.Entry)iter.next();
            length=length+((int)eachword.getValue()*(int)eachword.getValue());//take the sum of all the squre value
        }
        length=Math.sqrt(length);//do square root to get the value
        return length;
    }

    /**
     * 
     * @return the hashmap value of the class
     */
    public HashMap getMap(){
        return hm;
    }
    /**
     * method to do the dot product of two text using hashmap
     * @param hm2 input a hashmap value
     * @return 
     */
    public double dotProduct(HashMap hm2){
        double dotproduct=0.0;      
        Iterator iter1 = hm.entrySet().iterator(); 
        for(int i=0;i<hm.size();i++){
            Map.Entry eachword1 = (Map.Entry)iter1.next();
            //find the matching word, then do the dotproduct accordingly
            if(hm2.containsKey(eachword1.getKey())){
                dotproduct=dotproduct+((int)eachword1.getValue())*((int)hm2.get(eachword1.getKey()));
        }
        
    }
        return dotproduct;
    }
    
    /**
     * find the distance of two text using hashmap
     * @param hm2
     * @return 
     */
    public double distance(HashMap hm2){
        double dp=dotProduct(hm2);
        double eu=euclideanNorm();
        HashMap temp=hm;
        hm=hm2;
        double eu2=euclideanNorm();
        hm=temp;
        return Math.acos(dp/(eu*eu2));
        
        //return 0.0;
    }
    
}