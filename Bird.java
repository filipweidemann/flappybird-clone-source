package flappybird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.ImageIO;



public class Bird implements ActionListener, MouseListener, KeyListener {

	public static Bird flappyBird;
	public final int WIDTH = 720, HEIGHT = 640;
	public static GamePanel gamePanel = new GamePanel();
	public Rectangle theFlappyBird;
	public ArrayList<Rectangle> columns;
	public Random myRandom;
	public int ticks, yOffset, score;
	public boolean gameOver, startProgram = false;
	public int counter = 0;
	public BufferedImage image;
	
		
	public static void main(String[] args) throws IOException {
		
		flappyBird = new Bird();
		TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
            		gamePanel.repaint();
            		flappyBird.actionPerformed(null);
            }
        };
        
        Timer timer = new Timer("MyTimer");//create a new Timer

        timer.scheduleAtFixedRate(timerTask, 0, 25);
		
		
	}

	
	
	public Bird() throws IOException {
		JFrame window = new JFrame();
//		Timer fps = new Timer(30, this);
		
		try {
			image = ImageIO.read(new File("duck.jpg"));
		} catch(IOException ee){
			ee.printStackTrace();
		}
		
		gamePanel = new GamePanel();
		myRandom = new Random();
		
		window.setTitle("Flappy Bird Clone");
		window.add(gamePanel);
		window.addMouseListener(this);
		window.addKeyListener(this);
		
		
		

		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setSize(WIDTH, HEIGHT);
		
		window.setVisible(true);

		theFlappyBird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		columns = new ArrayList<>();
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
//		fps.start();
	}

	
	
	public void addColumn(boolean bool) {
		int space = 225;
		int width = 80;
		int height = 50 + myRandom.nextInt(300);

		if (bool) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 150, HEIGHT - height - 85, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 150, 0, width, HEIGHT - height - space));
		} else {
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 300, HEIGHT - height - 85, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}

	}

	public void repaint(Graphics g) {
		
		
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.GREEN.darker().darker());
		g.fillRect(0, HEIGHT - 85, WIDTH, 10);

		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 75, WIDTH, 75);

		g.setColor(Color.red);
		g.fillRect(theFlappyBird.x, theFlappyBird.y, theFlappyBird.width, theFlappyBird.height);

        g.drawImage(image, 0, 0, null);
			

//		ImageIcon newIcon = new ImageIcon(newimg);
//		JLabel myLabel = new JLabel(newIcon);
//		gamePanel.add(myLabel);
		
		// HIER WIRD DAS IMAGE GELADEN!!!!11!!EINSELF
		
		
		for (Rectangle column : columns) {
			paintColumn(g, column);
		}

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 100));

		if(!startProgram){
			g.drawString("Click to start", 60, HEIGHT / 2);
		}
		
		if(!gameOver && startProgram){
			g.drawString("" + score, WIDTH / 2, 120);
		}
		
		if (gameOver) {
			// theFlappyBird.y = HEIGHT - 120; // Verhindert, dass der
			// FlappyBird durch den Boden fällt.
			g.drawString("Game Over!", 70, HEIGHT / 2 - 60);
			g.drawString("Click to reset", 35, HEIGHT / 2 + 60);
		}

	}

	public void letBirdJump(){
		if(gameOver){
			theFlappyBird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			columns.clear();
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			gameOver = false;
			yOffset = 0;
			score = 0;
		}
		
		if(!startProgram){
			startProgram = true;
		} else if(!gameOver){
			if(yOffset > 0){
				yOffset = 0;
			}
			yOffset -= 10;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		System.out.println(counter);
		counter++;
		int speed = 10;
		ticks += 1;

		
		if(startProgram && !gameOver){
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= speed;
			}

			if (ticks % 2 == 0 && yOffset < 15) {
				yOffset += 2;
			}

			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				if (column.x + column.width < 0) {
					columns.remove(column);

					if (column.y == 0){
						addColumn(false);
					}

				}
			}

			theFlappyBird.y += yOffset;

			for (Rectangle column : columns) {
				
				if(theFlappyBird.x == column.x && column.y == 0){
					score += 1;
				}
				
				if (column.intersects(theFlappyBird)) {
					gameOver = true;
				}
			}

			if (theFlappyBird.y > HEIGHT || theFlappyBird.y < 0) {
				gameOver = true;
			}
		}
			gamePanel.repaint();

		}
	
	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.GREEN);
		g.fillRect(column.x, column.y, column.width, column.height);
	}

	
	// Mouse Listener, unimplementierte Methoden
	@Override
	public void mouseClicked(MouseEvent e) {
		letBirdJump();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			letBirdJump();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}


