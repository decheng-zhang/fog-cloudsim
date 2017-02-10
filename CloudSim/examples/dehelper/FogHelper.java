package dehelper;
/*
*
*/


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelPlanetLabInMemory;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
* The Helper class for the random workload.
* 
* If you are using any algorithms, policies or workload included in the power package please cite
* the following paper:
* 
* Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
* Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
* Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
* Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
* 
* @author Anton Beloglazov
* @since Jan 5, 2012
*/


public class FogHelper {

	static List<CentroidCluster<Point>> clusterResult;
	static List<Cloudlet> cloudlets;
	static List<Vm> vms;
	static Map<Integer,List<Integer>> fogCenterToVmMapping;
	static Map<Integer,List<Vm>> fogcenterToVm;
	

	public static Map<Integer, List<Vm>> getFogCenterToVmMapping() {
		
		return fogcenterToVm;
	}
	public static void setFogCenterToVmMapping(Map<Integer, List<Integer>> fogCenterToVmMapping) {
		FogHelper.fogCenterToVmMapping = fogCenterToVmMapping;
		fogcenterToVm = new HashMap<Integer, List<Vm>>();
		for(Integer centerId: fogCenterToVmMapping.keySet()){
			List<Vm> tempVmList = new ArrayList<Vm>();	
			for(Integer vmid: fogCenterToVmMapping.get(centerId)) {
				tempVmList.add(VmList.getById(vms, vmid));
			}
			FogHelper.fogcenterToVm.put(centerId, tempVmList);
		
		}
		
	}
	public static void setClusterResult(List<CentroidCluster<Point>> rr) {
		FogHelper.clusterResult=rr ;
		
	}
	public static List<CentroidCluster<Point>> getClusterResult(){
		return clusterResult;
	}
	
	public static DatacenterBroker createBroker(String name) {
		DatacenterBroker broker = null;
		try {
			broker = new FogBroker(name);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return broker;
	}

	/**
		 * Creates the cloudlet list.
		 * 
		 * @param brokerId the broker id
		 * @param cloudletsNumber the cloudlets number
		 * 
		 * @return the list< cloudlet>
		 */
		public static List<Cloudlet> createCloudletList(int brokerId,int cloudletsNumber,String inputFolderName) {
			if (cloudlets ==null) {
				
			
				cloudlets = new ArrayList<Cloudlet>();
				long fileSize = 300;
				long outputSize = 300;
				//long seed = FogConst.CLOUDLET_UTILIZATION_SEED;
				UtilizationModel utilizationModelNull = new UtilizationModelNull();
				File inputFolder = new File(inputFolderName);
				File[] files = inputFolder.listFiles();
				for (int i = 0; i < cloudletsNumber; i++) {
					Cloudlet cloudlet = null;
/*					if (seed == -1) {
						cloudlet = new Cloudlet(
								i,
								FogConst.CLOUDLET_LENGTH,
								FogConst.CLOUDLET_PES,
								fileSize,
								outputSize,
								new UtilizationModelStochastic(),
								utilizationModelNull,
								utilizationModelNull);
					} else {
						cloudlet = new Cloudlet(
								i,
								200* (i+1)* (int)FogConst.SIMULATION_LIMIT,
								FogConst.CLOUDLET_PES,
								fileSize,
								outputSize,
								new UtilizationModelStochastic(seed * i),
								utilizationModelNull,
								utilizationModelNull);
					}*/
					try {
						cloudlet = new Cloudlet(
								i,
								Constants.CLOUDLET_LENGTH,
								Constants.CLOUDLET_PES,
								fileSize,
								outputSize,
								new UtilizationModelPlanetLabInMemory(
										files[i].getAbsolutePath(),
										FogConst.SCHEDULING_INTERVAL), utilizationModelNull, utilizationModelNull);
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0);
					}
					cloudlet.setUserId(brokerId);
					cloudlet.setVmId(i);

					cloudlets.add(cloudlet);
									}
			} else {
				for(Cloudlet cloudlet:cloudlets) {
					
					cloudlet.setUserId(brokerId);
					cloudlet.setCloudletFinishedSoFar(0);
				}
			}

			return cloudlets;
		}

