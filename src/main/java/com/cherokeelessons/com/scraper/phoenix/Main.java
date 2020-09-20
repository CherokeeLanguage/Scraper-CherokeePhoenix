package com.cherokeelessons.com.scraper.phoenix;

import java.awt.Desktop;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

public class Main {
	public static class MyFontConfig extends SwingTerminalFontConfiguration {
		protected MyFontConfig(boolean useAntiAliasing, BoldMode boldMode, Font[] fontsInOrderOfPriority) {
			super(useAntiAliasing, boldMode, fontsInOrderOfPriority);
		}

		public static SwingTerminalFontConfiguration getDefault() {
			Font[] monospaced = filterMonospaced(selectDefaultFont());
			Font[] larger = new Font[monospaced.length];
			for (int ix = 0; ix < monospaced.length; ix++) {
				larger[ix] = monospaced[ix].deriveFont(28f);
			}
			return newInstance(larger);
		}

	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory() //
				.setTerminalEmulatorTitle("Scraper: Cherokee Phoenix")//
				.setInitialTerminalSize(new TerminalSize(100, 30));
		
		if (Desktop.isDesktopSupported()) {
			defaultTerminalFactory.setTerminalEmulatorFontConfiguration(MyFontConfig.getDefault());
		}
		

		try (Terminal terminal = defaultTerminalFactory.createTerminal()) {
			terminal.setBackgroundColor(TextColor.ANSI.BLACK);
			terminal.setForegroundColor(TextColor.ANSI.WHITE);
			terminal.clearScreen();

			TextGraphics outText = terminal.newTextGraphics();
			outText.setForegroundColor(TextColor.ANSI.WHITE);

			OutputStream out = new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					terminal.setBackgroundColor(TextColor.ANSI.BLACK);
					terminal.setForegroundColor(TextColor.ANSI.WHITE);
					terminal.putCharacter((char) b);
					if (terminal.getCursorPosition().getRow()+1>=terminal.getTerminalSize().getRows()) {
						scroll(terminal);
					}
				}
				@Override
				public void flush() throws IOException {
					super.flush();
					terminal.flush();
				}
			};
			PrintStream newOut = new PrintStream(out, true, StandardCharsets.UTF_8);
			System.setOut(newOut);

			OutputStream err = new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					terminal.setBackgroundColor(TextColor.ANSI.BLACK);
					terminal.setForegroundColor(TextColor.ANSI.YELLOW);
					terminal.putCharacter((char) b);
					if (terminal.getCursorPosition().getRow()+1>=terminal.getTerminalSize().getRows()) {
						scroll(terminal);
					}
				}
				@Override
				public void flush() throws IOException {
					super.flush();
					terminal.flush();
				}
			};
			PrintStream newErr = new PrintStream(err, true, StandardCharsets.UTF_8);
			System.setErr(newErr);

			new Application(terminal).run();
		}
	}
	
	protected static void scroll(Terminal terminal) throws IOException {
		TextGraphics tg = terminal.newTextGraphics();
		for (int row=1; row<terminal.getTerminalSize().getRows(); row++) {
			int prevRow = row - 1;
			for (int col=0; col<terminal.getTerminalSize().getColumns(); col++) {
				TextCharacter c = tg.getCharacter(col, row);
				tg.setCharacter(col, prevRow, c);
			}
		}
		terminal.setCursorPosition(0, terminal.getTerminalSize().getRows()-2);
	}
}
