package app;

import java.awt.Graphics;


public class Ball {
	double[] position = new double[2];
	double[] velocity = new double[2];
	int d;
	int minV;
	double mass;
	public Ball(double startX, double startY, double _vX, double _vY, int _diameter, double _mass) {
		this.position[0] = startX;
		this.position[1] = startY;
		this.velocity[0] = _vX;
		this.velocity[1] = _vY;
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
			this.position[i] += this.velocity[i] / _0_Constants.FPS;
		}
	}
	
	public void checkForBox(int width, int height, double damp) {
		double[] futurePos = new double[2];
		for (int i = 0; i < 2; i++) {
			futurePos[i] = this.position[i] + this.velocity[i] / _0_Constants.FPS;
		}
		
		if (futurePos[0] < 0 || futurePos[0]+this.d > width) {
			this.velocity[0] *= -damp;
		}
		if (futurePos[1] < 0) {
			this.velocity[1] *= -damp;
		}
		if (futurePos[1]+this.d > height) {
			this.velocity[1] *= -damp;
		}
		
		if (futurePos[1] < 0 || futurePos[1]+this.d > height) {
			if (Math.abs(this.velocity[1]) < this.minV) {
				this.velocity[1] *= 0;
			}
		}
		
		if (futurePos[0] < 0 || futurePos[0]+this.d > width) {
			if (Math.abs(this.velocity[0]) < this.minV) {
				this.velocity[0] *= 0;
			}
		}
	}
	
	public void checkForBall(Ball[] baelle, double damp) {
		for (Ball ball : baelle) {
			if (this == ball) {
				continue;
			} else {
				if (distance(this.position[0]+this.d/2, this.position[1]+this.d/2, ball.position[0]+ball.d/2, ball.position[1]+ball.d/2) < this.d/2+ball.d/2) {
					double num = this.velocity[0]*(this.mass - ball.mass) + 2 * ball.mass * ball.velocity[0];
					double den = this.mass + ball.mass;
					double newvel = num/den;
					
					num = ball.velocity[0] * (ball.mass - this.mass) + 2 * this.mass * this.velocity[0];
					ball.velocity[0] = (num/den)*damp;
					this.velocity[0] = newvel*damp;
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
}


