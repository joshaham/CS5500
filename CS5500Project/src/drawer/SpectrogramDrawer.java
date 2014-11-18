package drawer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpectrogramDrawer {
    public static void main(String[] args) { 
    	BufferedImage  img =new BufferedImage(255,255,BufferedImage.TYPE_INT_BGR);
    	for(int i=0;i<255;i++){
    		for(int j=1;j<255;j++){
	    			int col=i<<16|j<<8;
	    			img.setRGB(i, j, col);
    		}
    	}
    	File f = new File("Spectrogram.png");
    	try {
			ImageIO.write(img, "PNG", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    
//    public static void drawSpectrogram(String fileName,int[][] data){
//    	int interval=1;
//    	System.out.println("Height: "+data.length/interval);
//    	System.out.println("Width: "+data[0].length);
//
//
//        
//    	for(int i =0;i<data.length;i+=interval){
//    		for(int j=0;j<data[0].length;j++){
//    			int color = data[i][j];
//    			System.out.format("%4d ",color);
//    		}
//    		System.out.println();
//    	}
//    }
    
    public static void drawSpectrogram(String fileName,int[][] data){
    	if(fileName.contains(".")){
    		fileName=fileName.substring(0,fileName.indexOf('.'));
    	}
    	int frequencyRange=400;
    	int audioLength=data[0].length;
    	System.out.println("Height: "+frequencyRange+", width:"+audioLength);
    	BufferedImage  img =new BufferedImage(audioLength,frequencyRange,BufferedImage.TYPE_INT_BGR);
    	for(int i=0;i<audioLength;i++){
    		for(int j=0;j<frequencyRange;j++){
    			int col=data[j][i];
    		
//    			col=16711680;
    			//  RED GREEN BLUE
    			img.setRGB(i, j,col);
    		}
    	}
    	File f = new File(fileName+"Spectrogram.png");
    	try {
			ImageIO.write(img, "PNG", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
