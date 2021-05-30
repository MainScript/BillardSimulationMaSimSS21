package app;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.ApplicationTime;
import utils.FrameUpdate;

import java.util.Timer;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class Simulation {
	
	private static JFrame frame;

	public static void main(String[] args) {
		
		//open new thread for time measurement
		ApplicationTime animThread = new ApplicationTime();
		animThread.start();

		createFrame(animThread);
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new FrameUpdate(frame), 100, _0_Constants.TPF);
	}
	
	//create a JFrame as my container for the simulation content
	private static void createFrame(ApplicationTime thread) {
		
		//Create a new frame
		frame = new JFrame("Billard Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add a JPanel as the new drawing surface
		JPanel panel = new YourGraphicsContent(thread);
		frame.add(panel);
		frame.pack(); //adjusts size of the JFrame to fit the size of it's components
		frame.setVisible(true);
	}
}


@SuppressWarnings("serial") 
class YourGraphicsContent extends JPanel {
	
	//panel has a single time tracking thread associated with it
	private ApplicationTime t;
	private double time;
	
	public YourGraphicsContent(ApplicationTime thread) {
		this.t = thread;
	}
	
	//set this panel's preferred size for auto-sizing the container JFrame
	public Dimension getPreferredSize() {
		return new Dimension(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
	}
	
	int startX = 20;
	int startY = 20;
	int vX = 200;
	int vY = 0;
	int diameter = 50;
	double damp = 1;
	double mass = 1;
	
	private Ball b1 = new Ball(startX, startY, vX, vY, diameter, mass);
	private Ball b2 = new Ball(300, 30, 100, 0, diameter, mass);
	private Ball[] baelle = {b1, b2};
	
	
	//drawing operations should be done in this method
	@Override protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		time = t.getTimeInSeconds(); 
		 
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, _0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
		double[] grossV = {(b1.mass * b1.v[0] + b2.mass * b2.v[0]) / (b1.mass + b2.mass), (b1.mass * b1.v[1] + b2.mass * b2.v[1]) / (b1.mass + b2.mass)};
		//System.out.println(Arrays.toString(grossV));
		b1.calcCollisions(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT, damp, baelle, grossV);
		b2.calcCollisions(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT, damp, baelle, grossV);
		g.setColor(Color.RED);
		b1.draw(time, g, _0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT, damp, baelle);
		g.setColor(Color.GREEN);
		b2.draw(time, g, _0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT, damp, baelle);
	}
}