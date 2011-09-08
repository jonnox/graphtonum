package graphage.gui;

import javax.swing.*;
import javax.media.jai.*;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.*;
import com.sun.media.jai.widget.*;


public class MyDisplay {
	
	protected JFrame frame;
	protected JPanel panel;
	
	DisplayJAI ds_image;
	protected RenderedImage image;
	protected RenderedImage image_grey;
	protected Raster image_ras;
	
	protected GridBagConstraints gbcons;
	
	
	
	protected JSlider sl_red;
	protected JSlider sl_green;
	protected JSlider sl_blue;
	protected JSlider sl_grey;
	
	protected JLabel lb_img;
	protected JLabel lb_red;
	protected JLabel lb_green;
	protected JLabel lb_blue;
	protected JLabel lb_grey;
	
	protected JButton colswitch;
	
	
	public MyDisplay(String _title){
		frame = new JFrame(_title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel(new GridBagLayout());
		gbcons = new GridBagConstraints();
		image = null;
		image_grey = null;
	}
	
	/**
	 * Converts <code>image</code> from a colour image to grayscale and stores
	 * the result in <code>image_grey</code>
	 */
	private void convertToGrey(){
		image_grey = image;
		image_ras = image_grey.getData();
	}
	
	public void load(String imagename){
		try{
			// Load image file
			image = JAI.create("fileload", imagename);
			// Adjust frame to image size with room for controls
			frame.setSize(image.getWidth(),image.getHeight());
			// Add a panel to frame
			frame.getContentPane().add(panel);
			
			// Add image 
			gbcons.fill = GridBagConstraints.HORIZONTAL;
			gbcons.gridx = 0;
			gbcons.gridy = 0;
			
			ds_image = new DisplayJAI(image);
			//lb_img = new JLabel();
			//lb_img.setIcon(new ImageIcon((BufferedImage) image));
			//lb_img.setVisible(true);
			panel.add(ds_image,gbcons);
			//panel.add(new JLabel().setIcon(new ImageIcon((BufferedImage)image)),gbcons);
		}catch(Exception e){
			System.out.println("Error: " + e.toString());
		}
	}
	
	/**
	 * Displays a <code>JFrame</code> and sets up the
	 * graphical environment.
	 * 
	 * @author Jon Elliott
	 */
	public void run(){
		if(image != null){
			frame.pack();
			frame.setVisible(true);
		}else{
			System.out.println("Image not loaded");
		}
	}
}
