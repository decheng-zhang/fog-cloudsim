package DechengFog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 */

/**
 * @author dechengzhang-admin
 *
 */
public class fogNodeSplitGenerator {

	private double Nodes[][];
	private List<Edge> Edges ;
	
	public fogNodeSplitGenerator(double[] areaScale,int cut) {
		Nodes =  new double [cut*cut][2];
		
		int t =0;
		for (int i=0;i< cut;i++){
			for(int j=0;j<cut;j++) {
				
				Nodes[t][0]=areaScale[0] *((double)i+1)/((double)cut+1)-500;
				Nodes[t++][1]=areaScale[1]*((double)j+1)/((double)cut+1)-500;
				
			}
			}
		//buildEdges();
	}
	public fogNodeSplitGenerator(double radius, int degreecut,int level) {
		nodeAdder(radius,degreecut,level);
		//buildEdges();
		
	};
	public void nodeAdder(double radius, int degreecut,int level) {
		if( Nodes == null){
		Nodes =  new double [degreecut*level+1][2];
		Nodes[Nodes.length-1][0]=0;
		Nodes[Nodes.length-1][1]=0;
		}
		for(int i = 0;i<degreecut;i++) {
			//System.out.println(Math.toRadians(360/(double)degreecut));
			Nodes[i+degreecut*(level-1)][0]=radius*Math.cos(Math.toRadians(i*360/(double)degreecut));
			Nodes[i+degreecut*(level-1)][1]=radius*Math.sin(Math.toRadians(i*360/(double)degreecut));
		}
		if(level!=1) {
			nodeAdder(radius*2.5,degreecut, level-1);
			
		}
	}
	
	public String buildEdges() {
		String de = " ";
		//Edges = new ArrayList<Edge>();
		StringBuilder edgelines = new StringBuilder("\n\n"+"Edges: ( "+calcFullMashEdge(Nodes.length) +" )\n" );
		
		int t = 0;
		int edgeIndex=0;
		for (double[] i : Nodes) {
			
			for (int it=0;it <t;it++ ) {
				double distance = Math.sqrt(Math.pow((Nodes[t][0]-Nodes[it][0]),2)+
						Math.pow((Nodes[t][1]-Nodes[it][1]),2));
			edgelines.append(edgeIndex + de + t + de + it +de +distance +de + "1.00 10.00 -1 -1 E_RT_NONE\n" ); 
		
			//Edges.add(new Edge(edgeline));
			edgeIndex ++;
		}
			t++;
			
		
	}
		return edgelines.toString();
	}
	
	private int calcFullMashEdge(int nodesN) {
		if(nodesN==1){return 0;		}
		else {return calcFullMashEdge(nodesN-1)+nodesN-1;}
}
	public void writeNodes(String outputPath) {
		StringBuilder data = new StringBuilder();
		int t = 0;
		String de = " ";
		data.append("\n\n\n");
		data.append("Nodes: ");
		data.append("(" + Nodes.length +")\n");
		for (double[] i : Nodes) {
			
			data.append(t);
			data.append(de);
			data.append(i[0]);
			data.append(de);
			data.append(i[1]);
			data.append(" 3 3 -1 RT_NONE");
			
			data.append("\n");
			t++;
		}
		System.out.println(Arrays.deepToString(Nodes));
		File file = new File(outputPath);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(data.toString());
			writer.write(buildEdges());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double testarea[] = {1000,1000};
		//System.out.println(Array.toString(testarea)));
		fogNodeSplitGenerator t =  new fogNodeSplitGenerator(60.0,12,3);
		t.writeNodes("topo/test.brite");// TODO Auto-generated method stub

	}

}
