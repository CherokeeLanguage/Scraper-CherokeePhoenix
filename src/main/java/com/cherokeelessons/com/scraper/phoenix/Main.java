package com.cherokeelessons.com.scraper.phoenix;

import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

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
				.setTerminalEmulatorFontConfiguration(MyFontConfig.getDefault()) //
				.setTerminalEmulatorTitle("Scraper: Cherokee Phoenix");
		

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
				}
			};
			PrintStream newOut = new PrintStream(out, true, StandardCharsets.UTF_8);
			System.setOut(newOut);

			OutputStream err = new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					terminal.setBackgroundColor(TextColor.ANSI.RED);
					terminal.setForegroundColor(TextColor.ANSI.YELLOW);
					terminal.putCharacter((char) b);
				}
			};
			PrintStream newErr = new PrintStream(err, true, StandardCharsets.UTF_8);
			System.setErr(newErr);

			new Application(terminal).run();
		} finally {

		}
	}
}
