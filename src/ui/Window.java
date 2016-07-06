package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import Game.Game;


public class Window {
	
	public static void main(String arg[]) {
		new Window("Game of Life", 510, 520, new Game());
	}
	
	private static JFrame frame = new JFrame();
	
	public Window(String Title, int Width, int Height, Component contents) {
		frame.getContentPane().add(contents);
		contents.setFocusable(true);
		frame.setSize(Width,Height);
		frame.setResizable(false);
		frame.setTitle(Title);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public static void setTitle(String title) {
		frame.setTitle(title);
	}
}
