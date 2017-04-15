package dehelper;
import org.cloudbus.cloudsim.Cloudlet;

import java.util.List;
import java.util.Map;

public abstract class cloudletBrandAbstract {
	protected Map<Integer, Integer> cltobrandingMap;
	protected int[] vmTypes;
	//public cloudletBrandAbstract() {};
	public abstract int getClBranding(int cl);
	public abstract List<CloudletRes> getCloudletResSet(List<Cloudlet> t,boolean mergeable);
	public int [] getVmTypes() {
		return vmTypes;
	};
}