package dehelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudInformationService;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.examples.power.Helper;
import org.cloudbus.cloudsim.examples.power.random.RandomConstants;
import org.cloudbus.cloudsim.examples.power.random.RandomHelper;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;

public class Fog {
	
	private final int id;
	private String name;
	private final double x;
	private final double y;
	private double rentConstant;
	private PowerDatacenter DC;
	
	public Fog(int fogId,double[] coor, String name, PowerDatacenter datacenter) {
		DecimalFormat dft = new DecimalFormat("###.####");
		Log.printLine("Fog " + name + "is initialized at "+ "("+ dft.format(coor[0])+"," +dft.format(coor[1])+")" );
		
		id = fogId;
		this.x = coor[0];
		this.y = coor[1];
		this.name = name;
		this.DC = datacenter;
	}
	public int getId() {
		return id;
	}

	public double[] getCoord() {
		double []coor ={this.x, this.y};
		return coor;
	}
	
	public PowerDatacenter getDatacenter() {
		return DC;
	}

	public void setDatacenter(PowerDatacenter dC) {
		DC = dC;
	}

}
