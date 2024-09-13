package main;

import java.awt.*;

public class Paddle {
	int x, y, width, height;
	private int ySpeed;
	private Color color;

	// Create a paddle
	public Paddle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.ySpeed = 0;
		this.width = width;
		this.height = height;
		this.color = Color.WHITE;
	}

	// Method to draw the paddle
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

	// Method to get the bounds of the paddle
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	// Moving functions
	// Move up (change speed)
	public void moveUp() {
		this.ySpeed = -5;
	}

	// Move down(change speed)
	public void moveDown() {
		this.ySpeed = 5;
	}

	// Actually move
	public void move() {
		this.y += this.ySpeed;
	}

	// Stop
	public void stop() {
		this.ySpeed = 0;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getY() {
		return this.y;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isHit(Ball b) {
		return this.getBounds().intersects(b.getBounds());
	}
	
	public boolean hittingPoint(Ball b) {
		 double position = b.getX()-this.x;
		 return position > 90 || position < 30;
	}
}
