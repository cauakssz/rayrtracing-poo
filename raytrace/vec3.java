package raytrace;
 
public final class vec3 {
 
	public static final vec3 UP   = new vec3(0.0f,  1.0f, 0.0f);
	public static final vec3 DOWN = new vec3(0.0f, -1.0f, 0.0f);
	public static final vec3 ZERO = new vec3(0.0f,  0.0f, 0.0f);
 
	private final float x, y, z;
 
	public vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
 
	public float getX() { return x; }
	public float getY() { return y; }
	public float getZ() { return z; }
 
	public vec3 withY(float newY) {
		return new vec3(x, newY, z);
	}
 
	public float dot(vec3 b) {
		return x*b.x + y*b.y + z*b.z;
	}
 
	public vec3 cross(vec3 b) {
		return new vec3(y*b.z - z*b.y, z*b.x - x*b.z, x*b.y - y*b.x);
	}
 
	public vec3 add(vec3 b) {
		return new vec3(x+b.x, y+b.y, z+b.z);
	}
 
	public vec3 add(float f) {
		return new vec3(x+f, y+f, z+f);
	}
 
	public vec3 sub(vec3 b) {
		return new vec3(x-b.x, y-b.y, z-b.z);
	}
 
	public vec3 sub(float f) {
		return new vec3(x-f, y-f, z-f);
	}
 
	public vec3 scale(vec3 b) {
		return new vec3(x*b.x, y*b.y, z*b.z);
	}
 
	public vec3 scale(float f) {
		return new vec3(x*f, y*f, z*f);
	}
 
	public float length() {
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
 
	public float length2() {
		return x*x + y*y + z*z;
	}
 
	public float distance(vec3 b) {
		return (float) Math.sqrt((x-b.x)*(x-b.x) + (y-b.y)*(y-b.y) + (z-b.z)*(z-b.z));
	}
 
	public vec3 flat() {
		return new vec3(x, 0.0f, z);
	}
 
	public vec3 norm() {
		float l = length();
		return new vec3(x/l, y/l, z/l);
	}
 
	public vec3 rotateY(float angle) {
		return new vec3(
			(float) (x*Math.cos(angle) + z*Math.sin(angle)),
			y,
			(float) (x*-Math.sin(angle) + z*Math.cos(angle))
		);
	}
 
	@Override
	public String toString() {
		return "vec3(" + x + ", " + y + ", " + z + ")";
	}
}
