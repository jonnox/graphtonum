package graphage.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.media.jai.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import com.sun.media.jai.widget.*;


public class MyDisplay {
	
	protected JFrame frame;
	protected JPanel panel;
	
	DisplayJAI ds_image;
	protected BufferedImage buff_image;
	protected RenderedImage image;
	protected Raster col_rast;
	protected Raster gry_rast;
	protected WritableRaster col_w_rast;
	protected WritableRaster gry_w_rast;
	
	protected GridBagConstraints gbcons;
	
	private static final int MIN_FRAME_WIDTH = 600;
	private static final int MIN_FRAME_HEIGHT = 600;
	private static final double RED_BIAS = 0.21;
	private static final double GREEN_BIAS = 0.71;
	private static final double BLUE_BIAS = 0.07;
	private static final int RED_VALUE = 0;
	private static final int GREEN_VALUE = 1;
	private static final int BLUE_VALUE = 2;
	private static final int GREY_VALUE = 3;
	
	protected int th_red;
	protected int th_green;
	protected int th_blue;
	protected int th_grey;
	protected int img_width;
	protected int img_height;
	
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
	private boolean isColour;
	
	
	public MyDisplay(String _title){
		frame = new JFrame(_title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel(new GridBagLayout());
		gbcons = new GridBagConstraints();
		image = null;
		th_red = 255;
		th_green = 255;
		th_blue = 255;
		th_grey = 255;
	}
	
	/**
	 * Uses the luminosity algorithm to convert a pixel to grayscale
	 * @param pData array containing the pixel colour rgb data values
	 * @return gray rgb value
	 */
	private int convLum(int[] pData){
		int rvalue = 0;
		rvalue = (int)(pData[0] * RED_BIAS + pData[1] * GREEN_BIAS + pData[2] * BLUE_BIAS);
		return rvalue;
	}
	
	/**
	 * Uses the average RGB values to convert pixel to grayscale
	 * @param pData array containing the pixel colour rgb data values
	 * @return gray rgb value
	 */
	private int convAvg(int[] pData){
		int rvalue = 0;
		rvalue = (int)((pData[0] + pData[1] + pData[2]) / 3);
		return rvalue;
	}
	
	/**
	 * Converts <code>image</code> from a colour image to grayscale and stores
	 * the result in <code>image_grey</code>
	 */
	private WritableRaster convertToGrey(Raster _ras){
		WritableRaster image_writable_ras = _ras.createCompatibleWritableRaster();
		int image_w = _ras.getWidth();
		int image_h = _ras.getHeight();
		int grey_val = 0;
		int pVals[] = new int[4];
		// Iterate through each row, converting each pixel to grey
		for(int j = 0; j < image_h; j++){
			for(int i = 0; i < image_w; i++){
				grey_val = convLum(_ras.getPixel(i, j, pVals));
				pVals[0] = pVals[1] = pVals[2] = grey_val;
				image_writable_ras.setPixel(i, j, pVals);
			}
		}
		return image_writable_ras;
	}
	
	/**
	 * Re-draws a <code>WritableRaster</code> that is biased by a threashold
	 * @param _value
	 * @param _th
	 * @param _r
	 * @param _curr_wr
	 * @return
	 */
	private WritableRaster threashold(boolean isColour, Raster _r){
		int[] delPix = {255,255,255,1};
		int[] tmp_otmp = new int[4];
		int[] tmp_tmp = new int[4];
		WritableRaster rRast = _r.createCompatibleWritableRaster();
		if(isColour){
			for(int j = 0; j < img_height; j++){
				for(int i = 0; i < img_width; i++){
					tmp_otmp = _r.getPixel(i, j, tmp_tmp);
					if(tmp_otmp[0] > th_red)
						rRast.setPixel(i, j, delPix);
					else if(tmp_otmp[1] > th_green)
						rRast.setPixel(i, j, delPix);
					else if(tmp_otmp[2] > th_blue)
						rRast.setPixel(i, j, delPix);
					else
						rRast.setPixel(i,j,tmp_otmp);
				}
			}
		}else{
			for(int j = 0; j < img_height; j++){
				for(int i = 0; i < img_width; i++){
					tmp_otmp = _r.getPixel(i, j, tmp_tmp);
					if(tmp_otmp[0] > th_grey)
						rRast.setPixel(i, j, delPix);
					else
						rRast.setPixel(i,j,tmp_otmp);
				}
			}
		}
		
		return rRast;
	}
	
	public void load(String imagename){
		try{
			// Load image file
			image = JAI.create("fileload", imagename);
			// Adjust frame to image size with room for controls
			int frame_width, frame_height;
			
			img_width = frame_width = image.getWidth();
			img_height = frame_height = image.getHeight();
			
			// Create buffered image to display
			buff_image = new BufferedImage(frame_width, frame_height, BufferedImage.TYPE_INT_RGB);
			
			if(frame_width < MIN_FRAME_WIDTH)
				frame_width = MIN_FRAME_WIDTH;
			if(frame_height < MIN_FRAME_HEIGHT)
				frame_height = MIN_FRAME_HEIGHT;
			// Set the JFrame size to fit the image and controls
			frame.setSize(frame_width,frame_height);
			// Add a panel to frame
			frame.getContentPane().add(panel);
			
			// Add image 
			gbcons.fill = GridBagConstraints.HORIZONTAL;
			gbcons.gridwidth = 5;
			gbcons.gridx = 0;
			gbcons.gridy = 0;
			
			//
			// Create Rasters and WriteableRasters for colour and B&W  
			//
			col_rast = image.getData();
			int tmp_tmp[] = new int[4];
			int tmp_otmp[] = new int[4];
			col_w_rast = col_rast.createCompatibleWritableRaster();
			for(int j = 0; j < img_height; j++){
				for(int i = 0; i < img_width; i++){
					tmp_otmp = col_rast.getPixel(i, j, tmp_tmp);
					col_w_rast.setPixel(i, j, tmp_otmp);
				}
			}
			gry_w_rast = convertToGrey(col_rast);
			gry_rast = (Raster) gry_w_rast;
			
			//buff_image.setData(image.getData());
			buff_image.setData(col_w_rast);
			
			ds_image = new DisplayJAI(buff_image);
			//ds_image = new DisplayJAI(image);
			panel.add(ds_image,gbcons);
			
			sl_red = new JSlider(JSlider.HORIZONTAL,0,255,255);
			sl_red.setMajorTickSpacing(15);
			sl_red.setPaintTicks(true);
			sl_red.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent slev) {
					try{
						JSlider _slider = (JSlider)slev.getSource();  
						//if (!_slider.getValueIsAdjusting()) {
							th_red = _slider.getValue();
							col_w_rast = threashold(true,col_rast);
							buff_image.setData(col_w_rast);
							ds_image.set(buff_image);
						//}
					}catch(Exception e){
						System.out.println(e.toString());
					}
				}
			});
			
