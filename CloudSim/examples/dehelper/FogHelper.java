package dehelper;
/*
*
*/


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.cloudbus.cloudsim.Host;
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
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationLocalRegression;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationStaticThreshold;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumMigrationTime;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.util.MathUtil;

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
	static int VMINDEX = -1;

	public static Map<Integer, List<Vm>> getFogCenterToVmMapping() {
		
		return fogcenterToVm;
	}
	public static void setFogCenterToVmMapping(Map<Integer, List<Integer>> fogCenterToVmMapping) {
		FogHelper.fogCenterToVmMapping = fogCenterToVmMapping;
		fogcenterToVm = new HashMap<Integer, List<? extends Vm>>();
		for(Integer centerId: fogCenterToVmMapping.keySet()){
			List<Vm> tempVmList = new ArrayList<Vm>();	
			for(Integer vmid: fogCenterToVmMapping.get(centerId)) {
				tempVmList.add(VmList.getById(vms, vmid));
			}
			FogHelper.fogcenterToVm.put(centerId, tempVmList);
		
		}
		
	}
	public static void setFogCenterToVm(Map<Integer, List<Vm>> fogCenterToVmMapping) {
		FogHelper.fogCenterToVmMapping = null;
		fogcenterToVm = fogCenterToVmMapping;

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
		public static List<Cloudlet> createCloudletList(int brokerId,robinCloudletBrand opertor,String inputFolderName) {
			int cloudletsNumber=opertor.getCloudletNumber();
			double lm [][] = opertor.getLocationMatrix();
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
								FogConst.CLOUDLET_LENGTH,
								FogConst.CLOUDLET_PES,
								fileSize,
								outputSize,
								new UtilizationModelPlanetLabInMemory(
										files[i].getAbsolutePath(),
										FogConst.SCHEDULING_INTERVAL), utilizationModelNull, utilizationModelNull,
								false,
								lm[i][0],
								lm[i][1]);
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
				int hostType = i % FogConst.HOST_TYPES;

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
			
			List<PowerHost> hostList = createHostList(j,FogConst.HOST_TYPES);
			//VmAllocationPolicy vmAllocationPolicy = new PowerVmAllocationPolicySimple(hostList);
			PowerVmSelectionPolicy vmSelectionPolicy =new PowerVmSelectionPolicyMinimumMigrationTime();
			PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
					hostList,
					vmSelectionPolicy,
					0.7);
			vmAllocationPolicy = new PowerVmAllocationPolicyMigrationLocalRegression(
					hostList,
					vmSelectionPolicy,
					1.2,
					Constants.SCHEDULING_INTERVAL,
					fallbackVmSelectionPolicy);
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
						vmAllocationPolicy,
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


		public static Map<Integer,Vm> createVmList(int brokerId, List<Cloudlet> clList,cloudletBrandAbstract allocator) {
			
			Map<Integer,Vm> CloudletIdToVm = new HashMap<Integer,Vm>();
			
				 List<CloudletRes> clresSet = allocator.getCloudletResSet(clList,true);
				 
			
			for(CloudletRes clres :clresSet) {
				int vmType = clres.getVmType();
				
				clres.setVmId(++VMINDEX);
				
					PowerVm pv = new PowerVm(
								VMINDEX,
								brokerId,
								FogConst.VM_MIPS[vmType],
								FogConst.VM_PES[vmType],
								FogConst.VM_RAM[vmType],
								FogConst.VM_BW[vmType],
								FogConst.VM_SIZE,
								1,
								"Xen",
								new CloudletSchedulerDynamicWorkload(FogConst.VM_MIPS[vmType], FogConst.VM_PES[vmType]),
								
								FogConst.SCHEDULING_INTERVAL);
					String appname = clres.getAppType()==-1?"On-demand":FogConst.APPNAME[clres.getAppType()];
					pv.setAPP(appname);
					for(int i:clres.getCloudletId()) {
						CloudletIdToVm.put(i,pv);
					}	
			};
			
		
			return CloudletIdToVm;
		
		}
		

		public static List<Vm> createVmList(int brokerId, int vmsNumber) {
			vms = new ArrayList<Vm>();
			for (int i = 0; i < vmsNumber; i++) {
				int vmType = i / (int) Math.ceil((double) vmsNumber / FogConst.VM_TYPES);
				//vmType = 0;
				vms.add(new PowerVm(
						i,
						brokerId,
						FogConst.VM_MIPS[vmType],
						FogConst.VM_PES[vmType],
						FogConst.VM_RAM[vmType],
						FogConst.VM_BW,
						FogConst.VM_SIZE,
						1,
						"Xen",
						new CloudletSchedulerDynamicWorkload(FogConst.VM_MIPS[vmType], FogConst.VM_PES[vmType]),
						
						FogConst.SCHEDULING_INTERVAL));
						
						
			}
			return vms;
		}
		//public static List<>
		public static void printCloudletHistory(List<Cloudlet> cloudlets) {
			for(Cloudlet cloudlet:cloudlets) {
			Log.printLine(cloudlet.getCloudletHistory());
			}
		}
		
		public static void printFogResults(List<PowerDatacenter> datacenterList,double lastClock,String experimentName,String outputFolder) {
			File folder5 = new File(outputFolder + "/host_count");
			if (!folder5.exists()) {
				folder5.mkdir();
		}
			StringBuilder data = new StringBuilder();
			String delimeter = ",";
			for(PowerDatacenter datacenter:datacenterList) {
				data.append(Arrays.toString(datacenter.getHostCountHistory().toArray()).replaceAll("\\[|\\]",""));
				data.append("\n");
			}
			
			writeDataRow(data.toString(), outputFolder + "/host_count/" + experimentName + "_hscount.csv");
		}

		/**
		 * Prints the results.
		 * 
		 * @param datacenter the datacenter
		 * @param lastClock the last clock
		 * @param experimentName the experiment name
		 * @param outputInCsv the output in csv
		 * @param outputFolder the output folder
		 */
		public static void printResults(
				PowerDatacenter datacenter,
				List<Vm> vms,
				double lastClock,
				String experimentName,
				boolean outputInCsv,
				String outputFolder) {
			Log.enable();
			List<Host> hosts = datacenter.getHostList();

			int numberOfHosts = hosts.size();
			int numberOfVms = vms.size();

			double totalSimulationTime = lastClock;
			double energy = datacenter.getPower() / (3600 * 1000);
			int numberOfMigrations = datacenter.getMigrationCount();

			Map<String, Double> slaMetrics = getSlaMetrics(vms);

			double slaOverall = slaMetrics.get("overall");
			double slaAverage = slaMetrics.get("average");
			double slaDegradationDueToMigration = slaMetrics.get("underallocated_migration");
			// double slaTimePerVmWithMigration = slaMetrics.get("sla_time_per_vm_with_migration");
			// double slaTimePerVmWithoutMigration =
			// slaMetrics.get("sla_time_per_vm_without_migration");
			// double slaTimePerHost = getSlaTimePerHost(hosts);
			double slaTimePerActiveHost = getSlaTimePerActiveHost(hosts);

			double sla = slaTimePerActiveHost * slaDegradationDueToMigration;

			List<Double> timeBeforeHostShutdown = getTimesBeforeHostShutdown(hosts);

			int numberOfHostShutdowns = timeBeforeHostShutdown.size();

			double meanTimeBeforeHostShutdown = Double.NaN;
			double stDevTimeBeforeHostShutdown = Double.NaN;
			if (!timeBeforeHostShutdown.isEmpty()) {
				meanTimeBeforeHostShutdown = MathUtil.mean(timeBeforeHostShutdown);
				stDevTimeBeforeHostShutdown = MathUtil.stDev(timeBeforeHostShutdown);
			}

			List<Double> timeBeforeVmMigration = getTimesBeforeVmMigration(vms);
			double meanTimeBeforeVmMigration = Double.NaN;
			double stDevTimeBeforeVmMigration = Double.NaN;
			if (!timeBeforeVmMigration.isEmpty()) {
				meanTimeBeforeVmMigration = MathUtil.mean(timeBeforeVmMigration);
				stDevTimeBeforeVmMigration = MathUtil.stDev(timeBeforeVmMigration);
			}

			if (outputInCsv) {
				File folder = new File(outputFolder);
				if (!folder.exists()) {
					folder.mkdir();
				}
				File folder1 = new File(outputFolder + "/stats");
				if (!folder1.exists()) {
					folder1.mkdir();
				}
				File folder2 = new File(outputFolder + "/time_before_host_shutdown");
				if (!folder2.exists()) {
					folder2.mkdir();
				}
				File folder3 = new File(outputFolder + "/time_before_vm_migration");
				if (!folder3.exists()) {
					folder3.mkdir();
				}
				File folder4 = new File(outputFolder + "/metrics");
				if (!folder4.exists()) {
					folder4.mkdir();
				}
			
				StringBuilder data = new StringBuilder();
				String delimeter = ",";

				data.append(experimentName + delimeter);
				data.append(parseExperimentName(experimentName));
				data.append(String.format("%d", numberOfHosts) + delimeter);
				data.append(String.format("%d", numberOfVms) + delimeter);
				data.append(String.format("%.2f", totalSimulationTime) + delimeter);
				data.append(String.format("%.5f", energy) + delimeter);
				data.append(String.format("%d", numberOfMigrations) + delimeter);
				data.append(String.format("%.10f", sla) + delimeter);
				data.append(String.format("%.10f", slaTimePerActiveHost) + delimeter);
				data.append(String.format("%.10f", slaDegradationDueToMigration) + delimeter);
				data.append(String.format("%.10f", slaOverall) + delimeter);
				data.append(String.format("%.10f", slaAverage) + delimeter);
				// data.append(String.format("%.5f", slaTimePerVmWithMigration) + delimeter);
				// data.append(String.format("%.5f", slaTimePerVmWithoutMigration) + delimeter);
				// data.append(String.format("%.5f", slaTimePerHost) + delimeter);
				data.append(String.format("%d", numberOfHostShutdowns) + delimeter);
				data.append(String.format("%.2f", meanTimeBeforeHostShutdown) + delimeter);
				data.append(String.format("%.2f", stDevTimeBeforeHostShutdown) + delimeter);
				data.append(String.format("%.2f", meanTimeBeforeVmMigration) + delimeter);
				data.append(String.format("%.2f", stDevTimeBeforeVmMigration) + delimeter);

				if (datacenter.getVmAllocationPolicy() instanceof PowerVmAllocationPolicyMigrationAbstract) {
					PowerVmAllocationPolicyMigrationAbstract vmAllocationPolicy = (PowerVmAllocationPolicyMigrationAbstract) datacenter
							.getVmAllocationPolicy();

					double executionTimeVmSelectionMean = MathUtil.mean(vmAllocationPolicy
							.getExecutionTimeHistoryVmSelection());
					double executionTimeVmSelectionStDev = MathUtil.stDev(vmAllocationPolicy
							.getExecutionTimeHistoryVmSelection());
					double executionTimeHostSelectionMean = MathUtil.mean(vmAllocationPolicy
							.getExecutionTimeHistoryHostSelection());
					double executionTimeHostSelectionStDev = MathUtil.stDev(vmAllocationPolicy
							.getExecutionTimeHistoryHostSelection());
					double executionTimeVmReallocationMean = MathUtil.mean(vmAllocationPolicy
							.getExecutionTimeHistoryVmReallocation());
					double executionTimeVmReallocationStDev = MathUtil.stDev(vmAllocationPolicy
							.getExecutionTimeHistoryVmReallocation());
					double executionTimeTotalMean = MathUtil.mean(vmAllocationPolicy
							.getExecutionTimeHistoryTotal());
					double executionTimeTotalStDev = MathUtil.stDev(vmAllocationPolicy
							.getExecutionTimeHistoryTotal());

					data.append(String.format("%.5f", executionTimeVmSelectionMean) + delimeter);
					data.append(String.format("%.5f", executionTimeVmSelectionStDev) + delimeter);
					data.append(String.format("%.5f", executionTimeHostSelectionMean) + delimeter);
					data.append(String.format("%.5f", executionTimeHostSelectionStDev) + delimeter);
					data.append(String.format("%.5f", executionTimeVmReallocationMean) + delimeter);
					data.append(String.format("%.5f", executionTimeVmReallocationStDev) + delimeter);
					data.append(String.format("%.5f", executionTimeTotalMean) + delimeter);
					data.append(String.format("%.5f", executionTimeTotalStDev) + delimeter);

					writeMetricHistory(hosts, vmAllocationPolicy, outputFolder + "/metrics/" + experimentName
							+ "_metric");
				}

				data.append("\n");

				writeDataRow(data.toString(), outputFolder + "/stats/" + experimentName + "_stats.csv");
				writeDataColumn(timeBeforeHostShutdown, outputFolder + "/time_before_host_shutdown/"
						+ experimentName + "_time_before_host_shutdown.csv");
				writeDataColumn(timeBeforeVmMigration, outputFolder + "/time_before_vm_migration/"
						+ experimentName + "_time_before_vm_migration.csv");

			} /*else {
				Log.setDisabled(false);
				Log.printLine();
				Log.printLine(String.format("Experiment name: " + experimentName));
				Log.printLine(String.format("Number of hosts: " + numberOfHosts));
				Log.printLine(String.format("Number of VMs: " + numberOfVms));
				Log.printLine(String.format("Total simulation time: %.2f sec", totalSimulationTime));
				Log.printLine(String.format("Energy consumption: %.2f kWh", energy));
				Log.printLine(String.format("Number of VM migrations: %d", numberOfMigrations));
				Log.printLine(String.format("SLA: %.5f%%", sla * 100));
				Log.printLine(String.format(
						"SLA perf degradation due to migration: %.2f%%",
						slaDegradationDueToMigration * 100));
				Log.printLine(String.format("SLA time per active host: %.2f%%", slaTimePerActiveHost * 100));
				Log.printLine(String.format("Overall SLA violation: %.2f%%", slaOverall * 100));
				Log.printLine(String.format("Average SLA violation: %.2f%%", slaAverage * 100));
				// Log.printLine(String.format("SLA time per VM with migration: %.2f%%",
				// slaTimePerVmWithMigration * 100));
				// Log.printLine(String.format("SLA time per VM without migration: %.2f%%",
				// slaTimePerVmWithoutMigration * 100));
				// Log.printLine(String.format("SLA time per host: %.2f%%", slaTimePerHost * 100));
				Log.printLine(String.format("Number of host shutdowns: %d", numberOfHostShutdowns));
				Log.printLine(String.format(
						"Mean time before a host shutdown: %.2f sec",
						meanTimeBeforeHostShutdown));
				Log.printLine(String.format(
						"StDev time before a host shutdown: %.2f sec",
						stDevTimeBeforeHostShutdown));
				Log.printLine(String.format(
						"Mean time before a VM migration: %.2f sec",
						meanTimeBeforeVmMigration));
				Log.printLine(String.format(
						"StDev time before a VM migration: %.2f sec",
						stDevTimeBeforeVmMigration));

				if (datacenter.getVmAllocationPolicy() instanceof PowerVmAllocationPolicyMigrationAbstract) {
					PowerVmAllocationPolicyMigrationAbstract vmAllocationPolicy = (PowerVmAllocationPolicyMigrationAbstract) datacenter
							.getVmAllocationPolicy();

					double executionTimeVmSelectionMean = MathUtil.mean(vmAllocationPolicy
							.getExecutionTimeHistoryVmSelection());
					double executionTimeVmSelectionStDev = MathUtil.stDev(vmAllocationPolicy
							.getExecutionTimeHistoryVmSelection());
					double executionTimeHostSelectionMean = MathUtil.mean(vmAllocationPolicy
							.getExecutionTimeHistoryHostSelection());
					double executionTimeHostSelectionStDev = MathUtil.stDev(vmAllocationPolicy
							.getExecutionTimeHistoryHostSelection());
					double executionTimeVmReallocationMean = MathUtil.mean(vmAllocationPolicy
							.getExecutionTimeHistoryVmReallocation());
					double executionTimeVmReallocationStDev = MathUtil.stDev(vmAllocationPolicy
							.getExecutionTimeHistoryVmReallocation());
					double executionTimeTotalMean = MathUtil.mean(vmAllocationPolicy
							.getExecutionTimeHistoryTotal());
					double executionTimeTotalStDev = MathUtil.stDev(vmAllocationPolicy
							.getExecutionTimeHistoryTotal());

					Log.printLine(String.format(
							"Execution time - VM selection mean: %.5f sec",
							executionTimeVmSelectionMean));
					Log.printLine(String.format(
							"Execution time - VM selection stDev: %.5f sec",
							executionTimeVmSelectionStDev));
					Log.printLine(String.format(
							"Execution time - host selection mean: %.5f sec",
							executionTimeHostSelectionMean));
					Log.printLine(String.format(
							"Execution time - host selection stDev: %.5f sec",
							executionTimeHostSelectionStDev));
					Log.printLine(String.format(
							"Execution time - VM reallocation mean: %.5f sec",
							executionTimeVmReallocationMean));
					Log.printLine(String.format(
							"Execution time - VM reallocation stDev: %.5f sec",
							executionTimeVmReallocationStDev));
					Log.printLine(String.format("Execution time - total mean: %.5f sec", executionTimeTotalMean));
					Log.printLine(String
							.format("Execution time - total stDev: %.5f sec", executionTimeTotalStDev));
				}
				Log.printLine();
			}*/

			Log.setDisabled(true);
		}
		
		/**
		 * Write metric history.
		 * 
		 * @param hosts the hosts
		 * @param vmAllocationPolicy the vm allocation policy
		 * @param outputPath the output path
		 */
		public static void writeMetricHistory(
				List<? extends Host> hosts,
				PowerVmAllocationPolicyMigrationAbstract vmAllocationPolicy,
				String outputPath) {
			// for (Host host : hosts) {
			for (int j = 0; j < 10; j++) {
				Host host = hosts.get(j);

				if (!vmAllocationPolicy.getTimeHistory().containsKey(host.getId())) {
					continue;
				}
				File file = new File(outputPath + "_" + host.getId() + ".csv");
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.exit(0);
				}
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					List<Double> timeData = vmAllocationPolicy.getTimeHistory().get(host.getId());
					List<Double> utilizationData = vmAllocationPolicy.getUtilizationHistory().get(host.getId());
					List<Double> metricData = vmAllocationPolicy.getMetricHistory().get(host.getId());

					for (int i = 0; i < timeData.size(); i++) {
						writer.write(String.format(
								"%.2f,%.2f,%.2f\n",
								timeData.get(i),
								utilizationData.get(i),
								metricData.get(i)));
					}
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
		
		/**
		 * Write data column.
		 * 
		 * @param data the data
		 * @param outputPath the output path
		 */
		public static void writeDataColumn(List<? extends Number> data, String outputPath) {
			File file = new File(outputPath);
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for (Number value : data) {
					writer.write(value.toString() + "\n");
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		/**
		 * Write data row.
		 * 
		 * @param data the data
		 * @param outputPath the output path
		 */
		public static void writeDataRow(String data, String outputPath) {
			File file = new File(outputPath);
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(data);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

}
