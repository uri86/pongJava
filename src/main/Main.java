package main;

import java.awt.Color;

public class Main {
	public static void main(String[] args) {
		Ball b = new Ball(650, 500, 20, -2, -2, Color.WHITE);
		Paddle p1 = new Paddle(20,400,20,120);
		Paddle p2 = new Paddle(1400,400,20,120);
		PongPanel p = new PongPanel(p1,p2,b);
		Screen.createAndShowGUI(p);
	}
}
