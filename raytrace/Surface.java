package raytrace;

import java.util.Optional;

public interface Surface {

	Optional<Hit> intersect(ray viewRay);

	int brightnessAt(Hit hit, Scene scene);

	float shadowOn(ray shadowRay, Light light);

	void update();

	boolean isBackground();
}
