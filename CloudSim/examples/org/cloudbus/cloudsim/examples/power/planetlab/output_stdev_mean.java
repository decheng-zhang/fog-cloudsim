package org.cloudbus.cloudsim.examples.power.planetlab;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class output_stdev_mean {

	public static void output(String inputFolderName) throws NumberFormatException, IOException {
		File inputFolder = new File(inputFolderName);
		File[] files = inputFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			double [] data = new double[289];
			BufferedReader input = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
			String fileName = files[i].getName();
			int n = data.length;
				for (int j = 0; j < n-1; j++) {
					data[j] = Integer.valueOf(input.readLine()) ;
				}
				data[n - 1] = data[n - 2];
				DescriptiveStatistics DS = new DescriptiveStatistics(data);
				
				System.out.println(fileName + " Mean is" + DS.getMean() + " StandardDeviation is " + DS.getStandardDeviation());
				input.close();
			}
		
	}
	public static double verifyMean(double []data) {
		double sum = 0;
		for(int i=0;i<data.length;i++) {
			sum+=data[i];
		}
		return sum;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		output(output_stdev_mean.class.getClassLoader().getResource("workload/planetlab").getPath()+ "/20110303");
	}

}
