package raytrace;

public final class Hit {

	private final Surface surface;
	private final vec3 point;
	private final vec3 normal;
	private final float distance;

	public Hit(Surface surface, vec3 point, vec3 normal, float distance) {
		this.surface = surface;
		this.point = point;
		this.normal = normal;
		this.distance = distance;
	}

	public Surface getSurface() { return surface; }
	public vec3 getPoint() { return point; }
	public vec3 getNormal() { return normal; }
	public float getDistance() { return distance; }
}
