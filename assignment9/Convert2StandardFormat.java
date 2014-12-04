package assignment9;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Convert all the audio files to standard format:
 * : wav format
 * : 44100 sample rate
 * : 16 bit per sample
 * @author zhuoli
 *
 */

public class Convert2StandardFormat {
   // GIVEN: a file path
   // RETURN: the path of the canonical file or null 
   public static String Convert2CanonicalFormat(String filePath){
      String formatFile = filePath;
      if (filePath.endsWith(".mp3")){
         formatFile=convertFromMp3ToWav(filePath);
      }
      else if (filePath.endsWith(".ogg")){
         formatFile = convertFromOggToWav(filePath);
      }
      // check wav header
      else if (filePath.endsWith(".wav")){
         File file = new File(filePath);
         if (!file.exists() || !file.isFile()){
            return null;
         }
         byte[] fileArray = Audio.readFile2ByteArray(file);
         byte[] dataheader = Arrays.copyOfRange(fileArray, 0, 44);
         AudioHeader header =
               AudioHeader.getInstance(filePath,dataheader,
                        fileArray.length - 44);
         if (header == null){
            return null;
         }
      } else {
         if (Assignment8.DEBUG){
            System.err.println("ERROR: file not ends with audio format");
         }
         formatFile = null;
      }
      return formatFile;
      
   }

   // convert mp3 file to wav file
   private static String convertFromMp3ToWav(String filePath){
      String[] strs = filePath.split("/");
      String fileName = strs[strs.length-1];
      if (fileName.endsWith(".mp3")) {
         fileName = fileName.substring(0, fileName.indexOf("."));
      }
      String fileWav = "/tmp/assignment7Sanguoyanyi" + fileName + ".wav";
      String cmd = "./lame --decode " + filePath + " " + fileWav;
      Process p = null;
      try {
         p = java.lang.Runtime.getRuntime().exec(cmd);
      } catch (IOException e) {
         e.printStackTrace();
      }
      synchronized(p){
         try {
            p.wait(5000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }

      return fileWav;
   }

   // convert ogg to wav
   private static String convertFromOggToWav(String filePath){
      String[] strs = filePath.split("/");
      String fileName = strs[strs.length - 1];
      if (fileName.endsWith(".ogg") {
         fileName = fileName.substring(0, fileName.indexOf("."));
      }
      String fileWav = "/tmp/assignment7Sanguoyanyi" + fileName + ".wav";
      String cmd = "./user/bin/oggdec -o " + fileWav + " " + filePath;
      Process p = null;
      try {
         p = java.lang.Runtime.getRuntime().exec(cmd);
      } catch (IOException e) {
         e.printStackTrace();
      }
      synchronized(p){
         try {
            p.wait(5000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }

      return fileWav;
   }

}
