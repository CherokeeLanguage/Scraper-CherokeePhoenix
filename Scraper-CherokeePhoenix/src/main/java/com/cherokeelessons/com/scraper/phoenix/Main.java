package com.cherokeelessons.com.scraper.phoenix;

import java.awt.EventQueue;

import com.newsrx.gui.MainWindow;
import com.newsrx.gui.MainWindow.Config;

public class Main {

	public static void main(String[] args) {
		MainWindow.Config config = new Config() {
			@Override
			public String getApptitle() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Thread getApp(String... args) throws Exception {
				return new Application(args);
			}
		};
		EventQueue.invokeLater(new MainWindow(config, args));
	}
}
