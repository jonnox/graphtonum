package graphage.gui;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;

public class DrawableJPanel extends JPanel {
	
	private Color col;
	private boolean crop;
	private int cx,cy,cw,ch,tmpx,tmpy, reXW, reYH;
	
	public DrawableJPanel(){
		super();
		col = new Color(0.1f, 0.1f, 0.1f, 1.0f); 
		crop = false;
		cx = cy = cw = ch = 0;
		tmpx = tmpy = 0;
	}
	
	public DrawableJPanel(LayoutManager layout){
		super(layout);
		col = new Color(0.1f, 0.1f, 0.1f, 0.6f); 
		crop = false;
	}
	
	public void setCrop(){
		crop = true;
	}
	
	public void unsetCrop(){
		crop = false;
		cx = cy = cw = ch = 0;
		tmpx = tmpy = 0;
		this.repaint();
	}
	
	public Rectangle getCropRect(int imgX, int imgY, int imgW, int imgH){
		Rectangle rec = new Rectangle(tmpx,tmpy,cw,ch);
		
		reXW = (imgX + imgW);
		reYH = (imgY + imgH);
		
		if(tmpx < imgX)
			rec.x = imgX;
		else if(tmpx > reXW)
			rec.x = reXW;
		
		if(tmpy < imgY)
			rec.y = imgY;
		else if(tmpy > reYH)
			rec.x = reYH;
		
		if((rec.x + rec.width) > reXW){
			rec.width = reXW - rec.x;
		}
		
		if((rec.y + rec.height) > reYH){
			rec.height = reYH - rec.y;
		}
		
		return rec;
	}
	
	public void updateCrop(Point p){
		cw = Math.abs(p.x - cx);
		ch = Math.abs(p.y - cy);
		if(p.x < cx)
			tmpx = p.x;
		else
			tmpx = cx;
		
		if(p.y < cy)
			tmpy = p.y;
		else
			tmpy = cy;
		
		this.repaint();
	}
	
	public void setCropPoint(Point p){
		cx = p.x;
		cy = p.y;
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		
		if(crop){
			//draw rect 
			g.drawRect(tmpx,tmpy,cw,ch);  
		  
			g.setColor(col);  

			//	fill rect
			g.fillRect(tmpx,tmpy,cw,ch);
			
			//System.out.print("(" + tmpx + "," + tmpy + "," + cw + "," + ch + ")");
			
		}
	}

}
