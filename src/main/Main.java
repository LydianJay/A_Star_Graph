package main;
import javax.swing.JFrame;


import rendering.Render;


public class Main {
	
	public static void main(String[] args) {
		
		
		JFrame myFrame = new JFrame("A_Star - Lloyd Jay Edradan");
		Render render = new Render(800, 800);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setLocation(0, 0);
		myFrame.setResizable(false);
		myFrame.add(render);
		myFrame.addKeyListener(render);
		myFrame.addMouseListener(render);
		myFrame.addMouseMotionListener(render);
		myFrame.pack();
		myFrame.setVisible(true);
		render.update();
		
	}

}
