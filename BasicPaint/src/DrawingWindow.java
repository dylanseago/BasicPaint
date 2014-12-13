import java.awt.Dimension;

import javax.swing.JFrame;


public class DrawingWindow {
	private static final Dimension dim = new Dimension(1280, 720);
	
	public static void main(String[] args) {		
		JFrame myFrame = new JFrame();
		myFrame.setTitle("GUI Test");
		myFrame.setSize(dim);
		myFrame.setLocationRelativeTo(null);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.add(new DrawingPanel());
		myFrame.setVisible(true);
	}
}
