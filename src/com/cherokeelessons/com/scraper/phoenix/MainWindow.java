package com.cherokeelessons.com.scraper.phoenix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

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
		frame = new JFrame();
		frame.setTitle("Scraper-SEC");
		frame.setBounds(100, 100, 1068, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JTextPane txtpnStartup = new JTextPane();
		txtpnStartup.setText("Scraper-SEC startup...");
		scrollPane.setViewportView(txtpnStartup);

		MessageConsole mc = new MessageConsole(txtpnStartup);
		mc.redirectOut(Color.BLUE, null);
		mc.redirectErr(Color.RED, null);
	}

}
