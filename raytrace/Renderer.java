package raytrace;

import java.util.Optional;

public final class Renderer {

	private static final int BLOOM_THRESHOLD = 6;
	private static final int BLOOM_CEILING = 7;

	private final Scene scene;

	public Renderer(Scene scene) {
		this.scene = scene;
	}

	public String render() {

		Camera camera = scene.getCamera();
		int columns = camera.getColumns();
		int rows = camera.getRows();

		StringBuilder frame = new StringBuilder();

		for(int cy=-rows/2; cy<rows/2; ++cy) {
			for(int cx=-columns/2; cx<columns/2; ++cx) {
				int brightness = shade(camera.viewRay(cx, cy));
				frame.append(Shade.fromBrightness(brightness).getGlyph());
			}
			if(cy < rows/2 - 1) frame.append('\n');
		}

		return frame.toString();
	}

	private int shade(ray viewRay) {

		Optional<Hit> hit = scene.nearestHit(viewRay);

		int brightness = hit.map(h -> h.getSurface().brightnessAt(h, scene)).orElse(0);

		boolean bloomCanShow = hit.isEmpty() || hit.get().getSurface().isBackground();

		if(bloomCanShow && brightness <= BLOOM_THRESHOLD) {
			int bloomLight = (int) scene.getLight().bloom(viewRay);
			brightness = (int) FloatMath.clamp(brightness + bloomLight, 0, BLOOM_CEILING);
		}

		return brightness;
	}
}
