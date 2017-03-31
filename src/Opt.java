import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public class Opt {

	
	public static  void Opt(int numFrames, String traceFileName){
				//intialize the variables to 0 that we will track for statistics
				int[] frames = new int[numFrames];//creates array with the number of frames
				final int PT_SIZE_1MB = 1048576;//page table size
				final int PAGE_SIZE_4KB = 4096;//size of each page 
				final int PTE_SIZE_BYTES = 4;
				java.util.Random random = new java.util.Random();
				
				
			    int debug = 0;
			  
				
				//intialize the variables to 0 that we will track for statistics
				int numDiskWrites = 0;
				int numMemAccess = 0;
				int numPageFault = 0;
				int frameCounter = 0;//tracks the number of frames currently used 
				BufferedReader br;
				//create a reader that will count the # of memAccess (or lines in file)
				
				//using a hashtable becuase of O(1) lookup time
				Hashtable<Integer, PageTableEntry> p_table = new Hashtable<Integer, PageTableEntry>(PT_SIZE_1MB);
				//System.out.println(numMemAccess);
			    //populate a table that will keep track of the future page accesses
				ArrayList<LinkedList<Integer>> opt_table = new ArrayList<LinkedList<Integer>>();
				  //we need to add our pages to our Page Table p_table
			    for(int i = 0; i < PT_SIZE_1MB; i++){
			    	PageTableEntry temp_pg = new PageTableEntry();
			    	p_table.put(i, temp_pg);
			    	LinkedList<Integer> temp_ll = new LinkedList<Integer>();
			    	opt_table.add(temp_ll);
			    }
				/*For opt we need to preprocess the data so we 
				 * can figure out the future accesses*/
			    try {
					br = new BufferedReader(new FileReader(traceFileName));
					while(br.ready()){
						
						String line = br.readLine();
						String[] temp_array = line.split(" ");//split on space
						String address = temp_array[0];
						StringBuilder s = new StringBuilder();
						char[] char_array = address.toCharArray();
						s.append("0x");
						//s.append(address);
						for(int i =0; i<5; i++){
							s.append(char_array[i]);
						}
						//only need the first 5 bits (for the page table entry)
						
						
						
						
						//System.out.println("Address: " + s.toString());
						int int_address = Integer.decode(s.toString());//decodes the HEX into decimal
						opt_table.get(int_address).add(numMemAccess);
						
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
						char[] char_array = address.toCharArray();
						s.append("0x");
						//s.append(address);
						for(int i =0; i<5; i++){
							s.append(char_array[i]);
						}
						//only need the first 5 bits (for the page table entry)
						
						
						
						
						//System.out.println("Address: " + s.toString());
						int int_address = Integer.decode(s.toString());//decodes the HEX into decimal
						//System.out.println("int address + " + int_address);
						PageTableEntry temp = p_table.get(int_address);
						//System.out.println(int_address);
						
						temp.referenced = true;
						temp.index = int_address;
						//check dirty bit
						//System.out.println(temp.index);
						if(read_or_write.equalsIgnoreCase("w")){
							temp.dirty = true;
							//System.out.println("w");
							
								//if the page is dirty then we need to write it to disk
								//numDiskWrites++;
								//System.out.println("Page Fault – eviction (Dirty) at Location 0x" + address);

							
							
						}else{
							temp.dirty = false;
							//System.out.println("r");
						}
						
						try {
							opt_table.get(int_address).removeFirst();
				        }
				        catch (NoSuchElementException e) {
				            
				            break;
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
								temp.frame = frameCounter;//set the frame that the page is in to the current frame value
								temp.valid = true;//now in memory so its true
								frames[frameCounter] = int_address;//add the address to the frame array
								frameCounter++;
								System.out.println("Page Fault – no eviction at Location 0x" + address);
								//System.out.println(frameCounter);
							}else if(frameCounter == numFrames){
								//if they are == then we need to handle it by swapping it out
								//here we will run the opt  alogirthm
							
								
								
								
								
							
								int num_to_evict = 0;
								int highest_val = 0;
								//need to find the page that will be used furthest in the future
								PageTableEntry test_p = null;
								for(int i = 0; i < numFrames; i++){
									int temp_pgNum = frames[i];
									boolean hasPage = opt_table.get(temp_pgNum).isEmpty();
									test_p = p_table.get(temp_pgNum);

									if(hasPage){
										//if there is not a page there yet we can use this 
										//value because it is not used in the future at all
										num_to_evict = frames[i];
										i = numFrames;
									}else{
										//if it lands here it is not empty. So we need to compare it 
										//to highest value away to see if it is better or not
										try {
											if(opt_table.get(temp_pgNum).get(0) > highest_val){
												
												highest_val = opt_table.get(temp_pgNum).getFirst();
												num_to_evict = frames[i];
												
											}
								        }
								        catch (NoSuchElementException e) {
								            
								            //break;
								        }catch(IndexOutOfBoundsException f){
								        	
								        }
										
										
									}
								}
								PageTableEntry evictPage = p_table.get(num_to_evict);
								
								
								
								
								
								
								if(evictPage.dirty){
									//if the page is dirty then we need to write it to disk
									numDiskWrites++;
									System.out.println("Page Fault – eviction (Dirty) at Location 0x" + address);

								}else{
									System.out.println("Page Fault – eviction (Clean) at Location 0x" + address);
								}

								//complete the swap
								
								
								evictPage.dirty = false;
								evictPage.valid = false;
								evictPage.referenced = false;
								frames[evictPage.frame] = temp.index;
								temp.frame = evictPage.frame;
								evictPage.frame = 0;
								

								temp.valid = true;

								//put the updated page into the page table
								p_table.put(num_to_evict, evictPage);

								
								
								
							}else{
								System.out.println("Error: frameCounter > numFrames!!");
							}
							
							
							
						}else{
							//If to goes here then it is already in memory so we can skip it
							//System.out.println(temp.valid);
							System.out.println("Hit at Location 0x"  + address);
							debug++;
							
						}
						//debug++;
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
				System.out.println("Algorithm: Opt");
				System.out.println("Number of frames: \t" + numFrames);
				System.out.println("Total Memory Accesses \t" +  numMemAccess);
				System.out.println("Total Page Faults: \t" + numPageFault);
				System.out.println("Total writes to disk: \t" +  numDiskWrites);
				
	}
}
