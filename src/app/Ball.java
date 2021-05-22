package app;

import java.awt.Graphics;
import java.util.Arrays;


public class Ball {
	double[] position = new double[2];
	double[] v = new double[2];
	double[] u = new double[2];
	double[][] u_comp = new double[2][2];
	int d;
	int minV;
	double mass;
	
	public Ball(double startX, double startY, double _vX, double _vY, int _diameter, double _mass) {
		this.position[0] = startX;
		this.position[1] = startY;
		this.v[0] = _vX;
		this.v[1] = _vY;
		this.mass = _mass;
		this.d = _diameter;
	}
	
	public void draw(double _time, Graphics g, int width, int height, double damp, Ball[] baelle) {
		this.minV = 0;
		this.update(_time, width, height, damp, baelle);
		g.fillOval((int)this.position[0], (int)this.position[1], (int)this.d, (int)this.d);
	}
	
	public void update(double _time, int width, int height, double damp, Ball[] baelle) {
		this.checkForBox(width, height, damp);
		this.checkForBall(baelle, damp);
		for (int i = 0; i < 2; i++) {
			this.position[i] += this.v[i] / _0_Constants.FPS;
		}
	}
	
	public void checkForBox(int width, int height, double damp) {
		double[] futurePos = new double[2];
		for (int i = 0; i < 2; i++) {
			futurePos[i] = this.position[i] + this.v[i] / _0_Constants.FPS;
		}
		
		if (futurePos[0] < 0 || futurePos[0]+this.d > width) {
			this.v[0] *= -damp;
		}
		if (futurePos[1] < 0) {
			this.v[1] *= -damp;
		}
		if (futurePos[1]+this.d > height) {
			this.v[1] *= -damp;
		}
		
		if (futurePos[1] < 0 || futurePos[1]+this.d > height) {
			if (Math.abs(this.v[1]) < this.minV) {
				this.v[1] *= 0;
			}
		}
		
		if (futurePos[0] < 0 || futurePos[0]+this.d > width) {
			if (Math.abs(this.v[0]) < this.minV) {
				this.v[0] *= 0;
			}
		}
	}
	
	public void checkForBall(Ball[] baelle, double damp) {
		for (Ball ball : baelle) {
			if (this == ball) {
				continue;
			} else {
				if (distance(this.position[0]+this.d/2, this.position[1]+this.d/2, ball.position[0]+ball.d/2, ball.position[1]+ball.d/2) < this.d/2+ball.d/2) {
					this.splitU(baelle);
					
					double num = this.v[0]*(this.mass - ball.mass) + 2 * ball.mass * ball.v[0];
					double den = this.mass + ball.mass;
					double newvel = num/den;
					
					num = ball.v[0] * (ball.mass - this.mass) + 2 * this.mass * this.v[0];
					ball.v[0] = (num/den)*damp;
					this.v[0] = newvel*damp;
				}
			}
		}
	}
	
	public double distance(double x1, double y1, double x2, double y2) {
		double dist = 0;
		double num1 = Math.pow(x2 - x1, 2);
		double num2 = Math.pow(y2 - y1, 2);
		dist = Math.sqrt(num1 + num2);
		return dist;
	}
	
	public void calcCM(Ball[] baelle) {
		for(Ball ball :  baelle) {
			if(this == ball) {
				continue;
			} else {
				for(int i = 0; i < 2; i++) {
					this.u[i] = (ball.mass/(this.mass + ball.mass)) * (this.v[i] - ball.v[i]);
				}
			}
		}
	}
	
	public void splitU(Ball[] baelle) {
		this.calcCM(baelle);
		for(Ball ball :  baelle) {
			if(this == ball) {
				continue;
			} else {
				double[] b = {ball.position[0] - this.position[0], ball.position[1] - this.position[1]};
				System.out.println(b[1]);
				for(int i = 0; i < 2; i++) {
					this.u_comp[0][i] = (this.u[i] - b[i])/(b[i] * b[i]) * b[i];
				}
				for(int i = 0; i < 2; i++) {
					this.u_comp[1][i] = this.u[i] - this.u_comp[0][i];
				}
			}
		}
		
		System.out.println(Arrays.toString(this.u));
		double[] test = {this.u_comp[0][0] + this.u_comp[1][0], this.u_comp[0][1] + this.u_comp[1][1]};
		System.out.println(Arrays.toString(test));
	}
}


