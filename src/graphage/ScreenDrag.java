package graphage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class ScreenDrag extends MouseMotionAdapter {

	private SelectableArea selArea;
	
	public ScreenDrag(SelectableArea sa){
		this.selArea = sa;
	}
	
	 public void mouseDragged(MouseEvent e) {
		 this.selArea.updateSize(e.getLocationOnScreen());
	 }
}
