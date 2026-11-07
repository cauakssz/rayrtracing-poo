public class main {

	static final float CLIP_DIST  = 0.1f;
	static final float CLIP_WIDTH = 0.2f;
	static final float GRAVITY = 0.01f;

	static float CLAMP(float X, float lo, float hi) { return (X<lo ? lo : X>hi ? hi : X); }

	static vec3 UP            = new vec3( 0.0f,  1.0f,  0.0f);
	static vec3 DOWN          = new vec3( 0.0f, -1.0f,  0.0f);
	static vec3 VEC3_ZERO     = new vec3( 0.0f,  0.0f,  0.0f);

	static vec3 LIGHTPOS      = new vec3( 3.6f,  1.5f,  0.0f);
	static float LIGHTRADIUS  = 0.3f;

	static vec3 SPHERECENTRE  = new vec3( 0.0f,  2.0f,  0.0f);
	static vec3 SPHEREVEL     = new vec3( 0.0f,  0.0f,  0.0f);
	static float SPHERERADIUS = 0.9f;

	static char[] SHADES = {32, 32, 176, 176, 177, 177, 178, 178, 219, 219};

	static int COLUMNS, ROWS;

	static boolean intersectLight(ray ray) {

		return length( cross(ray.d, sub(LIGHTPOS, ray.o)) ) < LIGHTRADIUS;
	}

	static float bloom(ray ray) {

		float dist = length( cross(ray.d, sub(LIGHTPOS, ray.o)) );

		return CLAMP(8.0f-dist*6.0f, 0.0f, 4.0f);
	}

	static boolean intersectSphere(ray viewRay, ray outNormal) {

		float b = 2.0f*dot(viewRay.d, sub(viewRay.o, SPHERECENTRE));
		float c = length2(sub(viewRay.o, SPHERECENTRE)) - SPHERERADIUS*SPHERERADIUS;

		float determinant = b*b - 4.0f*c;
		if(determinant < 0.0f) return false;

		float distanceAlongRayToSphere = 0.5f*( -b - (float) Math.sqrt(determinant) );

		boolean intersectionIsBehindCamera = distanceAlongRayToSphere < 0.0f;
		if(intersectionIsBehindCamera) return false;

		outNormal.o = add(viewRay.o, scalef(viewRay.d, distanceAlongRayToSphere));
		outNormal.d = norm(sub(outNormal.o, SPHERECENTRE));

		return true;
	}
}