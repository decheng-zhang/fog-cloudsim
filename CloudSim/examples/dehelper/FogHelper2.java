package dehelper;
/*
*
*/


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.examples.power.Constants;
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


public class FogHelper2 {

	
	static List<Cloudlet> cloudlets;
	public static DatacenterBroker createBroker(String name) {
		DatacenterBroker broker = null;
		try {
			broker = new PowerDatacenterBroker(name);
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
		public static List<Cloudlet> createCloudletList(int brokerId,int cloudletsNumber) {
			if (cloudlets ==null) {
				
			
				cloudlets = new ArrayList<Cloudlet>();
				long fileSize = 300;
				long outputSize = 300;
				long seed = FogConst.CLOUDLET_UTILIZATION_SEED;
				UtilizationModel utilizationModelNull = new UtilizationModelNull();

				for (int i = 0; i < cloudletsNumber; i++) {
					Cloudlet cloudlet = null;
					if (seed == -1) {
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
								FogConst.CLOUDLET_LENGTH,
								FogConst.CLOUDLET_PES,
								fileSize,
								outputSize,
								new UtilizationModelStochastic(seed * i),
								utilizationModelNull,
								utilizationModelNull);
					}
					cloudlet.setUserId(brokerId);
					cloudlet.setVmId(i % FogConst.NUMBER_OF_VMS);

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
			
			List<PowerHost> hostList = createHostList(j,4);
			//VmAllocationPolicy vmAllocationPolicy = new PowerVmAllocationPolicySimple(hostList);
			String arch = "x86"; // system architecture
			String os = "Linux"; // operating system
			String vmm = "Xen";
			double time_zone = 10.0; // time zone this resource located
			double cost = 3.0; // the cost of using processing in this resource
			double costPerMem = 0.05; // the cost of using memory in this resource
			double costPerStorage = 0.001; // the cost of using storage in this resource
			double costPerBw = 0.0; // the cost of using bw in this resource
			String name = "Fog"+ j;
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
						Double.TYPE).newInstance(
						name,
						characteristics,
						new PowerVmAllocationPolicySimple(hostList),
						new LinkedList<Storage>(),
						Constants.SCHEDULING_INTERVAL);
				
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			
			Fog fog = new Fog(coor,name,(PowerDatacenter) datacenter);
			return fog;
		}


}
