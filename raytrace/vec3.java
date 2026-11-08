package raytrace;

public class vec3 {

	public float x, y, z;

	public vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	static float dot(vec3 a, vec3 b) {
		return a.x*b.x + a.y*b.y + a.z*b.z;
	}

	static vec3 cross(vec3 a, vec3 b) {
		return new vec3(a.y*b.z - a.z*b.y, a.z*b.x - a.x*b.z, a.x*b.y - a.y*b.x);
	}

	static vec3 add(vec3 a, vec3 b) {
		return new vec3(a.x+b.x, a.y+b.y, a.z+b.z);
	}

	static vec3 add3(vec3 a, vec3 b, vec3 c) {
		return new vec3(a.x+b.x+c.x, a.y+b.y+c.y, a.z+b.z+c.z);
	}

	@SuppressWarnings("unused")
	static vec3 addf(vec3 a, float f) {
		return new vec3(a.x+f, a.y+f, a.z+f);
	}

	static vec3 sub(vec3 a, vec3 b) {
		return new vec3(a.x-b.x, a.y-b.y, a.z-b.z);
	}

	@SuppressWarnings("unused")
	static vec3 subf(vec3 a, float f) {
		return new vec3(a.x-f, a.y-f, a.z-f);
	}

	@SuppressWarnings("unused")
	static vec3 scale(vec3 a, vec3 b) {
		return new vec3(a.x*b.x, a.y*b.y, a.z*b.z);
	}

	static vec3 scalef(vec3 a, float f) {
		return new vec3(a.x*f, a.y*f, a.z*f);
	}

	static float length(vec3 a) {
		return (float) Math.sqrt(a.x*a.x + a.y*a.y + a.z*a.z);
	}

	static float length2(vec3 a) {
		return a.x*a.x + a.y*a.y + a.z*a.z;
	}

	static float distance(vec3 a, vec3 b) {
		return (float) Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y) + (a.z-b.z)*(a.z-b.z));
	}

	static vec3 flat(vec3 a) {
		return new vec3(a.x, 0.0f, a.z);
	}

	static vec3 norm(vec3 a) {
		float l = length(a);
		return new vec3(a.x/l, a.y/l, a.z/l);
	}

	static vec3 rotate_y(vec3 a, float angle) {
		return new vec3(
			(float) (a.x*Math.cos(angle) + a.z*Math.sin(angle)),
			a.y,
			(float) (a.x*-Math.sin(angle) + a.z*Math.cos(angle))
		);
	}
}
