package graphage;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseClick implements MouseListener {
	
	private SelectableArea selArea;
	
	public MouseClick(SelectableArea sa){
		this.selArea = sa;
	}
	
	public void mouseClicked(MouseEvent e){
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON1){
			selArea.startSelection(e.getLocationOnScreen());
		}else{
			selArea.cancel();
		}
	}

	public void mouseReleased(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON1){
			if(selArea.hasSize())
				selArea.getShot();
			else
				selArea.cancel();
		}
	}
}
