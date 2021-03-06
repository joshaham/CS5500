package assignment9;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


// Audio class
public class Audio {

   public String filename = null;
   private int[][] data = null;
   AudioHeader header = null;
   private BufferedInputStream bis = null;
   private AverageAmplitude AverageAmplitude = null;
   
   @Override
   public String toString(){
	   return header.toString();
   }
   // returns the data
   public int[][] getData() {
      return data;
   }

   // returns the audio header information
   public AudioHeader getHeader(){
      return this.header;
   }

   // returns the average amplitude of all positive amplitudes per second
   public int[] getPostiveAmplitudeValuePerSecondWithOverlap(){
      return AverageAmplitude.getPostiveHashValuePerInterval();
   }

   // returns the average amplitude of all negative amplitudes per second
   public int[] getNegativeAmplitudeValuePerSecondWithOverlap(){
      return AverageAmplitude.getNegativeHashValuePerInterval();
   }

   // returns angles of the plot line of positive amplitudes array
   public int[] getPositiveValuesAngle(){
      return AverageAmplitude.getPostiveAngles();
   }

   // returns angles of the plot line of negative amplitudes array
   public int[] getNegativeValuesAngle(){
      return AverageAmplitude.getNegativeAngles();
   }

   // returns an instance of Audio
   public static Audio getInstance(String filePath){

      Audio audio=null;
      String fileCanonicalPath = 
         new Convert2StandardFormat().Convert2CanonicalFormat(filePath);
      if(fileCanonicalPath==null){
         return null;
      }
      String fileName=getActualName(filePath);
      FileInputStream fis=null;
      BufferedInputStream bis = null;
      try {
         fis = new FileInputStream(fileCanonicalPath);
         bis = new BufferedInputStream(fis);
         audio = new Audio(bis,fileCanonicalPath,fileName);
      } catch (Exception e) {
         if(Assignment9.DEBUG){
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         audio=null;
      } finally {
         try {
            if (bis != null)
               bis.close();
            if (fis != null)
               fis.close();
         } catch (Exception e) {
        	 if(Assignment9.DEBUG){
        		 e.printStackTrace();
        	 }
         }
      }
      if(!filePath.equals(fileCanonicalPath)){
         removeCanonicalPath(fileCanonicalPath);
      }
      return audio;
   }

   // remove the temporary file 
   private static void removeCanonicalPath(String fileCanonicalPath) {
      Process p=null;
      try {
         String str="rm "+fileCanonicalPath;
         p =java.lang.Runtime.getRuntime().exec(str);
      } catch (IOException e) {
         e.printStackTrace();
      }
      synchronized(p){
         try {
            p.wait(3000);
         } catch (InterruptedException e) {
        	 if(Assignment9.DEBUG){
        		 e.printStackTrace();
        	 }
         }
      }

   }

   // trim the path to get the file name
   private static String getActualName(String filePath){
      String[] strs = filePath.split("/");
      String fileName = strs[strs.length - 1];
      return fileName;
   }

   public Audio(BufferedInputStream bis, String fileCanonicalPath, 
                   String fileName) {
      this.bis=bis;
      this.filename = fileName;
      byte[] fileArray=readFile2ByteArray(new File(fileCanonicalPath));
      byte[] dataheader=Arrays.copyOfRange(fileArray, 0, 44);
      this.header = 
         AudioHeader.getInstance(fileName,dataheader,fileArray.length-44);

      readString(WaveConstants.LENCHUNKDESCRIPTOR);
      readLong();
      readString(WaveConstants.LENWAVEFLAG);

      readString(WaveConstants.LENFMTSUBCHUNK);
      readLong();
      readInt();
      readInt();
      readLong();
      readLong();
      readInt();
      readInt();
      readString(WaveConstants.LENDATASUBCHUNK);
      readLong();

      this.data = new int[header.numChannels][(fileArray.length-44)/
      (header.numChannels*header.bitePerSample/8)];

      // read channel data
      for (int i = 0; i < this.data[0].length; ++i) {
         for (int n = 0; n < header.getNumChannels(); ++n) {
            if (header.bitePerSample == 8) {
               try {
                  this.data[n][i] = bis.read();
               } catch (IOException e) {
              	 if(Assignment9.DEBUG){
            		 e.printStackTrace();
            	 }
               }
            } else if (header.bitePerSample == 16) {
               this.data[n][i] = this.readInt();
            }
         }
      }
      AverageAmplitude = new AverageAmplitude(this.data,header);
   }


   private int readInt() {
      byte[] buf = new byte[2];
      int res = 0;
      try {
         if (bis.read(buf) != 2)
            throw new IOException("no more data!!!");
         res = (buf[0] & 0x000000FF) | (((int) buf[1]) << 8);
      } catch (IOException e) {
    	 if(Assignment9.DEBUG){
    		 e.printStackTrace();
    	 }
      }
      return res;
   }

   private long readLong() {
      long res = 0;
      try {
         long[] l = new long[4];
         for (int i = 0; i < 4; ++i) {
            l[i] = bis.read();
            if (l[i] == -1) {
               throw new IOException("no more data!!!");
            }
         }
         res = l[0] | (l[1] << 8) | (l[2] << 16) | (l[3] << 24);
      } catch (IOException e) {
    	 if(Assignment9.DEBUG){
    		 e.printStackTrace();
    	 }
      }
      return res;
   }

   private String readString(int len) {
      byte[] buf = new byte[len];
      try {
         if (bis.read(buf) != len)
            throw new IOException("no more data!!!");
      } catch (IOException e) {
    	 if(Assignment9.DEBUG){
    		 e.printStackTrace();
    	 }
      }
      return new String(buf);
   }

   // read file to array
   static byte[] readFile2ByteArray(File file){
      byte[] fileArray=null;
      try{
         InputStream inputStream = new FileInputStream(file);
         fileArray = new byte[(int)file.length()];
         inputStream.read(fileArray, 0, fileArray.length);
         inputStream.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
         System.err.println("File not found");
         System.exit(1);
      } catch (IOException e) {
    	 if(Assignment9.DEBUG){
    		 e.printStackTrace();
    	 }
         System.exit(1);
      } 
      return fileArray;
   }

   public String getFileName() {
      return this.header.getFileName();
   }


   public static void main(String[] args){
      Audio reader1 = Audio.getInstance("A8/D2/comm2.wav");
      Audio reader2 = Audio.getInstance("A8/D1/AAA1.wav");
      System.out.println(reader1);
      System.out.println(reader2);
//      drawRawData(reader1.getData()[1],"",reader2.getData()[1],"");
   }

}