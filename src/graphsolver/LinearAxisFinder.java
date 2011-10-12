package graphsolver;

import java.awt.Rectangle;
import java.awt.image.Raster;

public class LinearAxisFinder implements AxisFinder {
	private Raster _r;
	pAxis currA,prevA,longA;
	
	public LinearAxisFinder(){
		currA = new pAxis();
		prevA = new pAxis();
		longA = new pAxis();
	}

	@Override
	public Rectangle findYAxis(Raster r) {
		int h,w;
		Rectangle _rect = new Rectangle(0,0,0,0);
		_r = r;
		h = _r.getHeight();
		w = _r.getWidth();
		currA.reset();
		prevA.reset();
		longA.reset();
		return _rect;
	}

	@Override
	public Rectangle findXAxis(Raster r) {
		// TODO Auto-generated method stub
		return null;
	}

}

class pAxis{
	int _start;
	int _end;
	int _r,_g,_b;
	void reset(){
		_start = _end = 0;
		_r = _g = _b = 0;
	}
}