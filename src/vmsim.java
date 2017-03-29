
public class vmsim {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int numFrames = 0;
		String algorithm = "";
		String traceFile = "";
		int refresh = 0;
		int tau = 0;
		
		if(args.length == 5){
			//this would use Random, OPT or Clock and be length 5
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

		}
		else{
			throw new IllegalArgumentException("Please enter command line args in correct form. Ex. -n 15 -a work -r 15 -t 10 gcc.trace");
		}
		

		
		
		if(algorithm.equalsIgnoreCase("opt")){
			Opt.Opt(numFrames, traceFile);
		}else if(algorithm.equalsIgnoreCase("clock")){
			//Clock.clock(numFrames, traceFile);
			Clock.clock(numFrames,traceFile);
		}else if(algorithm.equalsIgnoreCase("nru")){
			NRU.NRU(numFrames, refresh, traceFile);
		}else if(algorithm.equalsIgnoreCase("random")){
			Random.Random(numFrames, traceFile);
		}else{
			throw new IllegalArgumentException("Please make sure you enter a valid algorithm: opt, clock, nru, work");
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
				
	}

}
