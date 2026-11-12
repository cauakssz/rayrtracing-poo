package raytrace;
 
public final class ray {
 
	private final vec3 o, d;
 
	public ray(vec3 o, vec3 d) {
		this.o = o;
		this.d = d;
	}
 
	public vec3 getO() { return o; }
	public vec3 getD() { return d; }
 
	@Override
	public String toString() {
		return "ray(o=" + o + ", d=" + d + ")";
	}
}
