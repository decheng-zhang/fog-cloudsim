package dehelper;

import java.io.File;
import java.util.ArrayList;
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
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;



public class FogRunner2 extends RunnerAbstract2{
	private static int iterVersion;
	KmeanFogletAllocator km=null;
	Map<Integer,List<Cloudlet>> bk2cls;
	
	public int getIterVersion() {
		return iterVersion;
	}
	
	private List<PowerDatacenter> datacenterList = null;
	public FogRunner2(
			boolean enableOutput,
			boolean outputToFile,
			String inputFolder,
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
				
				FogRunner2.iterVersion++;
				this.datacenterList = new ArrayList<PowerDatacenter>();
				CloudSim.init(iterVersion, Calendar.getInstance(), false);
				
				bk2cls = new HashMap<Integer,List<Cloudlet>>();
				cloudletList = FogHelper.createCloudletList(0, FogConst.NUMBER_OF_CLOUDLETS);
				km = new KmeanFogletAllocator(cloudletList,iterVersion);
				km.printClustererOutput();
				List<CentroidCluster<Point>> clusterResult = km.getResult();
				for(int i=0;i<iterVersion;i++) {
				FogBroker broker = FogHelper.createBroker("FogBroker-"+iterVersion + "_"+i);
				List<Cloudlet> localcllist = new ArrayList<Cloudlet>();
					for(Point point:clusterResult.get(i).getPoints()) {
						localcllist.add(point.getCloudlet());
					}
					bk2cls.put(broker.getId(),localcllist);
				}
				

				//System.exit(0);
				//vmList = Helper.createVmList(brokerId, iterVersion);
				
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
				
				for (int j = 0; j < iterVersion; j++) {
					
					Fog fog = (Fog) FogHelper2.createFog((double[])km.getResult().get(j).getCenter().getPoint(), j, PowerDatacenter.class, vmAllocationPolicy);
					
					PowerDatacenter datacenter =fog.getDatacenter();
					datacenter.setDisableMigrations(false);
					datacenterList.add(datacenter);
					
				}
				broker.submitVmList(vmList);
				broker.submitCloudletList(cloudletList);
				for(int i=0;i<km.getResult().size();i++) {
					for (Point point:km.getResult().get(i).getPoints())
						
					broker.bindCloudletToVm(point.getCloudletID(), i);
				}
				
				CloudSim.terminateSimulation(Constants.SIMULATION_LIMIT);
				double lastClock = CloudSim.startSimulation();

				List<Cloudlet> newList = broker.getCloudletReceivedList();
				Log.printLine("Received " + newList.size() + " cloudlets");

				CloudSim.stopSimulation();
				Log.printLine("=============> User "+broker.getId()+"    ");
				Helper.printCloudletList(newList);

			/*	Helper.printResults(
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
