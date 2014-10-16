package drawer;


import javax.swing.*;

import org.math.plot.*;

import comparator.Audio;
 

/**
 * using third party library: jmathplot.jar 
 * scientific graph drawer
 * @author zhuoli
 *
 */
public class JmathplotLineGraph {	// TEST
	public static void main(String[] args) {	
		if (args.length!=2){
			System.err.println("Wrong input format: &java JmathplotLineGraph -draw AUDIOFILE");
			System.exit(1);
		}
		JmathplotLineGraph obj = new JmathplotLineGraph();
		Audio file=new Audio(args[1]);
		obj.DrawTimeZone(file);
	}
	
	public void DrawFrequency(Audio file){
		double[] y=file.getFrequencies();
		double[] x=new double[y.length];
		for(int i=0;i<x.length;i++){
			x[i]=i;
		}
		this.plot2d(x, y, "F(w)", "w",file.getFileName()+" Frequency Domain");
	}
	public void DrawTimeZone(Audio file){
		double[] y=file.getTimeZoneData();
		double[] x=new double[y.length];
		for(int i=0;i<x.length;i++){
			x[i]=i;
		}
		this.plot2d(x, y, "Amplitude", "t",file.getFileName()+" Time Domain");
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
        plot.addLinePlot("title", x, y);

        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("title");
        frame.setSize(1200, 1200);
        frame.setContentPane(plot);
        frame.setVisible(true);

	}

}