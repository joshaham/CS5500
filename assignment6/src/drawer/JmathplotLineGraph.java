package drawer;


import java.io.IOException;

import javax.swing.*;

import org.math.plot.*;

import audio.Audio;
 

/**
 * using third party library: jmathplot.jar 
 * scientific graph drawer
 * @author zhuoli
 *
 */
public class JmathplotLineGraph {	// TEST
	static String audioPath="A5/D1/z07.wav";
	
	public static void main(String[] args) {	
//		drawFrequency(audioPath);
		drawTimeZone(audioPath);
		
	}
	
	public static void drawFrequency(String path){

		JmathplotLineGraph obj = new JmathplotLineGraph();
		Audio file=null;
		try {
			file = Audio.getInstance(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj.DrawFrequency(file);
	}
	public static void drawTimeZone(String path){
		JmathplotLineGraph obj = new JmathplotLineGraph();
		Audio file=null;
		try {
			file = Audio.getInstance(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj.DrawTimeZone(file);
		
	}
	
	public void DrawFrequency(Audio file){			
		System.out.println(file);
		int indexFor20Hz = (int)(Math.floor(20 * 
				file.getFrequenciesData().length / file.getSampleRate()));
		int indexFor20000Hz = (int)(Math.ceil(20000 * 
				file.getFrequenciesData().length / file.getSampleRate()));
		double[] y=new double[indexFor20000Hz-indexFor20Hz+1];
		for(int i=0;i<indexFor20000Hz-indexFor20Hz;i++){
			y[i]=file.getFrequenciesData()[i+indexFor20000Hz];
		}
		double[] x=new double[y.length];
		for(int i=0;i<x.length;i++){
			x[i]=i;
		}
		this.plot2d(x, y, "w(HZ)", "F(w)",file.getFileName()+" Frequency Domain");
	}
	// 10 nodes per second
	public void DrawTimeZone(Audio file){
		if(file==null){
			return;
		}
		int samplerate=(int)file.getSampleRate();
		int n=(file.getdualChannelSamples().length)/(samplerate/10);
		double[] y=new double[n];
		for(int i=0;i<n;i++){
			y[i]=file.getdualChannelSamples()[i*samplerate/10];
		}
		double[] x=new double[y.length];
		for(int i=0;i<x.length;i++){
			x[i]=i*0.1;
		}
		System.out.println(file);
		System.out.println("X samples: "+ x.length+'\n'+"y samples: "+y.length);
		this.plot2d(x, y, "t(second)", "Amplitude",file.getFileName()+" Time Domain");
	}
	
	public void plot2d(double[] x,double[] y,String xaxisLabel,String yaxisLabel,String title) {


        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();

        // define the legend position
        plot.addLegend("NORTH");
        // change name of axes
        plot.setAxisLabel(0, xaxisLabel);
        plot.setAxisLabel(1, yaxisLabel);

        // add a line plot to the PlotPanel
        plot.addLinePlot(title, x, y);

        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("title");
        frame.setSize(1200, 900);
        frame.setContentPane(plot);
        frame.setVisible(true);

	}

}
