package raytrace;

import java.util.Optional;

public final class Light extends SceneObject {

	private static final int CORE_BRIGHTNESS = 9;

	private final vec3 position;
	private final float radius;

	public Light(vec3 position, float radius) {
		this.position = position;
		this.radius = radius;
	}

	public vec3 getPosition() {
		return position;
	}

	public vec3 directionFrom(vec3 point) {
		return position.sub(point).norm();
	}

	public ray rayFrom(vec3 point) {
		return new ray(point, directionFrom(point));
	}

	public float distanceTo(vec3 point) {
		return point.distance(position);
	}

	public float bloom(ray viewRay) {
		float dist = viewRay.getD().cross(position.sub(viewRay.getO())).length();
		return FloatMath.clamp(8.0f - dist*6.0f, 0.0f, 4.0f);
	}

	@Override
	public Optional<Hit> intersect(ray viewRay) {
		vec3 toLight = position.sub(viewRay.getO());
		float distanceAlongRay = viewRay.getD().dot(toLight);
		float distanceFromRay = viewRay.getD().cross(toLight).length();

		if(distanceAlongRay < 0.0f || distanceFromRay >= radius) return Optional.empty();

		vec3 point = viewRay.getO().add(viewRay.getD().scale(distanceAlongRay));

		return Optional.of(new Hit(this, point, viewRay.getD().scale(-1.0f), distanceAlongRay));
	}

	@Override
	public int brightnessAt(Hit hit, Scene scene) {
		return CORE_BRIGHTNESS;
	}
}
