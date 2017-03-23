import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;


public class Clock {

	//impliment the clock functionality 
	public static void clock(int numFrames, String traceFileName){
		
		int[] frames = new int[numFrames];//creates array with the number of frames
		final int PT_SIZE_1MB = 1048576;//page table size
		final int PAGE_SIZE_4KB = 4096;//size of each page 
		
		
		
		//we will use a hashtable to create the page table for the algorithm
	    Hashtable<Integer, PageTableEntry> p_table = new Hashtable<Integer, PageTableEntry>();

	    
	    
	    //we need to add our pages to our Page Table p_table
	    //this will create an entry for each location and add it to the table by int value
	    for(int i = 0; i < PT_SIZE_1MB; i++){
	    	PageTableEntry temp = new PageTableEntry();
	    	p_table.put(i,temp);
	    }
		
		//intialize the variables to 0 that we will track for statistics
		int numDiskWrites = 0;
		int numMemAccess = 0;
		int numPageFault = 0;
		
		BufferedReader br;
		//create a reader that will count the # of memAccess (or lines in file)
		try {
			br = new BufferedReader(new FileReader(traceFileName));
			while(br.readLine() != null){
				numMemAccess++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * Now we have the # of mem accesses in the file. We need to begin working the algorithm. 
		 * We will need to track the writes to disk as well as page faults
		 * We will use our hashtable for the simulation. 
		 * 
		 * */
		try {
			br = new BufferedReader(new FileReader(traceFileName));
			while(br.ready()){
				//go through the file and read in the data. We need to split it up.
			
				//read in the value and split it up by the space
				String line = br.readLine();
				String[] temp_array = line.split(" ");//split on space
				String address = temp_array[0];
				String read_or_write = temp_array[1];
				StringBuilder s = new StringBuilder();
				char[] address_arr = address.toCharArray();
				s.append("0x");
				if(address_arr[7] != '0'){
					//it equals something other than 0
					s.append(address);
				}else{
					for(int i = 0; i < 7; i++){
						s.append(address_arr[i]);
					}
				}
				
				
				//System.out.println("Address: " + s.toString());
				int int_address = Integer.decode(s.toString());//decodes the HEX into decimal
				
				PageTableEntry temp = p_table.get(int_address);
				System.out.println(int_address);
				System.out.println(temp);
				temp.referenced = true;
				temp.index = int_address;
				//check dirty bit
				if(read_or_write.equalsIgnoreCase("w")){
					temp.dirty = true;
				}else{
					temp.dirty = false;
				}
				
				if(temp.present){
					
				}
				
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		//print out the statistics 
		
		System.out.println("Algorithm: Clock");
		System.out.println("Number of frames: \t" + numFrames);
		System.out.println("Total Memory Accesses \t" +  numMemAccess);
		System.out.println("Total Page Faults: \t" + numPageFault);
		System.out.println("Total writes to disk: \t" +  numDiskWrites);
	}
	
}
