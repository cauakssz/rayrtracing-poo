package raytrace;

import java.util.concurrent.locks.LockSupport;

class Main {

	private static final int COLUMNS = 120;
	private static final int ROWS = 30;
	private static final float DURATION = 30.0f;
	private static final float TIME_STEP = 0.033f;
	private static final long FRAME_NANOS = 33_000_000L;

	private static Scene buildScene() {

		Camera camera = new Camera(
			new vec3(-4.0f, 1.5f, 0.0f),
			new vec3(1.0f, -0.1f, 0.0f).norm(),
			COLUMNS,
			ROWS
		);
		Light light   = new Light(new vec3(3.6f, 1.5f, 0.0f), 0.3f);
		Sphere sphere = new Sphere(new vec3(0.0f, 2.0f, 0.0f), vec3.ZERO, 0.9f);
		Plane plane   = new Plane();

		return new Scene(camera, light, sphere, plane);
	}

	public static void main(String[] args) {

		Scene scene = buildScene();
		Renderer renderer = new Renderer(scene);
		Terminal terminal = new Terminal();

		terminal.clear();

		float t = 0.0f;

		while(t < DURATION) {
			t += TIME_STEP;

			LockSupport.parkNanos(FRAME_NANOS);

			terminal.draw(renderer.render());
			scene.update();
		}
	}
}
