package dehelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;

public class robinCloudletBrand extends cloudletBrandAbstract {
	private static Map<Integer, Integer> cltobrandingMap;
	public static Map<Integer, Integer> getCltobrandingMap() {
		return cltobrandingMap;
	}

	

	public robinCloudletBrand(List<Cloudlet> clList) {
		super(clList);
		cltobrandingMap = new HashMap<Integer,Integer>();
		for (Cloudlet i : clList) {
			int id = i.getCloudletId();
			cltobrandingMap.put(id, id%(FogConst.CLOUDLET_TYPES));
		}
			
		
	}

	@Override
	public int getClBranding(int cl) {
		// TODO Auto-generated method stub
		return cltobrandingMap.get(cl);
	}

}
