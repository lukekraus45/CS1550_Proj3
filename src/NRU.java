import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;


public class NRU {

	
	
	@SuppressWarnings("unused")
	public static void NRU(int numFrames, int refreshRate, String traceFileName){
		
		
			//intialize the variables to 0 that we will track for statistics
				int[] frames = new int[numFrames];
				final int PT_SIZE_1MB = 1048576;
				final int PAGE_SIZE_4KB = 4096;
				final int PTE_SIZE_BYTES = 4;
		
				
				
			    int reset_counter=0;
				
				//intialize the variables to 0 that we will track for statistics
				int numDiskWrites = 0;
				int numMemAccess = 0;
				int numPageFault = 0;
				int frameCounter = 0;//tracks the number of frames currently used 
				BufferedReader buffered_reader;
				
				
				try {
					buffered_reader = new BufferedReader(new FileReader(traceFileName));
					while(buffered_reader.readLine() != null){
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
				Hashtable<Integer, PageTableEntry> p_table = new Hashtable<Integer, PageTableEntry>(PT_SIZE_1MB);
				
			    
				  //we need to add our pages to our Page Table p_table
			    for(int i = 0; i < PT_SIZE_1MB; i++){
			    	PageTableEntry temp_pg = new PageTableEntry();
			    	p_table.put(i, temp_pg);
			    }
				
				
				/*
				 * Now we have the # of mem accesses in the file. We need to begin working the algorithm. 
				 * We will need to track the writes to disk as well as page faults
				 * We will use our hashtable for the simulation. 
				 * 
				 * */
				try {
					buffered_reader = new BufferedReader(new FileReader(traceFileName));
					while(buffered_reader.ready()){
						
						reset_counter++;
						//go through the file and read in the data. We need to split it up.
					
						if(reset_counter % refreshRate == 0){
							//this means that we need to reset all the frames
							//to make their ref. bit 0 or false
							for(int i = 0; i < numFrames; i++){
								int temp_fNum = frames[i];
								PageTableEntry temp = p_table.get(temp_fNum);
								temp.referenced=false;
							}
						}
						
						
						
						//read in the value and split it up by the space
						String line = buffered_reader.readLine();
						String[] temp_array = line.split(" ");//split on space
						String address = temp_array[0];
						String read_or_write = temp_array[1];
						StringBuilder s = new StringBuilder();
						char[] char_array = address.toCharArray();
						s.append("0x");
						
						for(int i =0; i<5; i++){
							s.append(char_array[i]);
						}
						
						
						
						
						int int_address = Integer.decode(s.toString());//decodes the HEX into decimal
						
						PageTableEntry temp = p_table.get(int_address);
						temp.referenced = true;
						temp.index = int_address;
						
						//check dirty bit
						if(read_or_write.equalsIgnoreCase("w")){
							temp.dirty = true;
							
						}else{
							temp.dirty = false;
						}
						
						if(temp.valid == false){
							//if valid bit is not set it is not yet it memory
							//This means a pagefault has occured and we need to handle it
							numPageFault++;
							if(frameCounter < numFrames){
								//we will track the number of frames that are being used
								//throughout the program. If the frameCounter = numFrames 
								//then we need to run the algoirthm
								//if it is less then we can just add without a problem
								temp.frame = frameCounter;
								temp.valid = true;
								frames[frameCounter] = int_address;
								frameCounter++;
								System.out.println("Page Fault – no eviction at Location 0x" + address);
							}else if(frameCounter == numFrames){
								//if they are == then we need to handle it by swapping it out
								//here we will run the NRU  alogirthm
								int frame_counter = 0;
								
								//we will check all 4 classes. We want a page from class 1
								//if we cant find one we will move to 2,3,4 in that order 
								PageTableEntry evictPage_c1 = null;
								PageTableEntry evictPage_c2 = null;
								PageTableEntry evictPage_c3 = null;
								PageTableEntry evictPage_c4 = null;

								while(frame_counter < numFrames){
									
									//get temp page
									 int temp_frame_num = frames[frame_counter];
									 PageTableEntry tempPage = p_table.get(temp_frame_num);
									 if(tempPage.referenced && tempPage.dirty && evictPage_c4 == null&& tempPage.valid){
										 //both set to 1 and haven't seen a page yet with the bits set as such
										 //set the evict page to be the same 
										 //class 4
										 evictPage_c4 = new PageTableEntry();
										 evictPage_c4.dirty = tempPage.dirty;
										 evictPage_c4.referenced = tempPage.referenced;
										 evictPage_c4.index = tempPage.index;
										 evictPage_c4.frame = tempPage.frame;
										 evictPage_c4.valid = tempPage.valid;
										 
									 }else if(tempPage.referenced && !tempPage.dirty && evictPage_c3 == null && evictPage_c4 == null && tempPage.valid){
										 //dirty set to 0, refrenced to 1 and haven't seen a page yet with the bits set as such
										 //class 3
										 evictPage_c3 = new PageTableEntry();
										 evictPage_c3.dirty = tempPage.dirty;
										 evictPage_c3.referenced = tempPage.referenced;
										 evictPage_c3.index = tempPage.index;
										 evictPage_c3.frame = tempPage.frame;
										 evictPage_c3.valid = tempPage.valid;
										 
									 }else if(!tempPage.referenced && tempPage.dirty && evictPage_c2 == null&&  tempPage.valid){
										 //dirty set to 1 referenced to 1 and haven't seen a page yet with the bits set as such
										 //class 2
										 evictPage_c2 = new PageTableEntry();

										 evictPage_c2.dirty = tempPage.dirty;
										 evictPage_c2.referenced = tempPage.referenced;
										 evictPage_c2.index = tempPage.index;
										 evictPage_c2.frame = tempPage.frame;
										 evictPage_c2.valid = tempPage.valid;
										 
									 }else if(!tempPage.referenced && !tempPage.dirty&& tempPage.valid){
										 //both set to 0. Our desired result
										 //c1 If this is hit we can stop the algorithm. 
										 	evictPage_c1 = new PageTableEntry();
				                           temp.frame = tempPage.frame;
										 
										 if(tempPage.dirty){
												//if the page is dirty then we need to write it to disk
												numDiskWrites++;
												System.out.println("Page Fault – eviction (Dirty) at Location 0x" + address);

											}else{
												System.out.println("Page Fault – eviction (Clean) at Location 0x" + address);
											}
										 
										 
										 frames[temp.frame] = temp.index;
				                           tempPage.valid = false;
				                           tempPage.dirty = false;
				                           tempPage.referenced = false;
				                           tempPage.frame = -1;
				                           p_table.put(tempPage.index, tempPage);
				                           temp.valid = true;
				                           p_table.put(temp.index, temp);
				                           
				                           break;
									 }
									
									
									
									frame_counter++;
								}
								
								//after we have found our page that we want we check each class. If we find
								//that 1 is null we move to 2 and so on. We want c1, c2, c3, then c4 in that order. 
								PageTableEntry evictPage = new PageTableEntry();
								if(evictPage_c1 == null){
									
									if(evictPage_c2 != null){
									evictPage = evictPage_c2;

									}else if(evictPage_c3 != null){
									evictPage = evictPage_c3;

									}else {
									evictPage = evictPage_c4;

								}
								
								//now that evictPage contains the page we are using check if it is clean or dirty
								if(evictPage.dirty){
									//if the page is dirty then we need to write it to disk
									numDiskWrites++;
									System.out.println("Page Fault – eviction (Dirty) at Location 0x" + address);

								}else{
									System.out.println("Page Fault – eviction (Clean) at Location 0x" + address);
								}
			                     temp.frame = evictPage.frame;

								//complete the swap
			                     frames[temp.frame] = temp.index;
			                     evictPage.valid = false;
			                     evictPage.dirty = false;
			                     evictPage.frame = 0;
			                     evictPage.referenced = false;
			                     p_table.put(evictPage.index, evictPage);
			                     temp.valid = true;
			                     p_table.put(temp.index, temp);
						
								
								
								}	
							}else{
								System.out.println("Error: frameCounter > numFrames!!");
							}
							
							
							
						}else{
							//If to goes here then it is already in memory so we can skip it
							
							System.out.println("Hit at Location 0x"  + address);
							
						}
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
		
				System.out.println("Algorithm: NRU");
				System.out.println("Number of frames: \t" + numFrames);
				System.out.println("Total Memory Accesses \t" +  numMemAccess);
				System.out.println("Total Page Faults: \t" + numPageFault);
				System.out.println("Total writes to disk: \t" +  numDiskWrites);
	}
	
}
