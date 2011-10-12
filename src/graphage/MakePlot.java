package graphage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MakePlot {
  static public void main(String args[]) throws Exception {
    try {
      int width = 200, height = 400;

      // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
      // into integer pixels
      BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

      Graphics2D ig2 = bi.createGraphics();

      ig2.setPaint(Color.black);
      int prevX = 0, prevY = height, tmpy;
      for(int i = 1; i < 90; i++){
    	  tmpy = (height - ((int)(i * i))/32);
    	  ig2.drawLine(prevX, prevY, i * 2, tmpy);
    	  prevY = tmpy;
    	  prevX = i * 2;
      }

      //ImageIO.write(bi, "PNG", new File("c:\\yourImageName.PNG"));
      //ImageIO.write(bi, "JPEG", new File("c:\\yourImageName.JPG"));
      //ImageIO.write(bi, "gif", new File("c:\\yourImageName.GIF"));
      ImageIO.write(bi, "PNG", new File("PARABOLA.PNG"));
      
    } catch (IOException ie) {
      ie.printStackTrace();
    }

  }
}