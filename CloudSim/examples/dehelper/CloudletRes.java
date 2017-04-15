package dehelper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CloudletRes {
	private List<Integer> cloudletId=null;
	int appType;
	int UserCount;
	private int VmType=-1;

	private int VmId=-1;
	private double totalMIPS;
	private double totalRAM;
	private double totalBW;

	 public CloudletRes(final String input) {
		StringTokenizer t = new StringTokenizer(input);
		cloudletId = new ArrayList<Integer>();
		cloudletId.add(Integer.parseInt(t.nextToken()));
		t.nextToken();
		t.nextToken();
		UserCount = Integer.parseInt(t.nextToken());
		appType = Integer.parseInt(t.nextToken());
		//discard the in and out degree.
		if (appType == -1) {
			double MIPSper = Double.parseDouble(t.nextToken());
			totalMIPS = UserCount * MIPSper;
			double RAMper = Double.parseDouble(t.nextToken());
			totalRAM = UserCount * RAMper;
			double BWper = Double.parseDouble(t.nextToken());
			totalBW = UserCount * BWper;
		}else {
			totalMIPS = UserCount *FogConst.CLOUDLET_MIPS[appType];
			totalRAM = FogConst.CLOUDLET_RAM[appType];
			totalBW = UserCount * FogConst.CLOUDLET_BW[appType];
		}
;
	}

	 public CloudletRes merge(CloudletRes sameApp) {
		 cloudletId.addAll(sameApp.getCloudletId());
		 totalMIPS += sameApp.getTotalMIPS();
		 totalBW += sameApp.getTotalBW();
		 totalRAM += sameApp.getTotalRAM();
		 return this;
	 }
	/**
	 * @return the totalMIPS
	 */
	public double getTotalMIPS() {
		return totalMIPS;
	}

	/**
	 * @return the totalRAM
	 */
	public double getTotalRAM() {
		return totalRAM;
	}

	/**
	 * @return the totalBW
	 */
	public double getTotalBW() {
		return totalBW;
	}

	/**
	 * @return the cloudletId
	 */
	public List<Integer> getCloudletId() {
		return cloudletId;
	}

	
	/**
	 * @return the vmType
	 */
	public int getVmType() {
		return VmType;
	}

	/**
	 * @param vmType the vmType to set
	 */
	public void setVmType(int vmType) {
		VmType = vmType;
	}

	/**
	 * @return the vmId
	 */
	public int getVmId() {
		return VmId;
	}

	/**
	 * @param vmId the vmId to set
	 */
	public void setVmId(int vmId) {
		VmId = vmId;
	}

	/**
	 * @return the appType
	 */
	public int getAppType() {
		return appType;
	}

	/**
	 * @param appType the appType to set
	 */
	public void setAppType(int appType) {
		this.appType = appType;
	}

}
