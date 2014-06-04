package pingAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import pingAnalyzer.Drawer;

public class PingAnalyzer {

	private static final String FILE_NAME = "./data/data.txt";
	
	private static float[] sampleRTT = new float[100];
	private static float[] estimatedRTT = new float[100];
	private static float[] devRTT = new float[100];
	private static float[] timeoutInterval = new float[100];
	
	private static float[] rawData = new float[101];
	
	public static void main(String[] args) {
		getRawData(FILE_NAME);
		getData();
		for(int i = 0; i < 100; i++){
			System.out.println(timeoutInterval[i]);
		}
		float[][] data = new float[3][100];
		data[0] = sampleRTT;
		data[1] = estimatedRTT;
		data[2] = timeoutInterval;
		
		Drawer d = new Drawer(data);
		d.showGraph();
	}
	
	private static void getRawData(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {            
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            //read one line each time
            while ((tempString = reader.readLine()) != null) {               
            	int beginIndex = tempString.indexOf("time=") + 5;
            	int endIndex = tempString.indexOf("TTL") - 3;
            	int time = Integer.parseInt(tempString.substring(beginIndex, endIndex));  
            	rawData[line-1] = (float)time;            	
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	
  	private static void getData(){
  		float tempEstimatedRTT = rawData[0];
  		float tempDevRTT = 0f;
  		float tempTimeoutInterval = tempEstimatedRTT+ 4 * tempDevRTT;
  		
  		for(int i = 1; i < 101; i ++){
  			float tempSampleRTT = rawData[i];
  			sampleRTT[i-1] = tempSampleRTT;
  			
  			float x = new Float(0.875);
  			float y = new Float(0.125);
  			tempEstimatedRTT = x*tempEstimatedRTT + y*tempSampleRTT;			
  			estimatedRTT[i-1] = tempEstimatedRTT;
  			
  			float p = new Float(0.75);
  			float q = new Float(0.25);
  			tempDevRTT = p*tempDevRTT + q*Math.abs(tempEstimatedRTT-tempSampleRTT);
  			devRTT[i-1] = tempDevRTT;
  			
  			tempTimeoutInterval = tempEstimatedRTT+ 4 * tempDevRTT;
  			timeoutInterval[i-1] = tempTimeoutInterval;
  		}
  	}
}
