package graphage;

import graphage.gui.MyDisplay;
// 

/**
 * Public main function
 * 
 * @author Jon Elliott
 *
 */
public class Graphage {
	public static void main(String[] args){
		System.out.println("Hello World!");
		MyDisplay p = new MyDisplay("Test");
		p.load("image.png");
		p.run();
	}
}
