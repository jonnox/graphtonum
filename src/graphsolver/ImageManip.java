package graphsolver;

import graphage.gui.MyDisplay;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;

import com.sun.media.jai.widget.DisplayJAI;

public class ImageManip {
	
	
	/**
	 * Maximum naive distance
	 */
	private final int MAX_DISTANCE = 676;
	
	private Raster raster;
	int r_w;
	int r_h;
	
	public ImageManip(Raster r){
		this.raster = r;
		r_w = raster.getWidth();
		r_h = raster.getHeight();
	}
	
	/**
	 * Splits the current image into <code>k</code> images. The images
	 * are split by a k-means clustering technique
	 *  
	 * @param k number of cluster 
	 * @param bw <code>True</code> if you want the layers to be returned black and white
	 * @return
	 */
	public WritableRaster[] splitImage(int k, boolean bw){
		byte size = (byte) k;
		byte[][] clusterAssign;
		int[] tmppix = new int[4];
		
		if(k > 127)
			size = 127;
		else if (k < 3)
			size = 3;
		
		// All rasters are initalized as [0,0,0] pixel grids
		WritableRaster[] wr = new WritableRaster[size];
		for(int i=0; i<size; i++){
			wr[i] = raster.createCompatibleWritableRaster();
		}
		
		int[][] list = makeInitialList(size);
		int[][] newList;
		
		int count = 0;
		
		clusterAssign = assignToCluster(list, size);
		newList = recalculateMean(clusterAssign,size);

		//while(isSigchange(list,newList,size,1)){
		while(isSigchange(list,newList,size)){
			list = newList;
			clusterAssign = assignToCluster(list, size);
			newList = recalculateMean(clusterAssign,size);
			count++;
		}
		
		//printList(newList,size);
		//System.out.println("Count: " + count);
		int[] white = {255,255,255};
		if(bw){
			for(int i=0; i<r_h;i++){
				for(int j=0;j<r_w;j++){
					wr[clusterAssign[i][j]].setPixel(j, i, white);
				}
			}
		}else{
			for(int i=0; i<r_h;i++){
				for(int j=0;j<r_w;j++){
					wr[clusterAssign[i][j]].setPixel(j, i, raster.getPixel(j, i, white));
				}
			}
		}
		
		for(int i=2; i <size;i++){
			//MyDisplay md = new MyDisplay("" + 0);
			displayRaster(wr[i],"" + i,i);
			//md.run();
		}
		
		return wr;
	}
	
	private void displayRaster(WritableRaster wr, String _title, int pos){
		JFrame frame;
		BufferedImage buff_image = new BufferedImage(wr.getWidth(),wr.getHeight(),BufferedImage.TYPE_INT_RGB);
		frame = new JFrame(_title);
		frame.setSize(wr.getWidth(),wr.getHeight());
		frame.setLocation((pos * 10), (pos * 10));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buff_image.setData(wr);
		frame.add(new DisplayJAI(buff_image));
		frame.setVisible(true);
	}
	
	private void printList(int[][] list, byte size){
		System.out.println("List:");
		for(int i=0;i<size;i++)
			System.out.println(" [" + list[i][0] + "," + list[i][1] + "," + list[i][2] + "]");
	}
	
	/**
	 * Assignes each pixel in <code>raster</code> to a cluster
	 * @param list cluster list
	 * @param size number of clusters
	 * @return pixel assignment list
	 */
	private byte[][] assignToCluster(int[][] list, byte size){
		byte[][] clusterAssign = new byte[r_h][r_w];
		int d1, d2, tmp1,tmp2;
		byte index = 0;
		int[] tmppix = new int[4];
		
		for(int i=0;i<r_h;i++){
			for(int j=0;j<r_w;j++){
				index = 0;
				tmppix = raster.getPixel(j, i, tmppix);
				d1 = Math.abs(tmppix[0] - list[0][0]) +
						Math.abs(tmppix[1] - list[0][1]) +
						Math.abs(tmppix[2] - list[0][2]);
				for(byte k = 1; k < size; k++){
					d2 = Math.abs(tmppix[0] - list[k][0]) +
							Math.abs(tmppix[1] - list[k][1]) +
							Math.abs(tmppix[2] - list[k][2]);
					if(d2 < d1){
						d1 = d2;
						index = k;
					}
				}
				clusterAssign[i][j] = index;
			}
		}
		
		return clusterAssign;
	}
	
