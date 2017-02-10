package dehelper;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.cloudbus.cloudsim.Cloudlet;

public class Point implements Clusterable{
	private double[] point;
	private Cloudlet cloudlet;
	
	public Point(Cloudlet cloudlet) {
		this.cloudlet = cloudlet;
		this.point = new double[] {cloudlet.getxAxis(), cloudlet.getyAxis()};
		
	}
	public int getCloudletID(){
		return cloudlet.getCloudletId();
	}
	public double[] getPoint(){
		return point;
	}
	public Cloudlet getCloudlet() {
		return cloudlet;
	}
}