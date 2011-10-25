package graphsolver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a gradient section with various attributes to create a cost
 * between 2 PointSections
 * @author 100174454
 *
 */
public class PointSection {
	/**
	 * List of all points in section
	 */
	private ArrayList<Point> points;
	
	/**
	 * Total summation of individual red values of all pixels in arc
	 */
	private int red;
	
	/**
	 * Total summation of individual green values of all pixels in arc
	 */
	private int green;
	
	/**
	 * Total summation of individual blue values of all pixels in arc
	 */
	private int blue;
	
	/**
	 * Constructs a point section
	 */
	public PointSection(){
		red = green = blue = 0;
		points = new ArrayList<Point>();
	}
	
	/**
	 * Add a point to the section
	 * @param rgb red, greed, and blue values of the point
	 * @param p actual point on raster
	 */
	public void addPoint(int[] rgb, Point p){
		points.add(p);
		red += rgb[0];
		green += rgb[1];
		blue += rgb[2];
	}
	
	/**
	 * Calculates the mid-point of the arc
	 * @return midpoint of arc
	 */
	public Point getMidPoint(){
		return points.get(points.size() / 2);
	}
	
	public int[] getAvgColour(){
		return new int[3];
	}
	
	/**
	 * Calculates the 'cost' to move from <code>this</code> point section to <code>next</code>
	 * @param next the next point section being considered
	 * @return cost of transition
	 */
	public int getCost(PointSection next){
		return 0;
	}
}
