package graphage;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.sun.awt.AWTUtilities;

import graphage.gui.MyDisplay;

// 

/**
 * Public main function
 * 
 * @author Jon Elliott
 *
 */
public class Graphage extends JFrame {
	public static void main(String[] args){
		
		ControlPanel cp = new ControlPanel("Jx");
		cp.setVisible(true);
		
		/*
		MyDisplay p = new MyDisplay("Test");
		MyDisplay p2 = new MyDisplay("Screen Shot");
		p.load("image.png");
		
		
		try {
		    Robot robot = new Robot();
		    Rectangle captureSize = new Rectangle(0, 0, 500, 500);
		    BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
		    p2.load(bufferedImage.getRaster());
		    
		}catch(AWTException e) {
	    	System.err.println("Problem");
	    }
		
		p.run();
		p2.run();
		*/
	}
}
