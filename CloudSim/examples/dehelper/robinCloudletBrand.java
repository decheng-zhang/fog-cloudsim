package dehelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.cloudbus.cloudsim.Cloudlet;

public class robinCloudletBrand extends cloudletBrandAbstract {
	private Map<Integer, Integer> cltobrandingMap;
	private static CloudletRes[] data = null;
	private int cloudlet_num=0;
	private double locationMatrix [][];
	/**
	 * @return the locationMatrix
	 */
	public double[][] getLocationMatrix() {
		return locationMatrix;
	}



	public Map<Integer, Integer> getCltobrandingMap() {
		return cltobrandingMap;
	}

	
	
	public robinCloudletBrand(String inputFile) throws NumberFormatException,
	IOException {
		super();
	
	BufferedReader input = new BufferedReader(new FileReader(inputFile));
	//input.readLine();

	StringTokenizer t = new StringTokenizer(input.readLine(), "( )");
	t.nextToken();
	cloudlet_num = Integer.parseInt(t.nextToken());
	locationMatrix = new double[cloudlet_num][2];
	input.readLine();
	data=new CloudletRes[cloudlet_num];
	for (int i = 0; i < cloudlet_num ; i++) {
		String st= input.readLine();
		
			
		data[i] = new CloudletRes(st);
		StringTokenizer d = new StringTokenizer(st);
		d.nextToken();
		double x=Double.parseDouble(d.nextToken());
		double y=Double.parseDouble(d.nextToken());
		locationMatrix[i][0]=x;
		locationMatrix[i][1]=y;
		
	}
	
	
		
	
	input.close();
	}
	
	
	public List<CloudletRes> getCloudletResSet(List<Cloudlet> clList,boolean mergable) {
		
		cltobrandingMap = new HashMap<Integer,Integer>();
		List<CloudletRes> clrss = new ArrayList<CloudletRes>();
		
		
		for (Cloudlet i : clList) {
			int id = i.getCloudletId();
				CloudletRes r = data[id];
				clrss.add(r);
			//cltobrandingMap.put(id, id%(FogConst.CLOUDLET_TYPES));
		}
		if(mergable) {
		clrss = optimizeclress(clrss);
		}
		return clrss;
	}
	
	private List<CloudletRes> optimizeclress(List<CloudletRes> clrss){
		ListIterator<CloudletRes> litr = null;
		List<CloudletRes> optclress = new ArrayList<CloudletRes>();
		litr = optclress.listIterator();
		for(CloudletRes t : clrss) {
			
			int appid = t.getAppType();
			if(appid == -1) {
				
				optclress.add(t);
			}
			else {
				litr = optclress.listIterator();
				boolean finded = false;
				while(litr.hasNext() && !finded) {
					CloudletRes tm = litr.next();
					if(tm.getAppType()==appid) {
						litr.set(tm.merge(t));
						finded = true;
			}}
				if(!finded) {
					optclress.add(t);
				}
		}
			}
		litr =optclress.listIterator();
		while(litr.hasNext()) {
			CloudletRes r = litr.next();
			r.setVmType(findsuitableVm(r));
			litr.set(r);
		}
		return optclress;
		
		
		
		
	};
	private int findsuitableVm(CloudletRes r) {
		return 2;
	}
	public int [] getVmTypes(List<Cloudlet> cls) {
		
	}

	public int getCloudletNumber() {
		return cloudlet_num;
	}
	@Override
	public int getClBranding(int cl) {
		// TODO Auto-generated method stub
		return cltobrandingMap.get(cl);
	}

}
