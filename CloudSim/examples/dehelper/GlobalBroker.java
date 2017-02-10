package dehelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;


public class GlobalBroker extends SimEntity {

	private static final int CREATE_BROKER = 0;
	private static final int CREATE_CLOUDLETS = 4;
	private static final int CREATE_EXPERIENMENT = 3;
	private static final int ADDING_FOG = 1;
	private static final int REDEPLOY = 2;
	private static int INDEXBASE = 10000;
	private Map<Integer,List<Vm>> vmCollection;
	private final List<Cloudlet> cloudletList;
	private List<DatacenterBroker> brokerList;
	private List<Datacenter> datacenterList;
	private List<Fog> fogList;
	private static int iterationVersion = 1;
	
	public GlobalBroker(String name,List<Cloudlet> cloudlets) {
		super(name);		
		Log.printLine("Experienment Version" + iterationVersion + "is starting");
		cloudletList =  cloudlets;
		this.vmCollection = new HashMap<Integer,List<Vm>>();
	}
	@Override
	public void processEvent(SimEvent ev) {
		
		switch (ev.getTag()) {
		case CREATE_EXPERIENMENT:
			
			
			List<Vm> vmList = new ArrayList<Vm>();;
			
			DatacenterBroker tmp_broker = FogHelper.createBroker(iterationVersion);
			addBroker(tmp_broker);
			this.fogList = createFogs(iterationVersion, vmList,tmp_broker.getId());
			getVmColletion().put(iterationVersion, vmList);
			//Create VMs and Cloudlets and send them to broker
			//setVmList(CloudSimExample8.createVM(tmp_broker.getId(), 5, INDEXBASE)); //creating 5 vms
			//setCloudletList(CloudSimExample8.createCloudlet(tmp_broker.getId(), 100, INDEXBASE)); // creating 10 cloudlets
			GlobalBroker.INDEXBASE +=10000;
			
			tmp_broker.submitVmList(vmList);
			tmp_broker.submitCloudletList(getCloudletList());
	
			CloudSim.resumeSimulation();
	
			break;
		//case REDEPLOY:
	
			
			//kmeans(tmp_broker.getCloudletList());
			
		//case CREATE_CLOUDLETS:
		//	List<Cloudlet> cloudlets = FogHelper.createCloudletList(FogConst.NUMBER_OF_CLOUDLETS);
			
			
			
		case ADDING_FOG:
		//case CREATE_DC:
			
			
		default:
			Log.printLine(getName() + ": unknown event type");
			break;
		}
	}
	protected List<Fog> createFogs(int iterVersion,List<Vm> vmlist,int broker) {
		KmeanFogletAllocator km = new KmeanFogletAllocator(cloudletList,iterVersion);
		List<CentroidCluster<Point>> clusterResult = km.getResult();
		List<Fog> fogs = new ArrayList<Fog>();
		//List<Vm> tmp_vm_list = new ArrayList<Vm>();
		for(int i = 0;i<iterVersion;i++) {
			
 			int fid = iterVersion *10000 +i;
			
			Fog newfog = new Fog(fid, clusterResult.get(i).getCenter().getPoint(),broker);
			List<Cloudlet> cloudlets = new ArrayList<Cloudlet>();
			for(Point point:clusterResult.get(i).getPoints()) {
				cloudlets.add(point.getCloudlet());
			}
			newfog.init(cloudlets);
			vmlist.addAll(newfog.getVms());
			fogs.add(newfog);
			Log.printLine("fognode-"+ i + "is launching");
		}
		//FogBroker bb = (FogBroker) CloudSim.getEntity(broker);
		//bb.submitVmList(tmp_vm_list);
		return fogs;
		
	}
	@Override
	public void startEntity() {
		Log.printLine(super.getName()+" is starting....");
		schedule(getId(), 0, CREATE_EXPERIENMENT);
		//schedule(getId(),3000, CREATE_BROKER);
	}
	
	@Override
	public void shutdownEntity() {
	}
	
	public Map<Integer,List<Vm>> getVmColletion() {
		return vmCollection;
	}
	
	
	
	public List<Cloudlet> getCloudletList() {
		return cloudletList;
	}
	
	//protected void setCloudletList(List<Cloudlet> cloudletList) {
	//	this.cloudletList = cloudletList;
	//}
	
	public List<DatacenterBroker> getBroker() {
		return brokerList;
	}
	
	protected void addBroker(DatacenterBroker broker) {
		if(this.brokerList == null){
			this.brokerList = new LinkedList<DatacenterBroker>();
		}
		try{
			this.brokerList.add(broker);
		}  catch (Exception e) {
			e.printStackTrace();
	
		}
	}
	
	protected void setDatacenter(Datacenter datacenter) {
		this.datacenterList.add(datacenter);
	}
	public int getIterVersion() {
		return iterationVersion;
	}

}