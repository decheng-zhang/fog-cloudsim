package dehelper;

import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G5Xeon3075;

public final class FogConst {

	private FogConst() {} ;
	/*
	 * VM instance types:
	 *   High-Memory Extra Large Instance: 3.25 EC2 Compute Units, 8.55 GB // too much MIPS
	 *   High-CPU Medium Instance: 2.5 EC2 Compute Units, 0.85 GB
	 *   Extra Large Instance: 2 EC2 Compute Units, 3.75 GB
	 *   Small Instance: 1 EC2 Compute Unit, 1.7 GB
	 *   Micro Instance: 0.5 EC2 Compute Unit, 0.633 GB
	 *   We decrease the memory size two times to enable oversubscription
	 *
	 */
	public final static boolean ENABLE_OUTPUT = true;
	public final static boolean OUTPUT_CSV    = true;

	public final static double SCHEDULING_INTERVAL = 300;
	public final static double SIMULATION_LIMIT =  24 * 60 * 60;

	public final static int CLOUDLET_LENGTH	= 500 * (int) SIMULATION_LIMIT;
	public final static int CLOUDLET_PES	= 1;
	//public final static int CLOUDLET_TYPES = 2;
	//public final static int[] ClOUDLET_LENGTH = {500* (int)SIMULATION_LIMIT, 50* (int) SIMULATION_LIMIT };
	
	
	
	public final static int NUMBER_OF_VMS = 1;

	//public final static int NUMBER_OF_HOSTS = 15; Using host_types as count of host
	public final static int NUMBER_OF_CLOUDLETS  = 10;
	
	public final static int CLOUDLET_TYPES = 5;
	public final static int[] CLOUDLET_MIPS = {2300,2300,2300,2300,2300};
	public final static int[] CLOUDLET_RAM = {1,1,1,1,1};
	public final static int[] CLOUDLET_BW = {1,1,1,1,1};
	

	public final static long CLOUDLET_UTILIZATION_SEED = 1;
	/*
	 * VM types:
	 * 	t2.small 1 vcpu, 2 Gib, 0.023 /h
	 * t2.medium 2 vcpu, 4 Gib, 0.047 /h
	 * t2.large 2 vcpu, 8 Gib, 0.094 /h
	 * t2.xlarge 4 vcpu, 16 Gib, 0.188 /h
	 * t2.2xlarge 8 vcpu, 32 Gib, 0.376 /h
	 */
	/*
	 * VM aws:
	 * t2.nano	t2.micro	t2.small	t2.medium	t2.large	t2.xlarge	t2.2xlarge	m4.large	m4.xlarge	m4.2xlarge	m4.4xlarge	m4.10xlarge	m4.16xlarge	p2.xlarge	p2.8xlarge	p2.16xlarge	g2.2xlarge	g2.8xlarge

	 */
	public final static int VM_TYPES	= 18;
	public final static int[] VM_MIPS	= { 2500,2500,2500,2500,2500,2500,2500,2500,2500,2500,2500,2500,2500,2500,2500,2500,2500,2500 };
	//public final static int[] VM_PES	= { 1, 2, 2, 4, 8 };
	public final static int[] VM_PES	= {1,1,1,2,2,4,8,2,4,8,16,40,64,4,32,64,8,32};
	//public final static int[] VM_RAM	= { 2048,  4096, 8192, 16384, 32768};
	public final static int[] VM_RAM	= { 512,1024,2048,4096,8192,16384,32768,8192,16384,32768,65536,163840,262144,62464,499712,749568,15360,61440};
	public final static int[] VM_GPES   = { 0,0,0,0,0,0,0,0,0,0,0,0,0,1,8,16,1,4 };
	//public final static int VM_BW		= 100000; // 100 Mbit/s
	public final static int [] VM_BW       = { 250000,300000,300000,300000,300000,500000,500000,500000,1024000,1024000,1024000,10240000,20480000,1024000,10240000,20480000,1024000,10240000 };
	public final static int VM_SIZE		= 2500; // 2.5 GB

	/*
	 * Host types:
	 *   HP ProLiant ML110 G4 (1 x [Xeon 3040 1860 MHz, 2 cores], 4GB, MSRP: $595)
	 *   HP ProLiant ML110 G5 (1 x [Xeon 3075 2660 MHz, 2 cores], 4GB, MSRP: $799)
	 *   32 GB single Hex(1 x [Xeon E5-2640 2500 MHz, 6 cores], 32GB, 5x300GB 4x1Gb NIC, Pricing: $449/mo)
	 *   64 GB Dual Hex (2 x [Xeon E5-2640 2500 MHz, 6 cores], 64 GB,2x600GB 4x1Gb NIC,Pricing: $640/mo)
	 *   128 GB Quad Octa (4 x [Xeon E5-4640 2400 MHz 8 cores], 128GB, 2x300GB 4x1Gb NIC,Pricing: $879/mo)
	 *   We increase the memory size to enable over-subscription (x4)
	 */
	public final static int HOST_TYPES	 =5;
	public final static int[] HOST_MIPS	 = { 3300,3300,3300,3300,3300 };
	public final static int[] HOST_PES	 = { 2, 2 ,6,12,32};
	public final static int[] HOST_RAM	 = { 4096, 4096 ,32768,65536,131072};
	public final static int HOST_BW		 = 1000000; // 1 Gbit/s
	public final static int HOST_STORAGE = 1000000; // 1 TB

	public final static PowerModel[] HOST_POWER = {
		new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(),
		new PowerModelSpecPowerHpProLiantMl110G5Xeon3075(),
		new PowerModelSpecPowerHpProLiantMl110G5Xeon3075(),
		new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(),
		new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(),
		
	};
}