			sl_green = new JSlider(JSlider.HORIZONTAL,0,255,255);
			sl_green.setMajorTickSpacing(15);
			sl_green.setPaintTicks(true);
			sl_green.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent slev) {
					try{
						JSlider _slider = (JSlider)slev.getSource();  
						//if (!_slider.getValueIsAdjusting()) {
							th_green = _slider.getValue();
							col_w_rast = threashold(true,col_rast);
							buff_image.setData(col_w_rast);
							ds_image.set(buff_image);
						//}
					}catch(Exception e){
						System.out.println(e.toString());
					}
				}
			});
			
			sl_blue = new JSlider(JSlider.HORIZONTAL,0,255,255);
			sl_blue.setMajorTickSpacing(15);
			sl_blue.setPaintTicks(true);
			sl_blue.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent slev) {
					try{
						JSlider _slider = (JSlider)slev.getSource();  
						//if (!_slider.getValueIsAdjusting()) {
							th_blue = _slider.getValue();
							col_w_rast = threashold(true,col_rast);
							buff_image.setData(col_w_rast);
							ds_image.set(buff_image);
						//}
					}catch(Exception e){
						System.out.println(e.toString());
					}
				}
			});
			
			sl_grey = new JSlider(JSlider.HORIZONTAL,0,255,255);
			sl_grey.setMajorTickSpacing(15);
			sl_grey.setMinorTickSpacing(5);
			sl_grey.setPaintTicks(true);
			sl_grey.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent slev) {
					try{
						JSlider _slider = (JSlider)slev.getSource();  
						//if (!_slider.getValueIsAdjusting()) {
							th_grey = _slider.getValue();
							gry_w_rast = threashold(false,gry_rast);
							buff_image.setData(gry_w_rast);
							ds_image.set(buff_image);
						//}
					}catch(Exception e){
						System.out.println(e.toString());
					}
				}
			});
			
			lb_red = new JLabel("Red threashold");
			lb_green = new JLabel("Green threashold");
			lb_blue = new JLabel("Blue threashold");
			lb_grey = new JLabel("Grey Threashold");
			
			// Add red slider
			gbcons.fill = GridBagConstraints.HORIZONTAL;
			gbcons.ipady = 10;
			gbcons.gridwidth = 1;
			gbcons.gridx = 0;
			gbcons.gridy = 1;
			panel.add(lb_red,gbcons);
			gbcons.gridx = 1;
			panel.add(sl_red,gbcons);
			
			// Add Green slider
			gbcons.gridx = 0;
			gbcons.gridy = 2;
			panel.add(lb_green,gbcons);
			gbcons.gridx = 1;
			panel.add(sl_green,gbcons);
			
			// Add blue slider
			gbcons.gridx = 0;
			gbcons.gridy = 3;
			panel.add(lb_blue,gbcons);
			gbcons.gridx = 1;
			panel.add(sl_blue,gbcons);
			
			// Add button
			colswitch = new JButton("Greyscale");
			gbcons.gridheight = 3;
			gbcons.gridx = 2;
			gbcons.gridy = 1;
			colswitch.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        if(isColour){
			        	isColour = false;
			        	colswitch.setText("Colour");
			        	sl_red.setEnabled(false);
			        	sl_green.setEnabled(false);
			        	sl_blue.setEnabled(false);
			        	lb_red.setEnabled(false);
			        	lb_green.setEnabled(false);
			        	lb_blue.setEnabled(false);
			        	sl_grey.setEnabled(true);
			        	lb_grey.setEnabled(true);
			        	buff_image.setData(gry_w_rast);
			        }else{
			        	isColour = true;
			        	colswitch.setText("Greyscale");
			        	sl_red.setEnabled(true);
			        	sl_green.setEnabled(true);
			        	sl_blue.setEnabled(true);
			        	lb_red.setEnabled(true);
			        	lb_green.setEnabled(true);
			        	lb_blue.setEnabled(true);
			        	sl_grey.setEnabled(false);
			        	lb_grey.setEnabled(false);
			        	buff_image.setData(col_w_rast);
			        }
			        ds_image.set(buff_image);
			    }
			});
			panel.add(colswitch,gbcons);
			
			// Add grey slider
			gbcons.gridheight = 1;
			gbcons.gridx = 3;
			gbcons.gridy = 2;
			panel.add(lb_grey,gbcons);
			gbcons.gridx = 4;
			panel.add(sl_grey,gbcons);
			sl_grey.setEnabled(false);
			lb_grey.setEnabled(false);
			
			isColour = true;
			
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
