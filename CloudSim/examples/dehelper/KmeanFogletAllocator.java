package dehelper;
import org.apache.commons.math3.ml.clustering.*;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class KmeanFogletAllocator extends FogletAllocatorAbstract{
	
	
	/*public KmeanFogletAllocator(List<Cloudlet> cloudlets, Object k) {
	super(cloudlets);
	this(List<Cloudlet> cloudles, int k);
	}*/
	public KmeanFogletAllocator(List<Cloudlet> cloudlets, int k) {
		super(cloudlets);
		List<Point> cloudletLoca = new ArrayList<Point>(cloudlets.size());
		for (Cloudlet cloudlet : cloudlets)
			cloudletLoca.add(new Point(cloudlet));
		KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(k, 10000);
		this.clusterResults = clusterer.cluster(cloudletLoca);
		
	}

	public void printClustererOutput(){ 
		DecimalFormat dft = new DecimalFormat("###.####");
		String indent = "\t";
		for (int i = 0; i < clusterResults.size(); i++){
			Log.printLine("Cluster" + i);
			Log.printLine(dft.format(clusterResults.get(i).getCenter().getPoint()[0]) + indent+
					dft.format(clusterResults.get(i).getCenter().getPoint()[1]));
			for (Point point:clusterResults.get(i).getPoints())
				Log.printLine("Cloudlet" + point.getCloudletID()+ indent + dft.format(point.getPoint()[0])+ indent + dft.format(point.getPoint()[1]));
			Log.printLine();
		}
	}
}
