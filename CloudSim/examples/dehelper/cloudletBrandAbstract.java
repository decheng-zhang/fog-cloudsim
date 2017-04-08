package dehelper;
import org.cloudbus.cloudsim.Cloudlet;

import java.util.List;
import java.util.Map;

public abstract class cloudletBrandAbstract {
	protected Map<Integer, Integer> cltobrandingMap;
	public cloudletBrandAbstract(List<Cloudlet> cloudletlist) {};
	public abstract int getClBranding(int cl);
	
}
