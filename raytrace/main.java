package raytrace;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

class Main {

	interface Surface {
		Optional<ray> intersect(ray viewRay);
	}

	static class Light {

		private final vec3 position;
		private final float radius;

		Light(vec3 position, float radius) {
			this.position = position;
			this.radius = radius;
		}

		vec3 getPosition() {
			return position;
		}

		boolean intersects(ray ray) {
			return ray.getD().cross(position.sub(ray.getO())).length() < radius;
		}

		float bloom(ray ray) {
			float dist = ray.getD().cross(position.sub(ray.getO())).length();
			return clamp(8.0f - dist*6.0f, 0.0f, 4.0f);
		}
	}

	static class Sphere implements Surface {

		private static final float GRAVITY = 0.01f;

		private vec3 center;
		private vec3 velocity;
		private final float radius;

		Sphere(vec3 center, vec3 velocity, float radius) {
			this.center = center;
			this.velocity = velocity;
			this.radius = radius;
		}

		@Override
		public Optional<ray> intersect(ray viewRay) {

			float b = 2.0f*viewRay.getD().dot(viewRay.getO().sub(center));
			float c = viewRay.getO().sub(center).length2() - radius*radius;

			float determinant = b*b - 4.0f*c;
			if(determinant < 0.0f) return Optional.empty();

			float distanceAlongRayToSphere = 0.5f*( -b - (float) Math.sqrt(determinant) );

			boolean intersectionIsBehindCamera = distanceAlongRayToSphere < 0.0f;
			if(intersectionIsBehindCamera) return Optional.empty();

			vec3 point = viewRay.getO().add(viewRay.getD().scale(distanceAlongRayToSphere));

			return Optional.of(new ray(point, point.sub(center).norm()));
		}

		float shadowOn(ray shadowRay, Light light) {

			float distToClosestDist = shadowRay.getD().dot(center.sub(shadowRay.getO()));

			if(distToClosestDist > shadowRay.getO().distance(light.getPosition()))
				return 0;

			float dist = shadowRay.getD().cross(center.sub(shadowRay.getO())).length();

			return clamp(9.0f - dist*7, 0.0f, 9.0f);
		}

		void update() {

			center = center.add(velocity).add(vec3.DOWN.scale(GRAVITY));
			velocity = velocity.add(vec3.DOWN.scale(GRAVITY));

			if(center.getY() < radius) {
				velocity = velocity.withY(velocity.getY() * -1);
				center = center.withY(radius*1.01f);
			}
		}
	}

	static class Plane implements Surface {

		@Override
		public Optional<ray> intersect(ray viewRay) {

			boolean perpendicularToPlane = Math.abs(viewRay.getD().getY()) < 0.001f;
			if(perpendicularToPlane) return Optional.empty();

			float distanceAlongRayToPlane = -viewRay.getO().getY() / viewRay.getD().getY();

			boolean intersectionIsBehindCamera = distanceAlongRayToPlane < 0.0f;
			if(intersectionIsBehindCamera) return Optional.empty();

			vec3 point = viewRay.getO().add(viewRay.getD().scale(distanceAlongRayToPlane));

			return Optional.of(new ray(point, vec3.UP));
		}

		ray shadowRay(ray normal, Light light) {
			return new ray(normal.getO(), light.getPosition().sub(normal.getO()).norm());
		}
	}

	static class Camera {

		private static final float CLIP_DIST  = 0.1f;
		private static final float CLIP_WIDTH = 0.2f;

		private vec3 eye;
		private vec3 view;
		private vec3 screenLeft;
		private vec3 screenUp;

		Camera(vec3 eye, vec3 view) {
			this.eye = eye;
			this.view = view;
			updateScreenAxes();
		}

		vec3 getEye() {
			return eye;
		}

		ray viewRay(int cx, int cy) {

			vec3 viewDir = view.scale(CLIP_DIST)
				.add(screenLeft.scale(CLIP_WIDTH*cx/(float)COLUMNS))
				.add(screenUp.scale(CLIP_WIDTH*cy/(float)(ROWS*2)));

			return new ray(eye, viewDir.norm());
		}

		void rotateY(float angle) {
			eye  = eye.rotateY(angle);
			view = view.rotateY(angle);
			updateScreenAxes();
		}

		private void updateScreenAxes() {
			screenLeft = view.cross(vec3.UP);
			screenUp   = view.cross(screenLeft);
		}
	}

	static final char[] SHADES = {' ', ' ', '\u2591', '\u2591', '\u2592', '\u2592', '\u2593', '\u2593', '\u2588', '\u2588'};

	static int COLUMNS, ROWS;

	static Camera camera = new Camera(
		new vec3(-4.0f, 1.5f, 0.0f),
		new vec3(1.0f, -0.1f, 0.0f).norm()
	);
	static Light light   = new Light(new vec3(3.6f, 1.5f, 0.0f), 0.3f);
	static Sphere sphere = new Sphere(new vec3(0.0f, 2.0f, 0.0f), vec3.ZERO, 0.9f);
	static Plane plane   = new Plane();

	static PrintStream out;

	static float clamp(float x, float lo, float hi) {
		return (x < lo ? lo : x > hi ? hi : x);
	}

	static int shade(ray viewRay) {

		int brightness = 0;

		Optional<ray> sphereHit = sphere.intersect(viewRay);

		if( light.intersects(viewRay) && (sphereHit.isEmpty() || camera.getEye().getX() > 0.0f) ) {
			brightness = 9;
		}
		else if( sphereHit.isPresent() ) {
			ray normal = sphereHit.get();
			brightness = (int)(5*(normal.getD().dot(light.getPosition().sub(normal.getO()).norm()) + 1));
			brightness = Math.min(brightness, 9);
		}
		else {
			Optional<ray> planeHit = plane.intersect(viewRay);
			if( planeHit.isPresent() ) {
				ray normal = planeHit.get();
				ray shadow = plane.shadowRay(normal, light);
				float direct = clamp(9.0f - 0.25f*normal.getO().distance(light.getPosition().flat()), 2.0f, 9.0f);
				float sphereShadow = sphere.shadowOn(shadow, light);
				brightness = (int) clamp(direct-sphereShadow, 0.0f, 9.0f);
			}
		}

		if( sphereHit.isEmpty() && brightness<=6 ) {
			int bloomLight = (int) light.bloom(viewRay);
			brightness = (int) clamp(brightness + bloomLight, 0, 7);
		}

		return brightness;
	}

	static void drawScreen() {

		StringBuilder screen = new StringBuilder();

		for(int cy=-ROWS/2; cy<ROWS/2; ++cy) {
			for(int cx=-COLUMNS/2; cx<COLUMNS/2; ++cx) {
				screen.append(SHADES[shade(camera.viewRay(cx, cy))]);
			}
			if(cy < ROWS/2 - 1) screen.append('\n');
		}

		out.print("\033[H");
		out.print(screen);
		out.flush();
	}

	static void animate() {
		sphere.update();
		camera.rotateY(0.03f);
	}

	public static void main(String[] args) throws InterruptedException {

		COLUMNS = 120;
		ROWS = 30;

		out = new PrintStream(new FileOutputStream(FileDescriptor.out), false, StandardCharsets.UTF_8);
		out.print("\033[2J");

		float t = 0.0f;

		while(t < 30.0f) {
			t += 0.033f;

			java.util.concurrent.locks.LockSupport.parkNanos(33_000_000L);

			drawScreen();
			animate();
		}
	}
}
