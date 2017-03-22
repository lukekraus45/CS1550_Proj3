
public class vmsim {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int numFrames = 0;
		String algorithm = "";
		String traceFile = "";
		int refresh = 0;
		int tau = 0;
		
		if(args.length == 5){
			//this would use OPT or Clock and be length 5
			if(!args[0].equalsIgnoreCase("-n")){
				throw new IllegalArgumentException("Please enter -n followed by an integer value for the number of frames. Ex. -n 15");
			}
			numFrames = Integer.parseInt(args[1]);
			if(!args[2].equalsIgnoreCase("-a")){
				throw new IllegalArgumentException("Please enter -a followed by the algorithm you wish to use. Ex. -a opt");
			}
			algorithm = args[3];
			
			traceFile = args[4];

			
		}else if(args.length == 7){
			//this would be NRU and use the refresh
			if(!args[0].equalsIgnoreCase("-n")){
				throw new IllegalArgumentException("Please enter -n followed by the number of frames you wish to use. Ex. -n 10 ");
			}
			numFrames = Integer.parseInt(args[1]);
			if(!args[2].equalsIgnoreCase("-a")){
				throw new IllegalArgumentException("Please enter -a followed by the algorithm you wish to use. Ex. -a opt");
			}
			algorithm = args[3];
			if(!args[4].equalsIgnoreCase("-r")){
				throw new IllegalArgumentException("Please enter -r followed by the refresh value you wish to use. Ex. -r 10");
			}
			refresh = Integer.parseInt(args[5]);
			
					
			traceFile = args[6];

		}else if(args.length == 9){
			//this would be work and be lenth 9
			if(!args[0].equalsIgnoreCase("-n")){
				throw new IllegalArgumentException("Please enter -n followed by the number of frames. Ex. -n 10");
			}
			numFrames = Integer.parseInt(args[1]);
			if(!args[2].equalsIgnoreCase("-a")){
				throw new IllegalArgumentException("Please enter -a followed by the algorithm you wish to use");
			}
			algorithm = args[3];
			if(!args[4].equalsIgnoreCase("-r")){
				throw new IllegalArgumentException("Please enter -r followed by the refresh value");
			}
			refresh = Integer.parseInt(args[5]);
			
			if(!args[6].equalsIgnoreCase("-t")){
				throw new IllegalArgumentException("Please enter -t followed by the tau value");
			}
			tau = Integer.parseInt(args[7]);
			
			traceFile = args[8];
		
		}	
		else{
			throw new IllegalArgumentException("Please enter command line args in correct form. Ex. -n 15 -a work -r 15 -t 10 gcc.trace");
		}
		

		
		
		if(algorithm.equalsIgnoreCase("opt")){
			System.out.println("opt");
		}else if(algorithm.equalsIgnoreCase("clock")){
			System.out.println("clock");
		}else if(algorithm.equalsIgnoreCase("nru")){
			System.out.println("NRU");
		}else if(algorithm.equalsIgnoreCase("work")){
			System.out.println("work");
		}else{
			throw new IllegalArgumentException("Please make sure you enter a valid algorithm: opt, clock, nru, work");
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
				
	}

}
