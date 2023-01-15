import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import exceptions.TollCalculatorException;


public class Geektrust {
	
	private static int totalAmountCollected;
	private static int totalDiscount;
	private static int totalCashTransactions;
	
	private static Map<String,Integer> vehicleNumberFastagBalanceMap = new HashMap<>();
	private static Map<String,String> vehicleNumberVehicleTypeMap = new HashMap<>();
    private static Map<String, Integer> vehicleTypeTollMap =  new HashMap<>();
	private static Map<String, Integer> vehicleTypeCountMap = new HashMap<>();
	
    public static void main(String[] args) throws TollCalculatorException  {
    	
       try {
    	   if(args.length != 1) {
    		   throw new TollCalculatorException("Input file not supplied. Please provide the input file");
    	   }
    	   
            FileInputStream fis = new FileInputStream(args[0]);
            Scanner sc = new Scanner(fis); 
            
            while (sc.hasNextLine()) {
               String[] inputCommand = sc.nextLine().split(" ");
               //Mapping vehicle number with fastag balance.
               if(inputCommand[0].equals("FASTAG")) {
            	   vehicleNumberFastagBalanceMap.put(inputCommand[1],Integer.parseInt(inputCommand[2]));
               }
               //Mapping vehicle number with vehicle type 
               if(inputCommand[0].equals("COLLECT_TOLL")) {
            	   if(vehicleNumberVehicleTypeMap.containsKey(inputCommand[2])) {
            		   calculateToll(inputCommand[2], inputCommand[1], "return");
            		   vehicleNumberVehicleTypeMap.remove(inputCommand[2]);
            	   }
            	   else {
            		   vehicleNumberVehicleTypeMap.put(inputCommand[2], inputCommand[1]);
            		   calculateToll(inputCommand[2], inputCommand[1], "single");
            	   }
                }
             }
                 sc.close(); // closes the scanner
       } 
       catch (IOException e) {
        	 System.out.println(e.getMessage());
       }
       
        //Calling sorting function which will sort vehicle on the base of amount collected.
        HashMap<String, Integer> sortedMap = sortVehicleByAmount(vehicleTypeTollMap);
        
        //Printing the output.
        System.out.println("TOTAL_COLLECTION "+totalAmountCollected+" "+totalDiscount);
        System.out.println("PAYMENT_SUMMARY "+(totalAmountCollected-totalCashTransactions)+" "+totalCashTransactions);
        System.out.println("VEHICLE_TYPE_SUMMARY");
        for(Map.Entry<String, Integer> vehicle : sortedMap.entrySet() ) {
        	System.out.println(vehicle.getKey()+" "+vehicleTypeCountMap.get(vehicle.getKey()));
        }
	}
    
    //Toll calculator according to the type and journey type of the vehicle.
    public static void calculateToll(String vehicleNumber, String vehicleType, String journeyType) {
    	int toll = 0;
    	int discount = 0;
    	int fastagBalance = 0;
    	int flatFee = 0;
    	//Update the toll according to the weight of the vehicle.
    	if(vehicleType.equals("TRUCK") || vehicleType.equals("BUS")) toll = 200; //Heavy vehicle
    	else if(vehicleType.equals("VAN") || vehicleType.equals("CAR") || vehicleType.equals("RICKSHAW")) toll = 100; //Light vehicle
    	else toll = 50; //Two wheeler
    	//Check the fastag balance of the particular vehicle.
    	if(vehicleNumberFastagBalanceMap.get(vehicleNumber) != null) {
    		fastagBalance = vehicleNumberFastagBalanceMap.get(vehicleNumber);
    		
    		if(toll >= fastagBalance) {
    			vehicleNumberFastagBalanceMap.put(vehicleNumber, 0);
        	}
        	else {
        		vehicleNumberFastagBalanceMap.put(vehicleNumber, fastagBalance - toll);
        	}
    	}
    	//Calculating toll based on the type of journey.
    	if (journeyType.equals("return")) {
    		discount = toll / 2;
    		toll = toll/2;
    		totalAmountCollected += toll;
    		totalDiscount += discount;
         }
    	 else {
    		totalAmountCollected += toll - discount;
    		totalDiscount += discount;
    	 }
    	 //Adding flat fee if insufficient balance in the fastag. 
    	 if(fastagBalance < toll) {
    		flatFee = 40;
  		    totalCashTransactions += toll - fastagBalance + flatFee;
  		    totalAmountCollected += flatFee;
  		 }
         // Update vehicle type count and toll amount for the vehicle type
         if (vehicleTypeCountMap.containsKey(vehicleType)) {
             vehicleTypeCountMap.put(vehicleType, vehicleTypeCountMap.get(vehicleType) + 1);
             vehicleTypeTollMap.put(vehicleType, vehicleTypeTollMap.get(vehicleType) + toll  + flatFee);
         } else {
             vehicleTypeCountMap.put(vehicleType, 1);
             vehicleTypeTollMap.put(vehicleType, toll + flatFee);
         }
    }
    
    //Sorting vehicle based on the amount of toll is collected.
    public static HashMap<String,Integer> sortVehicleByAmount(Map<String,Integer> map) {
    	
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
    	HashMap<String, Integer> sortedMap = new LinkedHashMap<>();
    	for(Map.Entry<String,Integer> em:list) {
    		sortedMap.put(em.getKey(), em.getValue());
    	}
    	return sortedMap;
    }
}
