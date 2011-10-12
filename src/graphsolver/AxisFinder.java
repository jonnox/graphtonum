package graphsolver;

import java.awt.Rectangle;
import java.awt.image.Raster;

public interface AxisFinder {
	public Rectangle findYAxis(Raster r);
	public Rectangle findXAxis(Raster r);
}
