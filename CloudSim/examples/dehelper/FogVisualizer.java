package dehelper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import DechengFog.BRITETopology;


public class FogVisualizer extends BRITETopology{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

				try
				{
					//try to open the file.
					BufferedReader file = new BufferedReader(new FileReader(args[0]));
					//build the visualization
					BRITETopology t = new BRITETopology(file);
					//close the file
					file.close();
				}
				catch (FileNotFoundException e)
				{
					System.out.println("Error! could not open file: " + args[0]);
					System.out.println(e);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		

	

}
