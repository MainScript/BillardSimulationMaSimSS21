package app;

import java.awt.Graphics;


public class Ball {
	double[] position = new double[2];
	public double[] v = new double[2];
	double[] u = new double[2];
	double[] u_strich = new double[2];
	double[][] u_comp = new double[2][2];
	int d;
	int minV;
	public double mass;
	double[] grossV = new double[2];
	
	public Ball(double startX, double startY, double _vX, double _vY, int _diameter, double _mass) {
		this.position[0] = startX;
		this.position[1] = startY;
		this.v[0] = _vX;
		this.v[1] = _vY;
		this.mass = _mass;
		this.d = _diameter;
		this.minV = 0;
	}
	
	public void draw(double _time, Graphics g, int width, int height, double damp, Ball[] baelle) {
		this.update(_time, width, height, damp, baelle);
		g.fillOval((int)this.position[0], (int)this.position[1], (int)this.d, (int)this.d);
	}
	
	public void update(double _time, int width, int height, double damp, Ball[] baelle) {
		for (int i = 0; i < 2; i++) {
			this.position[i] += this.v[i] / _0_Constants.FPS;
		}
		this.checkForBox(width, height, damp);
		this.checkForBall(baelle, damp);
	}
	
	public void calcCollisions(int width, int height, double damp, Ball[] baelle, double[] grossV) {
		this.grossV = grossV;
		this.calcCM(baelle);
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
					this.splitU(baelle, damp);
					ball.splitU(baelle, damp);
					for(int i = 0; i < 2; i++) {
						this.v[i] = this.u_strich[i] + this.grossV[i];
						ball.v[i] = ball.u_strich[i] + this.grossV[i];
					}
					
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
	
	public void splitU(Ball[] baelle, double damp) {
		for(Ball ball :  baelle) {
			if(this == ball) {
				continue;
			} else {
				this.calcCM(baelle);
				ball.calcCM(baelle);
				double[] b = {ball.position[0] - this.position[0], ball.position[1] - this.position[1]};
				double con = (this.u[0] * b[0] + this.u[1] * b[1]) / (b[0] * b[0] + b[1] * b[1]);
				for(int i = 0; i < 2; i++) {
					this.u_comp[0][i] = con * b[i];
				}
				for(int i = 0; i < 2; i++) {
					this.u_comp[1][i] = this.u[i] - this.u_comp[0][i];
				}
				for(int i = 0; i < 2; i++) {
					this.u_comp[0][i] *= -1;
				}
				for(int i = 0; i < 2; i++) {
					this.u_strich[i] = this.u_comp[0][i] + this.u_comp[1][i];
				}
			}
		}
	}
}


