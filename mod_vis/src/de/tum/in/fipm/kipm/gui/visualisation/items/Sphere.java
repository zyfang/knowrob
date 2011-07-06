package de.tum.in.fipm.kipm.gui.visualisation.items;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import edu.tum.cs.vis.Canvas;

public class Sphere extends Handle {


	private float radius;

	public Sphere(float m00, float m01, float m02, float m03, float m10,
			float m11, float m12, float m13, float m20, float m21, float m22,
			float m23, float m30, float m31, float m32, float m33, float xdim,
			float ydim, float zdim) {
		
		super(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31,
				m32, m33, xdim, ydim, zdim);
		this.radius=(float) (0.5 * Math.sqrt(xdim*xdim + ydim*ydim + zdim*zdim));
	}

	public Sphere(Matrix4d pose, Vector3d dim){
		super(pose, dim);
		this.radius=(float) (0.5 * Math.sqrt(xdim*xdim + ydim*ydim + zdim*zdim));
	}
	

	@Override
	public void drawIt(Canvas c) {
				
		c.sphere(radius);
	}
}
