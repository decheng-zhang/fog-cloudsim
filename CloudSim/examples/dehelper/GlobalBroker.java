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
	private List<Vm> vmList;
	private List<Cloudlet> cloudletList;
	private DatacenterBroker broker;

	public GlobalBroker(String name) {
		super(name);
	}

	@Override
	public void processEvent(SimEvent ev) {
		switch (ev.getTag()) {
		case CREATE_BROKER:
			setBroker(createBroker(super.getName()+"_"));

			//Create VMs and Cloudlets and send them to broker
			setVmList(createVM(getBroker().getId(), 5, 100)); //creating 5 vms
			setCloudletList(createCloudlet(getBroker().getId(), 10, 100)); // creating 10 cloudlets

			broker.submitVmList(getVmList());
			broker.submitCloudletList(getCloudletList());

			CloudSim.resumeSimulation();

			break;

		default:
			Log.printLine(getName() + ": unknown event type");
			break;
		}
	}

	@Override
	public void startEntity() {
		Log.printLine(super.getName()+" is starting...");
		schedule(getId(), 200, CREATE_BROKER);
	}

	@Override
	public void shutdownEntity() {
	}

	public List<Vm> getVmList() {
		return vmList;
	}

	protected void setVmList(List<Vm> vmList) {
		this.vmList = vmList;
	}

	public List<Cloudlet> getCloudletList() {
		return cloudletList;
	}

	protected void setCloudletList(List<Cloudlet> cloudletList) {
		this.cloudletList = cloudletList;
	}

	public DatacenterBroker getBroker() {
		return broker;
	}

	protected void setBroker(DatacenterBroker broker) {
		this.broker = broker;
	}

}