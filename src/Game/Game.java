package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import ui.Window;
import utils.FrameCounter;

public class Game extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	private static final int 
	cells = 100,
	cellSize = 500/cells,
	flagsScale = 10,
	flagsSize =  cells/flagsScale;
	private static boolean[][]
	buffer = new boolean[cells][cells],
	flags = new boolean[flagsSize][flagsSize];
	private boolean 
	paused = false; 
	
	FrameCounter fc = new FrameCounter();
	
	private static final long serialVersionUID = -7722583104817421521L;
	
	public Game() {

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		
		for (byte y=0; y<flagsSize; y++)
			for (byte x=0; x<flagsSize; x++) 
				flags[x][y] = true;
		
		// Rerender ever 100ms (10fps)
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Window.setTitle("Game of Life " + String.valueOf(fc.fps) + "FPS");
				
				if (!paused) update();
				
				repaint();
				fc.newframe();
			}
		}, 0, 1000/60);
	}

	public void paintComponent(Graphics canvas) {
		canvas.setColor(Color.white);
		canvas.fillRect(0, 0, 500, 500);
		canvas.setColor(Color.black);
		for (int y=0; y<cells; y++) 
			for (int x=0; x<cells; x++) 
				if (buffer[x][y]) 
					canvas.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
		
		/*
		Debug code
		canvas.setColor(Color.green);
		int flagsrendersize = 500/flagsSize;
		for (int y=0; y<flagsSize; y++) 
			for (int x=0; x<flagsSize; x++) 
				if (flags[x][y])
					canvas.drawRect(x*flagsrendersize, y*flagsrendersize, flagsrendersize, flagsrendersize);
		*/
	}

	private void update() {
		boolean[][] image = new boolean[cells][cells];
		
		for (int fy=0; fy<flagsSize; fy++) 
			for (int fx=0; fx<flagsSize; fx++) {
				boolean changed = false;
				if (hasChanged(fx,fy) || hasChanged(fx+1,fy) || hasChanged(fx,fy+1) || hasChanged(fx-1,fy) || hasChanged(fx,fy-1 ))
					for (int y=fy*flagsScale; y<fy*flagsScale+flagsScale; y++)
						for (int x=fx*flagsScale; x<fx*flagsScale+flagsScale; x++) {
							byte neighbours = getNumneighbours(x, y);
							boolean alive = buffer[x][y];
							if (alive)
								if (neighbours < 2) {
									alive = false;
								} else if (neighbours > 3) {
									alive = false;
								} else {
									alive = true;
								}
							else
								if (neighbours == 3) 
									alive = true;
							
							if (alive)
								changed = true;
							image[x][y] = alive;
						}
				flags[fx][fy] = changed;
			}
		
		buffer = image;
	}
	
	private static byte getNumneighbours(int x, int y) {
		byte result = 0;
		result += (isAlive(x-1, y-1)) ? 1 : 0;
		result += (isAlive(x,   y-1)) ? 1 : 0;
		result += (isAlive(x+1, y-1)) ? 1 : 0;
		result += (isAlive(x+1, y))   ? 1 : 0;
		result += (isAlive(x+1, y+1)) ? 1 : 0;
		result += (isAlive(x, y+1))   ? 1 : 0;
		result += (isAlive(x-1, y+1)) ? 1 : 0;
		result += (isAlive(x-1, y))   ? 1 : 0;
		
		return result;
	}
	
	private static boolean isAlive(int x, int y) {
		if (x < 0) 			x = cells-x;
		if (x >= cells) 	x = x-cells;
		if (y < 0) 			y = cells-y;
		if (y >= cells) 	y = y-cells;
		return buffer[x][y];
	}
	
	private static boolean hasChanged(int x, int y) {
		if (x < 0) 				return false;
		if (x >= flagsSize) 	return false;
		if (y < 0) 				return false;
		if (y >= flagsSize) 	return false;
		return flags[x][y];
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_SPACE) 
			paused = !paused;
		
		if (key.getKeyCode() == KeyEvent.VK_Q) // Clear
			buffer = new boolean[cells][cells]; 
	}

	@Override
	public void keyReleased(KeyEvent key) {}

	@Override
	public void keyTyped(KeyEvent key) {}

	@Override
	public void mouseDragged(MouseEvent mouse) {
		int x = mouse.getX() / cellSize;
		int y = mouse.getY() / cellSize;
		if (x < 0) 		return;
		if (x >= cells) return;
		if (y < 0) 		return;
		if (y >= cells) return;
		
		buffer[x][y] = true;
		flags[x/flagsScale][y/flagsScale] = true;
	}

	@Override
	public void mouseMoved(MouseEvent mouse) {}

	@Override
	public void mouseClicked(MouseEvent mouse) {}

	@Override
	public void mouseEntered(MouseEvent mouse) {}

	@Override
	public void mouseExited(MouseEvent mouse) {}

	@Override
	public void mousePressed(MouseEvent mouse) {}

	@Override
	public void mouseReleased(MouseEvent mouse) {}
}