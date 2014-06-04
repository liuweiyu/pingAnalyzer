package pingAnalyzer;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class Drawer {
	private XYSeriesCollection[] datasets = new XYSeriesCollection[3];

    public Drawer(float[][] dataArray) {
        datasets[0] = new XYSeriesCollection();
        XYSeries data0 = addData(dataArray[0], "Sample RTT");
        datasets[0].addSeries(data0);
        
        datasets[1] = new XYSeriesCollection();
        XYSeries data1 = addData(dataArray[1], "Estimated RTT");
        datasets[1].addSeries(data1);
        
        datasets[2] = new XYSeriesCollection();
        XYSeries data2 = addData(dataArray[2], "Timeout Interval");
        datasets[2].addSeries(data2);
    }

    //add the burst size related data into the dataset to be displayed
    private XYSeries addData(float[] array, String dataName){
    	XYSeries data = new XYSeries(dataName);    	
    	
    	for(int i = 0; i < 100; i++){    		
    		int index = i + 1;
    		Float value = array[i];
    		data.add(index, value);
    	}
    	
    	return data;
    }
    
    //draw the graph
    public void showGraph() {
        final JFreeChart chart = createChart(datasets[0]);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        final ApplicationFrame frame = new ApplicationFrame("Sample RTT, Estimated RTT and "
        		+ "Timeout Interval via Packet Sequence Number Plot Graph");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    //create a chart with basic information
	private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createScatterPlot(
        	"Sample RTT, Estimated RTT and Timeout Interval "
        	+ "via Packet Sequence Number Plot Graph",                  // chart title
            "Packet Sequence Number",                      // x axis label
            "Time(ms)",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );
        
        XYPlot plot = (XYPlot) chart.getPlot();
        
        plot.setDataset(0, datasets[0]);
        plot.setDataset(1, datasets[1]);
        plot.setDataset(2, datasets[2]);
        
        XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        
        renderer0.setSeriesLinesVisible(0, true);
        renderer1.setSeriesLinesVisible(1, true);
        renderer0.setSeriesLinesVisible(2, true);
        
        plot.setRenderer(0, renderer0);
        plot.setRenderer(1, renderer1);
        plot.setRenderer(2, renderer2);
        
        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.GREEN);
        plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(1, Color.BLUE);
        plot.getRendererForDataset(plot.getDataset(2)).setSeriesPaint(2, Color.RED);
        
        return chart;
    }
}
