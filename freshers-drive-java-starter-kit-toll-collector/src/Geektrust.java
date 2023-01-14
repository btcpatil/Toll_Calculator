import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;


public class Geektrust {
	
	private static int totalAmountCollected;
	private static int totalDiscount;
	private static int totalCashTransactions;
	
	static Map<String,Integer> fastTag = new HashMap<>();
	
    private static Map<String, Integer> vehicleTypeTollMap =  new HashMap<>();
	private static Map<String, Integer> vehicleTypeCountMap = new HashMap<>();
	
	//static Set<Vehicle> list = new HashSet<>();
	static private Map<String,String> journeyType = new HashMap<>();
	
	
    public static void main(String[] args)  {
    	
        try {
           
            FileInputStream fis = new FileInputStream("sample_input//input1.txt");
            Scanner sc = new Scanner(fis); 
            
            while (sc.hasNextLine()) {
               String[] inputCommand = sc.nextLine().split(" ");
              
               if(inputCommand[0].equals("FASTAG")) {
            	   fastTag.put(inputCommand[1],Integer.parseInt(inputCommand[2]));
               }
               
               //System.out.println(fastTag);
               
               if(inputCommand[0].equals("COLLECT_TOLL")) {
            	   
            	   if(journeyType.containsKey(inputCommand[2])) {
            		   calculateToll(inputCommand[2], inputCommand[1], "return");
            		   journeyType.remove(inputCommand[2]);
            	   }
            	   else {
            		   journeyType.put(inputCommand[2], inputCommand[1]);
            		   calculateToll(inputCommand[2], inputCommand[1], "single");
            	   }
            	   
               }
               
               
            }
            sc.close(); // closes the scanner
        } catch (IOException e) {
        	
        }
        
        HashMap<String, Integer> sortedMap = sortByValue(vehicleTypeTollMap);
        
        System.out.println(totalAmountCollected);
        System.out.println(totalCashTransactions);
        System.out.println(totalDiscount);
        System.out.println(sortedMap);
       // System.out.println(vehicleTypeTollMap);
       
	}
    
    public static void calculateToll(String vehicleNumber, String vehicleType, String journeyType) {
    	
    	int toll = 0;
    	int discount = 0;
    	boolean isFASTAGEused = true;
    	int balance = 0;
    	int flatFee = 40;
    	
    	
    	if(vehicleType.equals("TRUCK") || vehicleType.equals("BUS")) toll = 200;
    	if(vehicleType.equals("VAN") || vehicleType.equals("CAR") || vehicleType.equals("RICKSHAW")) toll = 100;
    	if(vehicleType.equals("SCOOTER") || vehicleType.equals("MOTORBIKE")) toll = 50;
    	
    	if(fastTag.get(vehicleNumber) != null) {
    		balance = fastTag.get(vehicleNumber);
    		
    		if(toll >= fastTag.get(vehicleNumber)) {
        		fastTag.put(vehicleNumber, 0);
        	}
        	else {
        		fastTag.put(vehicleNumber, balance - toll);
        	}
    	}
    	
    	
    	
    	if (journeyType.equals("return")) {
    		discount = toll / 2;
    		toll = toll/2;
        }
    	
		if(balance < toll) {
		   totalCashTransactions += toll - balance + flatFee;
		   totalAmountCollected += flatFee;
		}
		
    	if(journeyType.equals("single")) {
    		
    		totalAmountCollected += toll - discount;
    		totalDiscount += discount;
    	}
    	else {
    		totalAmountCollected += toll;
    		totalDiscount += discount;
    	}
    	
        
         
         // Update vehicle type count and toll amount for the vehicle type
         if (vehicleTypeCountMap.containsKey(vehicleType)) {
             vehicleTypeCountMap.put(vehicleType, vehicleTypeCountMap.get(vehicleType) + 1);
             vehicleTypeTollMap.put(vehicleType, vehicleTypeTollMap.get(vehicleType) + toll - discount);
         } else {
             vehicleTypeCountMap.put(vehicleType, 1);
             vehicleTypeTollMap.put(vehicleType, toll - discount);
         }
    	
    }
    
    
    public static HashMap<String,Integer> sortByValue(Map<String,Integer> map) {
    	
    	List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
    	
    	Collections.sort(list, new Comparator<Map.Entry<String,Integer>>() {

			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				
				if(o1.getValue().equals(o2.getValue())) {
					return o1.getKey().compareTo(o2.getKey());
				}
				else {
					return o2.getValue().compareTo(o1.getValue());
				}
				
			}
    		
		});
    	
    	HashMap<String, Integer> temp = new LinkedHashMap<>();
    	for(Map.Entry<String,Integer> em:list) {
    		temp.put(em.getKey(), em.getValue());
    	}
    	return temp;
    	
    }
    
    
}
