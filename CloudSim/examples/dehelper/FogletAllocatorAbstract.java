package dehelper;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;

public abstract class FogletAllocatorAbstract {
	
	protected List<CentroidCluster<Point>> clusterResults;
	public FogletAllocatorAbstract(List<Cloudlet> cloudlets) {
		
	};
	
	public abstract void printClustererOutput();
	}


