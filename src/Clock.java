import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;


public class Clock {

	//impliment the clock functionality 
	public static void clock(int numFrames, String traceFileName){
		
		int[] frames = new int[numFrames];//creates array with the number of frames
		final int PT_SIZE_1MB = 1048576;//page table size
		final int PAGE_SIZE_4KB = 4096;//size of each page 
		final int PTE_SIZE_BYTES = 4;
		
		
		
	    int debug = 0;
	  
		
		//intialize the variables to 0 that we will track for statistics
		int numDiskWrites = 0;
		int numMemAccess = 0;
		int numPageFault = 0;
		int frameCounter = 0;//tracks the number of frames currently used 
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
		//using a hashtable becuase of O(1) lookup time
		Hashtable<Integer, PageTableEntry> p_table = new Hashtable<Integer, PageTableEntry>(PT_SIZE_1MB*PTE_SIZE_BYTES);
		//System.out.println(numMemAccess);
	    
		  //we need to add our pages to our Page Table p_table
	    for(int i = 0; i < PT_SIZE_1MB*PTE_SIZE_BYTES; i++){
	    	PageTableEntry temp = new PageTableEntry();
	    	p_table.put(i, temp);
	    }
		
		
		/*
		 * Now we have the # of mem accesses in the file. We need to begin working the algorithm. 
		 * We will need to track the writes to disk as well as page faults
		 * We will use our hashtable for the simulation. 
		 * 
		 * */
	    int position = 0;//this will be used to track the position of the clock hand
		try {
			br = new BufferedReader(new FileReader(traceFileName));
			int counter = 0;
			while(br.ready()){
				//go through the file and read in the data. We need to split it up.
			
				//read in the value and split it up by the space
				String line = br.readLine();
				String[] temp_array = line.split(" ");//split on space
				String address = temp_array[0];
				String read_or_write = temp_array[1];
				StringBuilder s = new StringBuilder();
				char[] char_array = address.toCharArray();
				s.append("0x");
				for(int i =0; i<5; i++){
					s.append(char_array[i]);
				}
				//only need the first 5 bits (for the page table entry)
				
				//s.append(address);
				
				
				//System.out.println("Address: " + s.toString());
				int int_address = Integer.decode(s.toString());//decodes the HEX into decimal
			   
				PageTableEntry temp = p_table.get(int_address);
				//System.out.println(int_address);
				temp.referenced = true;
				temp.index = int_address;
				//check dirty bit
				//System.out.println(temp.index);
				if(read_or_write.equalsIgnoreCase("w")){
					temp.dirty = true;
					System.out.println("w");
					
				}else{
					temp.dirty = false;
					System.out.println("r");
				}
				
				if(!temp.valid){
					//if valid bit is not set it is not yet it memory
					//This means a pagefault has occured and we need to handle it
					numPageFault++;
					if(frameCounter < numFrames){
						//we will track the number of frames that are being used
						//throughout the program. If the frameCounter = numFrames 
						//then we need to run the algoirthm
						//if it is less then we can just add without a problem
						temp.frame = frameCounter;//set the frame that the page is in to the current frame value
						temp.valid = true;//now in memory so its true
						frames[frameCounter] = int_address;//add the address to the frame array
						frameCounter++;
						//System.out.println(frameCounter);
					}else if(frameCounter == numFrames){
						//if they are == then we need to handle it by swapping it out
						//here we will run the clock alogirthm
						boolean not_evicted = true;
						int temp_clock_position =0;
						while(not_evicted){
							if(position == numFrames){
								position = 0;//reset the position if it hits the end
							}else if(position > numFrames){
								//should not hit this 
								System.out.println("Error: position of clock hand is > numFrames");
							}
							
							int temp_frame_num = frames[position];//get the page number of the frame that is at the clock hand position
							
							PageTableEntry temp_page = p_table.get(temp_frame_num);
							int index = temp_page.index;
								
									
									
								
							
							if(p_table.get(index).referenced == true){
								//if it is already set then we set it to false
								p_table.get(index).referenced = false;
							}else{
								//if it is not set then we can evict it.
								not_evicted = false;
								temp_clock_position = position;
								
							}
							
							position++;
						}
						
						PageTableEntry evictPage = p_table.get(temp_clock_position);
						if(evictPage.dirty){
							//if the page is dirty then we need to write it to disk
							numDiskWrites++;
							
						}

						//complete the swap
						evictPage.dirty = false;
						evictPage.valid = false;
						evictPage.referenced = false;
						frames[evictPage.frame] = temp.index;
						temp.frame = evictPage.frame;
						evictPage.frame = 0;
						

						temp.frame = evictPage.frame;
						temp.valid = true;

						//put the updated page into the page table
						p_table.put(frames[temp_clock_position], evictPage);
						
						
						
						
						
						
						
					}else{
						System.out.println("Error: frameCounter > numFrames!!");
					}
					
					
					
				}else{
					//If to goes here then it is already in memory so we can skip it
					//System.out.println("Already in memory");//debugging
				}
				debug++;
	            p_table.put(int_address, temp);
				
			}//end of reading file
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		//print out the statistics 
		//System.out.println("Debug " + debug);
		System.out.println("Algorithm: Clock");
		System.out.println("Number of frames: \t" + numFrames);
		System.out.println("Total Memory Accesses \t" +  numMemAccess);
		System.out.println("Total Page Faults: \t" + numPageFault);
		System.out.println("Total writes to disk: \t" +  numDiskWrites);
	}
	
}
