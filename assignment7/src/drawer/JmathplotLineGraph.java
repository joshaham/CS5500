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
	static String audoPath="A5/D1/bad0616.wav";
	public static void main(String[] args) {	
//		if (args.length!=2){
//			System.err.println("Wrong input format: &java JmathplotLineGraph -draw AUDIOFILE");
//			System.exit(1);
//		}
		JmathplotLineGraph obj = new JmathplotLineGraph();
		Audio file=null;
		try {
			file = Audio.getInstance(audoPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj.DrawTimeZone(file);
		//obj.DrawFrequency(file);
	}
	
	public void DrawFrequency(Audio file){	
		int samplerate=(int)file.getSampleRate();	
		int n=(file.getFrequenciesData().length)/(samplerate*100);
		double[] y=new double[n];
		for(int i=0;i<n;i++){
			y[i]=file.getFrequenciesData()[i*samplerate*100];
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
		int n=(file.getTimeZoneData().length)/(samplerate/10);
		double[] y=new double[n];
		for(int i=0;i<n;i++){
			y[i]=file.getTimeZoneData()[i*samplerate/10];
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
