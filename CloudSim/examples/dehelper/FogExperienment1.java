package dehelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.planetlab.Dvfs;
import org.cloudbus.cloudsim.examples.power.random.RandomRunner;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


public class FogExperienment1 {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Log.printLine("fog first experiment launching...");
		//private static List<Cloudlet> cloudletList =null;
		try {
			
			
			/*
			FogHelper.createFogs();*/
			
			
			//KmeanFogletAllocator km = new KmeanFogletAllocator(newList,3);
			//printCloudletList(newList);
			boolean enableOutput = true;
			boolean outputToFile = true;
			String inputFolder = Dvfs.class.getClassLoader().getResource("workload/planetlab").getPath();
			String outputFolder = "outputfogt1";
			String workload = "20110303"; // Random workload
			String vmAllocationPolicy = "lr"; // DVFS policy without VM migrations
			String vmSelectionPolicy = "mmt";
			String parameter = "1.2";
			for(int j=0;j<3;j++){
			
			new FogRunner(
					enableOutput,
					outputToFile,
					inputFolder,
					outputFolder,
					workload,
					vmAllocationPolicy,
					vmSelectionPolicy,
					parameter,j );
			System.out.println("Finishing " + j+ " iteration");
		
			}
			Log.printLine("fog-exper-1 finished!");
			}catch(Exception e) {
				e.printStackTrace();
				System.exit(0);
			
			}

	}
}