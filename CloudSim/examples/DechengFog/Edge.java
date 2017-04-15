package DechengFog;
import java.util.StringTokenizer;
import java.awt.*;

/**
 * Representation of an edge from a BRITE topology.
 */
public class Edge
{
	/**
	 * The source node id.
	 */
	private int sourceID = -1;

	/**
	 * The destination node ID.
	 */
	private int destinationID = -1;

	/**
	 * Does this link connect between ASs?
	 */
	private boolean asconnector = false;

	private double delay = 0;
	private double bandwidth = 0;
	/**
	 * Default constructor
	 */
	public Edge(final String str)
	{
		StringTokenizer t = new StringTokenizer(str);
		//discard the ID.
		t.nextToken();
		sourceID = Integer.parseInt(t.nextToken());
		destinationID = Integer.parseInt(t.nextToken());
		t.nextToken();
		delay = Double.parseDouble(t.nextToken());
		bandwidth = Double.parseDouble(t.nextToken());
		int from = Integer.parseInt(t.nextToken());
		int to = Integer.parseInt(t.nextToken());
		asconnector = !(from == to);
	}

	/**
	 * Draw the edge
	 *
	 * @param g Graphics object.
	 * @param nodes The nodes to draw from and to.
	 */
	public void paint(Graphics g, final Node nodes[])
	{
		Graphics2D g2 = (Graphics2D) g;
		if (asconnector)
		{
			g2.setColor(Color.red);
		}
		else
		{
			g2.setColor(Color.black);
		}
		g2.setStroke(new BasicStroke((int)bandwidth/5));
		g2.drawLine((int)nodes[sourceID].getX()+500,
				   (int)nodes[sourceID].getY()+500,
				   (int)nodes[destinationID].getX()+500,
				   (int)nodes[destinationID].getY()+500);
	}
}
