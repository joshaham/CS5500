package comparator;


class HeaderChecker{

	static boolean DEBUG=false;
	enum Error{
		ARGUMENTSLENGTH,
		INCORRECTCOMMAND,
		WRONGFILEFORMAT1,
		WRONGFILEFORMAT2,
	}


	public static boolean CheckFormat(String[] args){

		HeaderChecker instance = new HeaderChecker();
		Error err =instance.checkInput(args);
		
		if(err != null) { 
			if (err == Error.WRONGFILEFORMAT1) {
				System.err.println(String.format("ERROR: %s is not a "
						+ "supported format", args[1]));
			}
			else if (err == Error.WRONGFILEFORMAT2) { 
				System.err.println(String.format("ERROR: %s is not a "
						+ "supported format", args[3]));
			}
			else if(err==Error.INCORRECTCOMMAND){
				System.err.println("ERROR: incorrect command line");
			}
			else if(err==Error.ARGUMENTSLENGTH){
				System.err.println("ERROR: incorrect command line");
			}
			else{
				System.err.println("ERROR: default error");
			}
			return false;
		} 

		return true;
	}

	// Checks to see if the inputs are used correctly
	private Error checkInput(String[] args){
		if (args.length!=4){
			return Error.ARGUMENTSLENGTH;
		}
		if (!(args[0].trim()).equals("-f") || !(args[2].trim()).equals("-f")) {
			return Error.INCORRECTCOMMAND;
		}
		String file1 = args[1].trim(), file2 = args[3].trim();
		if(!file1.endsWith(".wav")) {
			return Error.WRONGFILEFORMAT1;
		}
		if (!file2.endsWith(".wav")) { 
			return Error.WRONGFILEFORMAT2;
		}
		return null;
	}



}