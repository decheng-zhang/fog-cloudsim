package org.cloudbus.cloudsim.examples.power.random;

import java.io.IOException;

public class fogTest1 {
	
	public static void main(String[] args) throws IOException {
		boolean enableOutput = true;
		boolean outputToFile = true;
		String inputFolder = "";
		String outputFolder = "output";
		String workload = "random"; // Random workload
		String vmAllocationPolicy = "dvfs"; // DVFS policy without VM migrations
		String vmSelectionPolicy = "";
		String parameter = "";

		new RandomRunner(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
	}


}