		public static List<PowerHost> createHostList(int fogindex,int hostsNumber) {
			List<PowerHost> hostList = new ArrayList<PowerHost>();
			for (int i = 0; i < hostsNumber; i++) {
				int hostType = 1;

				List<Pe> peList = new ArrayList<Pe>();
				for (int j = 0; j < FogConst.HOST_PES[hostType]; j++) {
					peList.add(new Pe(j, new PeProvisionerSimple(FogConst.HOST_MIPS[hostType])));
				}

				hostList.add(new PowerHostUtilizationHistory(
						fogindex*10000+i,
						new RamProvisionerSimple(FogConst.HOST_RAM[hostType]),
						new BwProvisionerSimple(FogConst.HOST_BW),
						FogConst.HOST_STORAGE,
						peList,
						new VmSchedulerTimeSharedOverSubscription(peList),
						FogConst.HOST_POWER[hostType]));
			}
			return hostList;

		}

		public static Fog createFog(double[] coor,
				int j,
				Class<? extends Datacenter> datacenterClass,VmAllocationPolicy vmAllocationPolicy) throws Exception {
			
			List<PowerHost> hostList = createHostList(j,FogConst.NUMBER_OF_HOSTS);
			//VmAllocationPolicy vmAllocationPolicy = new PowerVmAllocationPolicySimple(hostList);
			String arch = "x86"; // system architecture
			String os = "Linux"; // operating system
			String vmm = "Xen";
			double time_zone = 10.0; // time zone this resource located
			double cost = 3.0; // the cost of using processing in this resource
			double costPerMem = 0.05; // the cost of using memory in this resource
			double costPerStorage = 0.001; // the cost of using storage in this resource
			double costPerBw = 0.0; // the cost of using bw in this resource
			String name = "Fog-"+ j;
			DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
					arch,
					os,
					vmm,
					hostList,
					time_zone,
					cost,
					costPerMem,
					costPerStorage,
					costPerBw);

			Datacenter datacenter = null;
			try {
				
				datacenter = datacenterClass.getConstructor(
						String.class,
						DatacenterCharacteristics.class,
						VmAllocationPolicy.class,
						List.class,
						Double.TYPE,
						Double.TYPE,
						Double.TYPE
						).newInstance(
						name+"-datacenter",
						characteristics,
						new PowerVmAllocationPolicySimple(hostList),
						new LinkedList<Storage>(),
						Constants.SCHEDULING_INTERVAL,
						coor[0],
						coor[1]
						);
				//System.out.print("fogcenter"+ datacenter.getId() + "is being created at "+ ((PowerDatacenter) datacenter).getPosition()[0]);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			
			Fog fog = new Fog(j,coor,name,(PowerDatacenter) datacenter);
			return fog;
		}


		
		public static List<Vm> createVmList(int brokerId, int vmsNumber) {
			vms = new ArrayList<Vm>();
			for (int i = 0; i < vmsNumber; i++) {
				int vmType = i / (int) Math.ceil((double) vmsNumber / Constants.VM_TYPES);
				vmType = 0;
				vms.add(new PowerVm(
						i,
						brokerId,
						Constants.VM_MIPS[vmType],
						Constants.VM_PES[vmType],
						Constants.VM_RAM[vmType],
						Constants.VM_BW,
						Constants.VM_SIZE,
						1,
						"Xen",
						new CloudletSchedulerDynamicWorkload(Constants.VM_MIPS[vmType], Constants.VM_PES[vmType]),
						
						Constants.SCHEDULING_INTERVAL));
						
						
			}
			return vms;
		}
		public static void printCloudletHistory(List<Cloudlet> cloudlets) {
			for(Cloudlet cloudlet:cloudlets) {
			Log.printLine(cloudlet.getCloudletHistory());
			}
		}

}
