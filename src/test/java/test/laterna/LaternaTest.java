package test.laterna;

import java.awt.Desktop;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

public class LaternaTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory() //
				.setTerminalEmulatorTitle("Scraper: Cherokee Phoenix");
		
		if (Desktop.isDesktopSupported()) {
			defaultTerminalFactory.setTerminalEmulatorFontConfiguration(MyFontConfig.getDefault());
		}
		

		try (Terminal terminal = defaultTerminalFactory.createTerminal()) {
			terminal.setBackgroundColor(TextColor.ANSI.BLACK);
			terminal.setForegroundColor(TextColor.ANSI.WHITE);
			terminal.clearScreen();

			OutputStream out = new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					terminal.setBackgroundColor(TextColor.ANSI.BLACK);
					terminal.setForegroundColor(TextColor.ANSI.WHITE);
					terminal.putCharacter((char) b);
				}
				@Override
				public void flush() throws IOException {
					super.flush();
					terminal.flush();
				}
			};
			PrintStream newOut = new PrintStream(out, true, StandardCharsets.UTF_8);
//			System.setOut(newOut);

			SwingTerminal st;
			TextGraphics tg = terminal.newTextGraphics();
			
			OutputStream err = new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					terminal.setBackgroundColor(TextColor.ANSI.BLACK);
					terminal.setForegroundColor(TextColor.ANSI.YELLOW);
					terminal.putCharacter((char) b);
					if (terminal.getCursorPosition().getRow()+1>=terminal.getTerminalSize().getRows()) {
						scroll(terminal);
					}
					//terminal.putCharacter((char) b);
				}
				@Override
				public void flush() throws IOException {
					super.flush();
					terminal.flush();
				}
			};
			PrintStream newErr = new PrintStream(err, true, StandardCharsets.UTF_8);
			System.setErr(newErr);

			System.out.println(terminal.getCursorPosition());
			for (int ix=0; ix<50; ix++) {
				System.err.println("Line: "+(ix+1));
				System.out.println(terminal.getCursorPosition());
			}
			Thread.sleep(5000);
		}
	}

	protected static void scroll(Terminal terminal) throws IOException {
		terminal.putCharacter((char) 27);
		terminal.putCharacter('[');
		terminal.putCharacter('r');
		terminal.flush();
		terminal.putCharacter((char) 27);
		terminal.putCharacter('M');
		terminal.flush();
//		TextGraphics tg = terminal.newTextGraphics();
//		for (int row=1; row<terminal.getTerminalSize().getRows(); row++) {
//			int prevRow = row - 1;
//			for (int col=0; col<terminal.getTerminalSize().getColumns(); col++) {
//				TextCharacter c = tg.getCharacter(col, row);
//				tg.setCharacter(col, prevRow, c);
//			}
//		}
//		terminal.setCursorPosition(0, terminal.getTerminalSize().getRows()-2);
	}
}

class MyFontConfig extends SwingTerminalFontConfiguration {
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