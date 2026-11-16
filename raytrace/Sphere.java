package raytrace;

import java.util.Optional;

public final class Sphere extends SceneObject {

	private static final float GRAVITY = 0.01f;

	private vec3 center;
	private vec3 velocity;
	private final float radius;

	public Sphere(vec3 center, vec3 velocity, float radius) {
		this.center = center;
		this.velocity = velocity;
		this.radius = radius;
	}

	@Override
	public Optional<Hit> intersect(ray viewRay) {

		float b = 2.0f*viewRay.getD().dot(viewRay.getO().sub(center));
		float c = viewRay.getO().sub(center).length2() - radius*radius;

		float determinant = b*b - 4.0f*c;
		if(determinant < 0.0f) return Optional.empty();

		float distanceAlongRayToSphere = 0.5f*( -b - (float) Math.sqrt(determinant) );

		boolean intersectionIsBehindCamera = distanceAlongRayToSphere < 0.0f;
		if(intersectionIsBehindCamera) return Optional.empty();

		vec3 point = viewRay.getO().add(viewRay.getD().scale(distanceAlongRayToSphere));

		return Optional.of(new Hit(this, point, point.sub(center).norm(), distanceAlongRayToSphere));
	}

	@Override
	public int brightnessAt(Hit hit, Scene scene) {
		vec3 towardLight = scene.getLight().directionFrom(hit.getPoint());
		int brightness = (int)(5*(hit.getNormal().dot(towardLight) + 1));
		return Math.min(brightness, 9);
	}

	@Override
	public float shadowOn(ray shadowRay, Light light) {

		float distToClosestDist = shadowRay.getD().dot(center.sub(shadowRay.getO()));

		if(distToClosestDist > light.distanceTo(shadowRay.getO()))
			return 0;

		float dist = shadowRay.getD().cross(center.sub(shadowRay.getO())).length();

		return FloatMath.clamp(9.0f - dist*7, 0.0f, 9.0f);
	}

	@Override
	public void update() {

		center = center.add(velocity).add(vec3.DOWN.scale(GRAVITY));
		velocity = velocity.add(vec3.DOWN.scale(GRAVITY));

		if(center.getY() < radius) {
			velocity = velocity.withY(velocity.getY() * -1);
			center = center.withY(radius*1.01f);
		}
	}
}
