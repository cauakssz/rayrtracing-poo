package raytrace;

import java.util.Optional;

public final class Plane extends SceneObject {

	@Override
	public Optional<Hit> intersect(ray viewRay) {

		boolean perpendicularToPlane = Math.abs(viewRay.getD().getY()) < 0.001f;
		if(perpendicularToPlane) return Optional.empty();

		float distanceAlongRayToPlane = -viewRay.getO().getY() / viewRay.getD().getY();

		boolean intersectionIsBehindCamera = distanceAlongRayToPlane < 0.0f;
		if(intersectionIsBehindCamera) return Optional.empty();

		vec3 point = viewRay.getO().add(viewRay.getD().scale(distanceAlongRayToPlane));

		return Optional.of(new Hit(this, point, vec3.UP, distanceAlongRayToPlane));
	}

	@Override
	public int brightnessAt(Hit hit, Scene scene) {
		Light light = scene.getLight();
		ray shadowRay = light.rayFrom(hit.getPoint());
		float direct = FloatMath.clamp(9.0f - 0.25f*hit.getPoint().distance(light.getPosition().flat()), 2.0f, 9.0f);
		float shadow = scene.shadowOn(shadowRay);
		return (int) FloatMath.clamp(direct - shadow, 0.0f, 9.0f);
	}

	@Override
	public boolean isBackground() {
		return true;
	}
}
