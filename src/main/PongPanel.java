package main;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class PongPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Paddle p1, p2, line;
	private Ball b;
	private Timer timer;
	private int p1Score, p2Score;
	private boolean p1MoveUp = false, p2MoveUp = false, p1MoveDown = false, p2MoveDown = false;

	private enum GameState {
		START_SCREEN, PLAYING, WIN
	}

	private GameState gameState;

	public PongPanel(Paddle p1, Paddle p2, Ball b) {
		this.b = b;
		this.p1 = p1;
		this.p2 = p2;
		this.p1Score = 0;
		this.p2Score = 0;
		this.line = new Paddle(700, 0, 20, 800);
		this.gameState = GameState.START_SCREEN;
		setPreferredSize(new Dimension(1423, 800));
		setBackground(Color.BLACK);
		this.timer = new Timer(10, this);
		this.timer.start();

		setupKeyBindings();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		switch (this.gameState) {
		case START_SCREEN -> drawStartScreen(g);
		case PLAYING -> drawGameElements(g);
		case WIN -> drawWinScreen(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.gameState == GameState.PLAYING) {
			this.b.move();
			this.handleP1Movement();
			this.handleP2Movement();
			this.p1.move();
			this.p2.move();
			this.checkCollision();
			this.checkBounds();
			repaint();
		}
	}

	private void handleP1Movement() {
		if (this.p1MoveUp) {
			this.p1.moveUp();
		} else if (this.p1MoveDown) {
			this.p1.moveDown();
		} else {
			this.p1.stop();
		}
	}

	private void handleP2Movement() {
		if (this.p2MoveUp) {
			this.p2.moveUp();
		} else if (this.p2MoveDown) {
			this.p2.moveDown();
		} else {
			this.p2.stop();
		}
	}

	private void startNewGame() {
		this.resetPlayersAndBall();
		this.gameState = GameState.PLAYING;
		this.timer.start();
		repaint();
	}

	private void resetPlayersAndBall() {
		this.p1.setPosition(20, 350);
		this.p2.setPosition(1380, 350);
		this.b.setPosition(712, 350);
		this.b.resetSpeed();
	}

	private void checkBounds() {
		Rectangle ballBounds = this.b.getBounds();
		if (ballBounds.getMinY() < 0 || ballBounds.getMaxY() > getHeight()) {
			this.b.bounceOffVertical();
			Sound.play("./audio/hitWall.wav");
		}
		if (ballBounds.getMinX() < 0) {
			this.p2Score++;
			Sound.play("./audio/hitWall.wav");
			this.timer.stop();
			this.handleScoring();
		}
		if (ballBounds.getMaxX() > getWidth()) {
			this.p1Score++;
			Sound.play("./audio/hitWall.wav");
			this.timer.stop();
			this.handleScoring();
		}
		this.checkPlayersBounds();

	}

	private void checkCollision() {
		if (this.p1.isHit(b) || this.p2.isHit(b)) {
			this.b.bounceOffHorizontal();
			this.b.randomAngleChange();
			Sound.play("./audio/hitPlayer.wav");
		}
	}

	private void handleScoring() {
		if (this.p1Score < 10 && this.p2Score < 10) {
			this.resetPlayersAndBall();
			this.timer.start();
			this.gameState = GameState.PLAYING;
		} else {
			this.gameState = GameState.WIN;
		}
	}

	private void checkPlayersBounds() {
		if (this.p1.getY() < 0) {
			this.p1.setY(0);
		}
		if (this.p1.getY() + this.p1.getHeight() > getHeight()) {
			this.p1.setY(getHeight() - this.p1.getHeight());
		}
		if (this.p2.getY() < 0) {
			this.p2.setY(0);
		}
		if (this.p2.getY() + this.p2.getHeight() > getHeight()) {
			this.p2.setY(getHeight() - this.p2.getHeight());
		}
	}

	private void setupKeyBindings() {
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "startGame");
		actionMap.put("startGame", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameState == GameState.START_SCREEN) {
					startNewGame();
				} else if (gameState == GameState.WIN) {
					gameState = GameState.START_SCREEN;
					p1Score = 0;
					p2Score = 0;
					repaint();
				}
			}
		});

		setupMovementKeys(inputMap, actionMap);
	}

	private void setupMovementKeys(InputMap inputMap, ActionMap actionMap) {
		// player 2
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "p2moveUpPressed");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "p2moveUpReleased");

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "p2moveDownPressed");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "p2moveDownReleased");

		actionMap.put("p2moveUpPressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				p2MoveUp = true;
				p2MoveDown = false;
			}
		});
		actionMap.put("p2moveUpReleased", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				p2MoveUp = false;
			}
		});

		actionMap.put("p2moveDownPressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				p2MoveUp = false;
				p2MoveDown = true;
			}
		});
		actionMap.put("p2moveDownReleased", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				p2MoveDown = false;
			}
		});

		// Player 1
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "p1moveUpPressed");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "p1moveUpReleased");

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "p1moveDownPressed");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "p1moveDownReleased");

		actionMap.put("p1moveUpPressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				p1MoveUp = true;
				p1MoveDown = false;
			}
		});
		actionMap.put("p1moveUpReleased", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				p1MoveUp = false;
			}
		});

		actionMap.put("p1moveDownPressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				p1MoveUp = false;
				p1MoveDown = true;
			}
		});
		actionMap.put("p1moveDownReleased", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				p1MoveDown = false;
			}
		});
	}

	private void drawStartScreen(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("PONG GAME", 600, 300);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Press ENTER to Start", 620, 350);
	}

	private void drawGameElements(Graphics g) {
		this.b.draw(g);
		this.p1.draw(g);
		this.p2.draw(g);
		this.line.draw(g);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 80));
		g.drawString("" + p1Score, 320, 100);
		g.drawString("" + p2Score, 1050, 100);
	}

	private void drawWinScreen(Graphics g) {
		int player;
		if(p2Score >= 10) {
			player = 2;
		} else {
			player = 1;
		}
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("PLAYER " + player + " WINS!", 560, 300);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Press ENTER to Restart", 610, 350);
		Sound.play("./audio/youWin.wav");
	}
}
