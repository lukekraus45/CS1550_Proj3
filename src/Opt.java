
public class Opt {

	
	public static  void Opt(int numFrames, String traceFileName){
		//intialize the variables to 0 that we will track for statistics
				int numMemAccess = 0;
				int numPageFault = 0;
				int numDiskWrites = 0;
				
				
				
				//print out the statistics 
				
				System.out.println("Algorithm: NRU");
				System.out.println("Number of frames: \t" + numFrames);
				System.out.println("Total Memory Accesses \t" +  numMemAccess);
				System.out.println("Total Page Faults: \t" + numPageFault);
				System.out.println("Total writes to disk: \t" +  numDiskWrites);
				
	}
}
