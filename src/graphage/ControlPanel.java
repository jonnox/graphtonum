package graphage;

import graphage.gui.InteractivePlotFinder;
import graphage.gui.MyDisplay;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import com.sun.awt.AWTUtilities;

public class ControlPanel extends JFrame{
	
	// Button to take a screen shot / open file
	private JButton btn_ss;
	private JButton btn_img;
	private JButton btn_about;
	private JPanel panel;
	private GridBagConstraints gbcons;
	private JFileChooser fileCh;
	private JPanel spanel;
	private SelectableArea selArea;
	
	// Transparent screen to take a screen shot
	private JFrame screenFrame;
	
	public ControlPanel(String title){
		super(title);
		
		selArea = new SelectableArea();
		
		// Set controls box size
		this.setSize(150, 150);
		// Cannot alter size of frame
		this.setResizable(false);
		
		this.setLocation(400, 300);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		gbcons = new GridBagConstraints();
		
		screenFrame = new JFrame("Screen Capture");
		screenFrame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		screenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		spanel = new JPanel();
		
		// Create buttons
		btn_ss = new JButton("Screen Capture");
		btn_img = new JButton("Open File..");
		btn_about = new JButton("About");
		
		//=========================
		// Configure About Button 
		//=========================
		btn_about.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	JOptionPane.showMessageDialog(null,"Hello");
		    }
		});
		
		//==========================
		// Configure File Button
		//==========================
		fileCh = new JFileChooser();
		ImageFileFilter chFilter = new ImageFileFilter();
	    chFilter.addExtension("gif");
		chFilter.addExtension("png");
		chFilter.addExtension("bmp");
		chFilter.addExtension("jpg");
	    chFilter.setDescription("Image files");
	    fileCh.setFileFilter(chFilter);
	    btn_img.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e) {
	    		int responce;
	    		responce = fileCh.showOpenDialog(null);
	    		if(responce == 0){
	    			/*
	    			MyDisplay tmp = new MyDisplay("-");
	    			tmp.load(fileCh.getSelectedFile().getPath());
	    			tmp.run();
	    			*/
	    			InteractivePlotFinder ipf = new InteractivePlotFinder(fileCh.getSelectedFile().getPath());
	    		}
	    	}
	    });
	    
	    //==========================
	  	// Configure Screen Capture
	  	//==========================
	  	btn_ss.addActionListener(new ActionListener(){
	  		public void actionPerformed(ActionEvent e) {
	  			captureScreen();
	  	    }
	  	});
		
		// Add image 
		
		// Add buttons
		gbcons.fill = GridBagConstraints.HORIZONTAL;
		gbcons.gridx = 0;
		gbcons.gridy = 0;
		panel.add(new JLabel("Welcome"),gbcons);
		gbcons.gridy = 1;
		panel.add(btn_ss,gbcons);
		gbcons.gridy = 2;
		panel.add(btn_img,gbcons);
		gbcons.gridy = 3;
		panel.add(btn_about,gbcons);
		this.add(panel);

	}
	
	private void captureScreen(){
		selArea.start(this);
		this.setState(ICONIFIED);
	}
}
