package com.cherokeelessons.com.scraper.phoenix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class MainWindow {

	private JFrame frame;

	static private String[] args;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MainWindow.args = args.clone();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
					window.frame.setExtendedState(window.frame.getExtendedState());// | JFrame.MAXIMIZED_HORIZ);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		/**
		 * start the main program logic in a new thread so that the UI can
		 * update itself in this thread during long running operations, like
		 * huge pdf uploads
		 */
		System.out.println("Starting Application Thread");
		new Thread(new Application(args)).start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Calendar cal = Calendar.getInstance();

		JScrollPane scrollPane = new JScrollPane();
		JTextPane txtpnStartup = new JTextPane();

		frame = new JFrame();
		frame.setVisible(true);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screen_width = gd.getDisplayMode().getWidth();
		int width = screen_width * 75 / 100;
		int screen_height = gd.getDisplayMode().getHeight();
		int height = screen_height * 75 / 100;

		frame.setTitle("Cherokee Phoenix Scraper");
		frame.setBounds((screen_width - width) / 2, (screen_height - height) / 2, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(txtpnStartup);
		
		MessageConsole mc = new MessageConsole(txtpnStartup);
		
		mc.redirectOut(Color.BLUE, System.out);
		mc.redirectErr(Color.RED, System.err);
	}

}
