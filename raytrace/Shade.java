package raytrace;

public enum Shade {

	BLANK(' '),
	LIGHT('\u2591'),
	MEDIUM('\u2592'),
	DARK('\u2593'),
	FULL('\u2588');

	private static final Shade[] ALL = values();

	private final char glyph;

	Shade(char glyph) {
		this.glyph = glyph;
	}

	public char getGlyph() {
		return glyph;
	}

	public static Shade fromBrightness(int brightness) {
		return ALL[brightness / 2];
	}
}
