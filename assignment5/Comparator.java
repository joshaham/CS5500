class Comparator{
  static boolean DEBUG=true;

  public static void main(String[] args){
    Comparator instance = new Comparator();
    int r=instance.checkInput(args);
    if(0!=r){
      System.err.println("ERROR: incorrect command line");
      if(DEBUG){
        System.out.println(r);
      }
    }    
  }
  private int checkInput(String[] args){
     if(args.length!=4){
       return 1;
      }
     if(!(args[0].trim()).equals("f") || !(args[2].trim()).equals("f")){
       return 2;
     }
     String file1=args[1].trim(),file2=args[3].trim();
     if(!file1.endsWith(".wav") || !file2.endsWith(".wav")){
       return 3;
     }
     return 0;

  }
}
