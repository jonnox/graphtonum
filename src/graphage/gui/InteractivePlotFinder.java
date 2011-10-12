package graphage.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import javax.media.jai.JAI;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.media.jai.widget.DisplayJAI;

public class InteractivePlotFinder extends JFrame{
	private WritableRaster wrzoom2,wrzoom,gblock;
	private Raster wr;
	private BufferedImage buff_image;
	private JPanel panel,sidepanel;
	private BufferedImage buff_zoom;
	private int r = 20;
	private DisplayJAI disp,disp_zoom;
	private JLabel label;
	
	int[] gray = {128,128,128,1};
	int[] white = {255,255,255,1};
	int[] red = {255,0,0,1};
	
	public InteractivePlotFinder(String fname){
		super("File");
		this.wr = JAI.create("fileload", fname).getData();
		this.initalize();
	}
	
	public InteractivePlotFinder(String name, Raster wr){
		super(name);
		this.wr = wr;
		
		this.initalize();
	}
	
	
	private void updateMagnifier(Point p){
		int ii = p.y - r/2;
		int jj = p.x - r/2;
		int newi=0, newj=0,i4,j4;
		int[] pix = new int[4];
		int[] pix2 = new int[4];
		
		//wrzoom = buff_zoom.getRaster();
		//wrzoom2 = wrzoom.createCompatibleWritableRaster();
		
		for(int i = 0; i < r; i++){
			for(int j = 0; j < r; j++){
				newi = i + ii;
				newj = j + jj;
				i4 = i * 4;
				j4 = j * 4;
				if(newi < 0 || newi >= this.buff_image.getHeight()){
					wrzoom2.setDataElements(j * 4, i * 4, gblock);
				}else if (newj < 0 || newj >= wr.getWidth()){
					wrzoom2.setDataElements(j * 4, i * 4, gblock);
				}else{
					try{
					pix = wr.getPixel(newj, newi, pix);
					}catch(Exception e){
						System.out.println("e:" + newj + "x" + newi);
					}
					
					for(int iin = 0; iin < 4; iin++){
						for(int jin=0;jin < 4;jin++){
							wrzoom2.setPixel(jin + j4,iin + i4, pix);
						}
					}
				}
			}
		}
		
		newi = (r*2);
		for(int i=0; i<4;i++)
			for(int j=0;j<4;j++)
				wrzoom2.setPixel(newi + j,newi + i, red);
		
		buff_zoom.setData(wrzoom2);
		disp_zoom.repaint();
	}
	
	private void initalize(){
		panel = new JPanel();
		sidepanel = new JPanel(new GridBagLayout());
		buff_image = new BufferedImage(wr.getWidth(),wr.getHeight(),BufferedImage.TYPE_INT_RGB);
		buff_image.setData(wr);
		buff_zoom = new BufferedImage((r * 4),(r * 4),BufferedImage.TYPE_INT_RGB);
		wrzoom = buff_zoom.getRaster();
		wrzoom2 = wrzoom.createCompatibleWritableRaster();
		gblock = new BufferedImage(4,4,BufferedImage.TYPE_INT_RGB).getRaster();
		label = new JLabel("-");
		
		for(int i = 0; i < 4; i++){
			for(int j=0;j<4;j++){
				if((j + i) % 2 == 0){
					gblock.setPixel(i, j, gray);
				}else{
					gblock.setPixel(i, j, white);
				}
			}
		}
		
		GridBagConstraints c = new GridBagConstraints();
		this.setSize(wr.getWidth() + (r * 4) + 50, Math.max(wr.getHeight() + 50,(r * 4) + 100));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		disp = new DisplayJAI(buff_image);
		disp_zoom = new DisplayJAI(buff_zoom);
		
		disp.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				updateMagnifier(e.getPoint());
			}
		});
		
		panel.add(disp);
		disp.setVisible(true);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		
		sidepanel.add(disp_zoom,c);
		
		JButton next = new JButton("->"),prev = new JButton("<-");
		
		next.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    }
		});
		
		prev.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    }
		});
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		//sidepanel.add(prev,c);
		c.gridx = 1;
		//sidepanel.add(next,c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		sidepanel.add(label,c);
		label.setText("Select the line");
		
		JPanel all = new JPanel();
		
		all.add(panel);
		all.add(sidepanel);
		this.add(all);
		this.setVisible(true);
	}
	
}
