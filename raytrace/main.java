package raytrace;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static raytrace.vec3.*;

class Main {

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

	// em Java, char 176..219 sao outros simbolos; use os blocos Unicode
	static char[] SHADES = {' ', ' ', '\u2591', '\u2591', '\u2592', '\u2592', '\u2593', '\u2593', '\u2588', '\u2588'};

	static int COLUMNS, ROWS;

	// no C, eye e view eram passados por ponteiro; aqui viram campos estaticos
	static vec3 eye  = new vec3(-4.0f, 1.5f, 0.0f);
	static vec3 view = norm(new vec3(1.0f, -0.1f, 0.0f));

	static PrintStream out;

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

	static float shadowOfSphere(ray ray) {

		float distToClosestDist = dot(ray.d, sub(SPHERECENTRE, ray.o));

		if(distToClosestDist > distance(ray.o, LIGHTPOS))
			return 0;

		float dist = length( cross(ray.d, sub(SPHERECENTRE, ray.o)) );

		return CLAMP(9.0f - dist*7, 0.0f, 9.0f);
	}

	static boolean intersectPlane(ray viewRay, ray outNormal, ray outShadow) {

		boolean perpendicularToPlane = Math.abs(viewRay.d.y) < 0.001f;
		if(perpendicularToPlane) return false;

		float distanceAlongRayToPlane = -viewRay.o.y / viewRay.d.y;

		boolean intersectionIsBehindCamera = distanceAlongRayToPlane < 0.0f;
		if(intersectionIsBehindCamera) return false;

		outNormal.o = add(viewRay.o, scalef(viewRay.d, distanceAlongRayToPlane));
		outNormal.d = UP;

		outShadow.o = outNormal.o;
		outShadow.d = norm( sub(LIGHTPOS, outShadow.o) );

		return true;
	}

	static void drawScreen() {

		StringBuilder screen = new StringBuilder();

		vec3 screenLeft = cross(view, UP);
		vec3 screenUp   = cross(view, screenLeft);

		for(int cy=-ROWS/2; cy<ROWS/2; ++cy) {
			for(int cx=-COLUMNS/2; cx<COLUMNS/2; ++cx) {

				vec3 viewDir = add3(
					scalef(view, CLIP_DIST),
					scalef(screenLeft, CLIP_WIDTH*cx/(float)COLUMNS),
					scalef(screenUp,   CLIP_WIDTH*cy/(float)(ROWS*2))
				);

				ray viewRay = new ray(eye, norm(viewDir));
				ray normal  = new ray(VEC3_ZERO, VEC3_ZERO);
				ray shadow  = new ray(VEC3_ZERO, VEC3_ZERO);

				int light = 0;

				if( intersectLight(viewRay) && (!intersectSphere(viewRay, normal) || eye.x > 0.0f) ) {
					light = 9;
				}
				else if( intersectSphere(viewRay, normal) ) {
					light = (int)(5*(dot(normal.d, norm(sub(LIGHTPOS, normal.o))) + 1));
					light = Math.min(light, 9); // no C isso podia estourar o vetor
				}
				else if( intersectPlane(viewRay, normal, shadow) ) {
					float direct = CLAMP(9.0f - 0.25f*distance(normal.o, flat(LIGHTPOS)), 2.0f, 9.0f);
					float sphereShadow = shadowOfSphere(shadow);
					light = (int) CLAMP(direct-sphereShadow, 0.0f, 9.0f);
				}

				if( !intersectSphere(viewRay, normal) && light<=6 ) {
					int bloomLight = (int) bloom(viewRay);
					light = (int) CLAMP(light + bloomLight, 0, 7);
				}

				screen.append(SHADES[light]);
			}
			if(cy < ROWS/2 - 1) screen.append('\n');
		}

		out.print("\033[H"); // volta o cursor para o topo
		out.print(screen);
		out.flush();
	}

	static void animate() {

		SPHERECENTRE = add( add(SPHERECENTRE, SPHEREVEL), scalef(DOWN, GRAVITY) );
		SPHEREVEL = add( SPHEREVEL, scalef(DOWN, GRAVITY) );

		if(SPHERECENTRE.y < SPHERERADIUS) {
			SPHEREVEL.y *= -1;
			SPHERECENTRE.y = SPHERERADIUS*1.01f;
		}

		eye  = rotate_y(eye,  0.03f);
		view = rotate_y(view, 0.03f);
	}

	public static void main(String[] args) throws InterruptedException {

		// Java nao tem GetConsoleScreenBufferInfo, entao o tamanho e fixo
		COLUMNS = 120;
		ROWS = 30;

		out = new PrintStream(new FileOutputStream(FileDescriptor.out), false, StandardCharsets.UTF_8);
		out.print("\033[2J"); // limpa a tela

		float t = 0.0f;

		while(t < 30.0f) {
			t += 0.033f;

			java.util.concurrent.locks.LockSupport.parkNanos(33_000_000L);

			drawScreen();
			animate();
		}
	}
}
