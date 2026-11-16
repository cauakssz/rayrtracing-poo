package raytrace;

public final class FloatMath {

	private FloatMath() {
	}

	public static float clamp(float x, float lo, float hi) {
		return (x < lo ? lo : x > hi ? hi : x);
	}
}
