package raytrace;

public abstract class SceneObject implements Surface {

	@Override
	public float shadowOn(ray shadowRay, Light light) {
		return 0.0f;
	}

	@Override
	public void update() {
	}

	@Override
	public boolean isBackground() {
		return false;
	}
}
