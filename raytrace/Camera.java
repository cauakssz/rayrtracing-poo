package raytrace;

public final class Camera {

	private static final float CLIP_DIST  = 0.1f;
	private static final float CLIP_WIDTH = 0.2f;
	private static final float ROTATION_STEP = 0.03f;

	private final int columns;
	private final int rows;

	private vec3 eye;
	private vec3 view;
	private vec3 screenLeft;
	private vec3 screenUp;

	public Camera(vec3 eye, vec3 view, int columns, int rows) {
		this.eye = eye;
		this.view = view;
		this.columns = columns;
		this.rows = rows;
		updateScreenAxes();
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public ray viewRay(int cx, int cy) {

		vec3 viewDir = view.scale(CLIP_DIST)
			.add(screenLeft.scale(CLIP_WIDTH*cx/(float)columns))
			.add(screenUp.scale(CLIP_WIDTH*cy/(float)(rows*2)));

		return new ray(eye, viewDir.norm());
	}

	public void update() {
		rotateY(ROTATION_STEP);
	}

	private void rotateY(float angle) {
		eye  = eye.rotateY(angle);
		view = view.rotateY(angle);
		updateScreenAxes();
	}

	private void updateScreenAxes() {
		screenLeft = view.cross(vec3.UP);
		screenUp   = view.cross(screenLeft);
	}
}
