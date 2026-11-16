package raytrace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class Scene {

	private final Camera camera;
	private final Light light;
	private final List<Surface> surfaces = new ArrayList<>();

	public Scene(Camera camera, Light light, Surface... surfaces) {
		this.camera = camera;
		this.light = light;
		Collections.addAll(this.surfaces, surfaces);
		this.surfaces.add(light);
	}

	public Camera getCamera() {
		return camera;
	}

	public Light getLight() {
		return light;
	}

	public Optional<Hit> nearestHit(ray viewRay) {

		Optional<Hit> nearest = Optional.empty();

		for(Surface surface : surfaces) {
			Optional<Hit> hit = surface.intersect(viewRay);
			if(hit.isPresent() && (nearest.isEmpty() || hit.get().getDistance() < nearest.get().getDistance())) {
				nearest = hit;
			}
		}

		return nearest;
	}

	public float shadowOn(ray shadowRay) {

		float total = 0.0f;

		for(Surface surface : surfaces) {
			total += surface.shadowOn(shadowRay, light);
		}

		return total;
	}

	public void update() {
		for(Surface surface : surfaces) {
			surface.update();
		}
		camera.update();
	}
}
