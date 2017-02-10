package dehelper;
import org.apache.commons.math3.ml.clustering.*;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;

import java.util.ArrayList;
import java.util.List;

public class KmeanFogletAllocator {
	
	private List<CentroidCluster<Point>> clusterResults;
	public KmeanFogletAllocator(List<Cloudlet> cloudlets, int k) {
		
		List<Point> cloudletLoca = new ArrayList<Point>(cloudlets.size());
		for (Cloudlet cloudlet : cloudlets)
			cloudletLoca.add(new Point(cloudlet));
		KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(k, 10000);
		this.clusterResults = clusterer.cluster(cloudletLoca);
		
	}
	public List<CentroidCluster<Point>> getResult(){
		return this.clusterResults;
	}
	
	public void printClustererOutput(){ 
		String indent = "\t";
		for (int i = 0; i < clusterResults.size(); i++){
			Log.printLine("Cluster" + i);
			Log.printLine(clusterResults.get(i).getCenter());
			for (Point point:clusterResults.get(i).getPoints())
				Log.printLine("Cloudlet" + point.getCloudletID()+ indent + point.getPoint()[0]+ indent + point.getPoint()[1]);
			Log.printLine();
		}
	}
}