	/**
	 * Calculates if there is ANY change in the cluster mean lists
	 * @param oldList
	 * @param newList
	 * @param size
	 * @return if a significant change has been made or not
	 */
	private boolean isSigchange(int[][] oldList, int[][] newList, byte size){
		boolean isChange = false;
		for(int i=0; i<size;i++){
			for(int j=0;j<3;j++){
				if(oldList[i][j] != newList[i][j]){
					isChange = true;
					break;
				}
			}
			if(isChange)
				break;
		}
		return isChange;
	}
	
	/**
	 * Calculates if there is a change more than <code>threashold</code> in the cluster
	 * mean lists
	 * @param oldList
	 * @param newList
	 * @param size
	 * @param threashold
	 * @return
	 */
	private boolean isSigchange(int[][] oldList, int[][] newList, byte size, int threashold){
		boolean isChange = false;
		for(int i=0; i<size;i++){
			for(int j=0;j<3;j++){
				if(Math.abs(oldList[i][j] - newList[i][j]) > threashold) {
					isChange = true;
					break;
				}
			}
			if(isChange)
				break;
		}
		return isChange;
	}
	
	private int[][] recalculateMean(byte[][] clusterAssign, byte size){
		int[][] list = new int[size][4];
		int[] tmppix = new int[4];
		for(int i=0;i<r_h;i++){
			for(int j=0;j<r_w;j++){
				tmppix = raster.getPixel(j, i, tmppix);
				list[clusterAssign[i][j]][0] += tmppix[0];
				list[clusterAssign[i][j]][1] += tmppix[1];
				list[clusterAssign[i][j]][2] += tmppix[2];
				list[clusterAssign[i][j]][3]++;
			}
		}
		for(int i=0; i < size; i++){
			if(list[i][3] < 1)
				list[i][3] = 1;
			for(int j=0; j < 3; j++){
				list[i][j] = list[i][j] / list[i][3];
			}
		}
		return list;
	}
	
	/**
	 * Creates initial list for k-mean clustering
	 * @param size
	 * @return
	 */
	private int[][] makeInitialList(byte size){
		int[][] list = new int[size][3];
		int[] tmppix = new int[4];
		boolean done = false, good;
		int i = 0, j = 2;
		tmppix  = raster.getPixel(0, 0, tmppix);
		list[0][0] = 0;
		list[0][1] = 0;
		list[0][2] = 0;
		list[1][0] = 255;
		list[1][1] = 255;
		list[1][2] = 255;
		
		while(!done){
			if(i < r_h){
				good = true;
				for(int ki=0;ki<r_w;ki++){
					tmppix = raster.getPixel(ki, i, tmppix);
					for(int tmp = 0; tmp < j; tmp++){
						if(tmppix[0] == list[tmp][0] && tmppix[1] == list[tmp][1]
								&& tmppix[2] == list[tmp][2]){
							good = false;
							break;
						}
					}
					if(good){
						list[j][0] = tmppix[0];
						list[j][1] = tmppix[1];
						list[j][2] = tmppix[2];
						j++;
					}
					if(j >= size){
						done = true;
						break;
					}
				}
				i++;
			}else{
				done = true;
			}
		}
		return list;
	}

	/**
	* Finds the closest point in the given list to the given point.
	* This algorithm simply finds the distances in each R,G,B component
	* and sums them to quickly estimate distance
	* 	
 	* @param p RGB point
 	* @param list list of points
 	* @return index of a point in <code>list</code> that <code>p</code> is nearest to 
 	*/
	private int fastClosest(int[] p, int[][] list){
		int i = 0; // index of closest point thus far
		int len = MAX_DISTANCE; // length of the closest distance thus far
		int tmp; // used for temporarly storing the current distance
		for(int j=0; j<list.length;j++){
			tmp = Math.abs(list[j][0] - p[0]);
			tmp = tmp + Math.abs(list[j][1] - p[1]);
			tmp = tmp + Math.abs(list[j][2] - p[2]);
			if(tmp < len){
				i = j;
				len = tmp;
			}
		}
		return i;
	}
}
