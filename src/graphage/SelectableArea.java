package graphage;

import graphage.gui.InteractivePlotFinder;
import graphage.gui.MyDisplay;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import javax.swing.JButton;
import javax.swing.JFrame;
import com.sun.awt.AWTUtilities;

public class SelectableArea extends JFrame {
	
	private boolean isClicked;
	private Point p;
	private JFrame screenFrame;
	private Raster img;
	private ControlPanel sd;
	
	public SelectableArea(){
		super("Screen Capture");
		this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 155));
		AWTUtilities.setWindowOpacity(this, 0.8f);
		this.setSize(0,0);
		this.setLocation(0, 0);
		
		// Invisible full-screen canvas
		screenFrame = new JFrame();
		screenFrame.addMouseMotionListener(new ScreenDrag(this));
		screenFrame.addMouseListener(new MouseClick(this));
		AWTUtilities.setWindowOpacity(screenFrame, 0.01f);
		screenFrame.setUndecorated(true);
		screenFrame.setVisible(false);
		screenFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		
		isClicked = false;
		img = null;
		p = null;
	}
	
	public void start(ControlPanel _sd){
		sd = _sd;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		screenFrame.setSize(d.width, d.height);
		screenFrame.setVisible(true);
	}
	
	public void startSelection(Point iniP){
		this.p = iniP;
		isClicked = true;
		this.setLocation(iniP);
		this.setVisible(true);
	}
	
	public void updateSize(Point curP){
		if(isClicked){
			int tmpx, tmpy;
			this.setSize(Math.abs(curP.x - p.x), Math.abs(curP.y - p.y));
			if(curP.x < p.x)
				tmpx = curP.x;
			else
				tmpx = p.x;
			
			if(curP.y < p.y)
				tmpy = curP.y;
			else
				tmpy = p.y;
			
			this.setLocation(tmpx, tmpy);
		}
	}
	
	public void getShot(){
		try {
			
			this.setVisible(false);
			screenFrame.setVisible(false);
			
		    Robot robot = new Robot();
		    Rectangle captureSize = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		    BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
		    img = bufferedImage.getRaster();
		    
		    /*
		    MyDisplay ssDisplay = new MyDisplay("Screen Capture");
			ssDisplay.load(img);
			ssDisplay.run();
			*/
		    InteractivePlotFinder ipf = new InteractivePlotFinder("Screen Capture",img);
			
			cancel();
		    
		}catch(AWTException e) {
	    	System.err.println("Problem taking screen shot - " + e.getMessage());
	    }
	}
	
	public boolean hasSize(){
		return(this.getSize().width > 0 && this.getSize().height > 0);
	}
	
	public void cancel(){
		this.setVisible(false);
		this.setSize(0,0);
		this.setLocation(0, 0);
		isClicked = false;
		p = null;
		screenFrame.setVisible(false);
		sd.setState(NORMAL);
		sd = null;
		System.gc();
	}

}
