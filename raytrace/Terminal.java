package raytrace;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class Terminal {

	private static final String CLEAR_SCREEN = "\033[2J";
	private static final String CURSOR_HOME  = "\033[H";

	private final PrintStream out;

	public Terminal() {
		this.out = new PrintStream(new FileOutputStream(FileDescriptor.out), false, StandardCharsets.UTF_8);
	}

	public void clear() {
		out.print(CLEAR_SCREEN);
	}

	public void draw(String frame) {
		out.print(CURSOR_HOME);
		out.print(frame);
		out.flush();
	}
}
