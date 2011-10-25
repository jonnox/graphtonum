package graphsolver;

import java.awt.Point;
import java.awt.image.Raster;
import java.util.ArrayList;

/**
 * Responsible for 'growing' a line to find points
 * 
 * @author 100174454
 *
 */
public class LineCrawler {
	Raster image;
	public LineCrawler(Raster image){
		this.image = image;
	}
	
	/**
	 * Finds a curve given a point on the given curve
	 * @param p a point on the line to be solved
	 * @return points of the line
	 */
	public ArrayList<Point> findLine(Point p){
		ArrayList<Point> points = new ArrayList<Point>();
		
		return points;
	}
}
