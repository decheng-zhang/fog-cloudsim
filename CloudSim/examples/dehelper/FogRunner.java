package dehelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.NetworkTopology;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.examples.power.Helper;
import org.cloudbus.cloudsim.examples.power.random.RandomConstants;
import org.cloudbus.cloudsim.examples.power.random.RandomHelper;
import org.cloudbus.cloudsim.lists.CloudletList;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;



public class FogRunner extends RunnerAbstract{
	private static int iterVersion;
	KmeanFogletAllocator km=null;
	
	public int getIterVersion() {
		return iterVersion;
	}
	
	private List<PowerDatacenter> datacenterList = null;
	public FogRunner(
			boolean enableOutput,
			boolean outputToFile,
			String inputFolder,
			//TODO Adding workload input
			String outputFolder,
			String workload,
			String vmAllocationPolicy,
			String vmSelectionPolicy,
			String parameter,
			int iter) {
		super(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder+File.separator+ (iter+1),
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
		
		
	}
		@Override
		protected void init(String inputFolder) {
			try {
				
				FogRunner.iterVersion++;
				this.datacenterList = new ArrayList<PowerDatacenter>();
				CloudSim.init(1, Calendar.getInstance(), false);
				
				FogRunner.broker = FogHelper.createBroker("FogBroker-"+iterVersion);
				int brokerId = broker.getId();
				cloudletList = FogHelper.createCloudletList(brokerId, FogConst.NUMBER_OF_CLOUDLETS,inputFolder);
				km = new KmeanFogletAllocator(cloudletList,iterVersion);
				km.printClustererOutput();
				List<CentroidCluster<Point>> clusterResult = km.getResult();
				FogHelper.setClusterResult(clusterResult);
				//System.exit(0);
				vmList = FogHelper.createVmList(brokerId, FogConst.NUMBER_OF_CLOUDLETS);
				
				//hostList = RandomHelper.createHostList(RandomConstants.NUMBER_OF_HOSTS);
			} catch (Exception e) {
				e.printStackTrace();
				Log.printLine("The simulation has been terminated due to an unexpected error");
				System.exit(0);
			}
		}
		@Override
		protected void start(String experimentName, String outputFolder, VmAllocationPolicy vmAllocationPolicy) {
			System.out.println("Starting " + experimentName);

			try {

/*				NetworkTopology.buildNetworkTopology("examples\\org\\cloudbus\\cloudsim\\examples\\network\\topology.brite");
				
				//maps CloudSim entities to BRITE entities
				//Datacenter0 will correspond to BRITE node 0
				

				//Datacenter will correspond to BRITE node 0
				int briteNode=0;
				NetworkTopology.mapNode(datacenter.getId(),briteNode);

				
				
				//Broker will correspond to BRITE node 2
				briteNode=2;
				NetworkTopology.mapNode(broker.getId(),briteNode);
				Log.printLine("Showing network-topology");
				Log.print(NetworkTopology.printOutGraph());
				*/
				Map<Integer,List<Integer>> fogCenterToVmMapping = new HashMap<Integer,List<Integer>>();
				double totalMeanDistance=0;
				double totalNetworkBuildingCost = 0;
				double fogCoorlist[][] = new double [iterVersion][2];
				for (int j = 0; j < iterVersion; j++) {
					double [] localcoor =FogHelper.getClusterResult().get(j).getCenter().getPoint();
					fogCoorlist[j]= localcoor;
					Fog fog = (Fog) FogHelper.createFog((double[]) FogHelper.getClusterResult().get(j).getCenter().getPoint(), j, PowerDatacenter.class, vmAllocationPolicy);
					
					PowerDatacenter datacenter =fog.getDatacenter();
					
					datacenter.setDisableMigrations(true);
					datacenterList.add(datacenter);
					int datacenterId = datacenter.getId();
					List<Integer> cloudletIdList = new ArrayList<Integer>();
					List<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
					for(Point pt: FogHelper.getClusterResult().get(j).getPoints()) {
						cloudletIdList.add(pt.getCloudletID());
						cloudletList.add(pt.getCloudlet());
					}
						fogCenterToVmMapping.put(datacenterId, cloudletIdList);
						totalMeanDistance+=(CloudletList.getMeanDistance(cloudletList,localcoor));
						double subaddtive  = 0;
						for(int i=0;i<j;i++) {
						subaddtive +=Math.sqrt(Math.pow((fogCoorlist[i][0]-fogCoorlist[j][0]),2)+Math.pow((fogCoorlist[i][1]-fogCoorlist[j][1]), 2)); }
						totalNetworkBuildingCost +=subaddtive;
						
				};
				 	
					System.out.print("The average distance from cloudlets to nearest fog server is " +  totalMeanDistance/iterVersion);
					System.out.print("The total network need to build " +  totalNetworkBuildingCost);
				FogHelper.setFogCenterToVmMapping(fogCenterToVmMapping);
				broker.submitVmList(vmList);
				broker.submitCloudletList(cloudletList);
				//for(int i=0;i<km.getResult().size();i++) {
					//for (Point point:km.getResult().get(i).getPoints())
					for(int i=0;i< cloudletList.size();i++) 
					{
						broker.bindCloudletToVm(i, i);
					}
						//}
				
				CloudSim.terminateSimulation(2000);
				double lastClock = CloudSim.startSimulation();

				List<Cloudlet> newList = broker.getCloudletReceivedList();
				Log.printLine("Received " + newList.size() + " cloudlets");
				
				CloudSim.stopSimulation();
	
				Log.printLine("=============> User "+broker.getId()+"    ");
				Helper.printCloudletList(newList);
				FogHelper.printFogResults(datacenterList,lastClock,experimentName,outputFolder);
				//FogHelper.printCloudletHistory(newList);
				/*FogHelper.printResults(
						datacenter,
						vmList,
						lastClock,
						experimentName,
						Constants.OUTPUT_CSV,
						outputFolder);*/

			} catch (Exception e) {
				e.printStackTrace();
				Log.printLine("The simulation has been terminated due to an unexpected error");
				System.exit(0);
			}

			Log.printLine("Finished " + experimentName);
		}
		
		
	}
