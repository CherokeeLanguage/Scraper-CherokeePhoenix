package com.cherokeelessons.com.scraper.phoenix;

import java.awt.EventQueue;

import com.cherokeelessons.gui.MainWindow;
import com.cherokeelessons.gui.MainWindow.Config;


public class Main {

	public static void main(String[] args) {
		MainWindow.Config config = new Config() {
			@Override
			public String getApptitle() {
				return "Scraper: Cherokee Phoenix";
			}
			
			@Override
			public Thread getApp(String... args) throws Exception {
				return new Application();
			}
		};
		EventQueue.invokeLater(new MainWindow(config, args));
	}
}
