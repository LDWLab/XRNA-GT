package ssview;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

public class Vector2 {
	public double x, y;
	
	private static final double DEFAULT_EPSILON = 1E-4d;
	
	private static boolean areEqual(double a, double b) {
		return areEqual(a, b, DEFAULT_EPSILON);
	}
	
	private static boolean areEqual(double a, double b, double epsilon) {
		return compare(a, b, epsilon) == 0;
	}
	
	private static int compare(double a, double b) {
		return compare(a, b, DEFAULT_EPSILON);
	}
	
	private static int compare(double a, double b, double epsilon) {
		double dif = a - b;
		if (dif < -epsilon) {
			return -1;
		} else if (dif > epsilon) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public static boolean inBounds(double x, double x0, double x1) {
		return compareBounds(x, x0, x1) == 0;
	}
	
	private static int compareBounds(double x, double x0, double x1) {
		int cmp = compare(x, x0);
		if (cmp < 0) {
			return cmp;
		}
		cmp = compare(x, x1);
		if (cmp > 0) {
			return cmp;
		}
		return 0;
	}
	
	public Vector2() {
		this(0f, 0f);
	}
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(new double[] {x, y});
	}
	
	@Override
	public String toString() {
		return "<" + this.x + "," + this.y + ">";
	}
	
	public double magnitude() {
		return (double)Math.sqrt(dot(this, this));
	}
	
	public int roundedX() {
		return (int)this.x;
	}
	public int roundedY() {
		return (int)this.y;
	}
	
	public static Vector2 add(Vector2 a, Vector2 b) {
		return new Vector2(a.x + b.x, a.y + b.y);
	}
	public static Vector2 subtract(Vector2 a, Vector2 b) {
		return add(a, negate(b));
	}
	public static Vector2 negate(Vector2 a) {
		return scale(-1f, a);
	}
	public static Vector2 scale(Vector2 a, double b) {
		return new Vector2(a.x * b, a.y * b);
	}
	public static Vector2 scale(double a, Vector2 b) {
		return scale(b, a);
	}
	public static Vector2 scaleDown(Vector2 a, double b) {
		return new Vector2(a.x / b, a.y / b);
	}
	
	public static Vector2 normalize(Vector2 a) {
		return scaleDown(a, a.magnitude());
	}
	
	public static Vector2 interp(Vector2 v0, Vector2 v1, double t) {
		return add(v0, scale(subtract(v1, v0), t));
	}
	
	public static double dot(Vector2 a, Vector2 b) {
		return a.x * b.x + a.y * b.y;
	}
	
	public static double cross(Vector2 a, Vector2 b) {
		return a.x * b.y - a.y * b.x;
	}
	
	public static double distance(Vector2 a, Vector2 b) {
		return subtract(a, b).magnitude();
	}

	public static Vector2 project(Vector2 a, Vector2 b) {
		return scale(b, dot(a, b) / dot(b, b));
	}

	public static Vector2 reject(Vector2 a, Vector2 b) {
		// System.out.println("reject(" + a + ", " + b + "): " + subtract(a, project(a, b)));
		return subtract(a, project(a, b));
	}
	
	public static double angle(Vector2 a, Vector2 b) {
		return (double)Math.acos(Vector2.dot(a, b) / (a.magnitude() * b.magnitude()));
	}
	
	public static void drawLine(Graphics g, Vector2 a, Vector2 b) {
		g.drawLine(a.roundedX(), a.roundedY(), b.roundedX(), b.roundedY());
	}
	
	public static Vector2 intersect(Vector2 p, Vector2 pPlusR, Vector2 q, Vector2 qPlusS) {
		Vector2
			r = Vector2.subtract(pPlusR, p),
			s = Vector2.subtract(qPlusS, q),
			qMinusP = Vector2.subtract(q, p);
		double rCrossS = Vector2.cross(r, s);
		if (areEqual(rCrossS, 0f)) {
			double qMinusPCrossR = Vector2.cross(qMinusP, r);
			if (areEqual(qMinusPCrossR, 0f)) {
				Vector2 rOverRDotR = Vector2.scaleDown(r, Vector2.dot(r, r));
				double
					t0 = Vector2.dot(qMinusP, rOverRDotR),
					t1 = t0 + Vector2.dot(s, rOverRDotR);
				int
					cmpBounds0 = compareBounds(t0, 0f, 1f),
					cmpBounds1 = compareBounds(t1, 0f, 1f);
				if (cmpBounds0 * cmpBounds1 > 0) {
					return null;
				} else {
					if (cmpBounds0 < 0) { // cmpBounds1 >= 0
						return p;
					} else if (cmpBounds0 == 0) {
						return Vector2.add(p, Vector2.scale(t0, r));
					} else {
						return pPlusR;
					}
				}
			} else {
				return null;
			}
		} else {
			double
				t = Vector2.cross(qMinusP, s) / rCrossS,
				u = Vector2.cross(qMinusP, r) / rCrossS;
			if (inBounds(t, 0f, 1f) && inBounds(u, 0f, 1f)) {
				return Vector2.add(p, Vector2.scale(t, r));
			} else {
				return null;
			}
		}
	}
	
	public static Vector2[] vertices(Rectangle2D.Double rect) {
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(rect.x, rect.y);
		vertices[1] = new Vector2(rect.x + rect.width, rect.y);
		vertices[2] = new Vector2(rect.x + rect.width, rect.y + rect.height);
		vertices[3] = new Vector2(rect.x, rect.y + rect.height);
		return vertices;
	}
	
	public static Vector2 intersect(Rectangle2D.Double rect, Vector2 v0, Vector2 v1) {
		Vector2
			vertices[] = vertices(rect),
			r0 = vertices[0],
			r1 = vertices[1],
			r2 = vertices[2],
			r3 = vertices[3],
			intersection = Vector2.intersect(v0, v1, r0, r1);
		if (intersection != null) {
			return intersection;
		} else {
			intersection = Vector2.intersect(v0, v1, r1, r2);
			if (intersection != null) {
				return intersection;
			} else {
				intersection = Vector2.intersect(v0, v1, r2, r3);
				if (intersection != null) {
					return intersection;
				} else {
					return Vector2.intersect(v0, v1, r3, r0);
				}
			}
		}
	}
	
	public static void draw(Graphics2D g, Vector2 a) {
		draw(g, a, 1f);
	}
	public static void draw(Graphics2D g, Vector2 a, double radius) {
		double diameter = radius * 2f;
		g.draw(new Ellipse2D.Double(a.x - radius, a.y - radius, diameter, diameter));
	}
	
	public static void fill(Graphics2D g, Vector2 a) {
		fill(g, a, 1f);
	}
	public static void fill(Graphics2D g, Vector2 a, double radius) {
		double diameter = radius * 2f;
		g.fill(new Ellipse2D.Double(a.x - radius, a.y - radius, diameter, diameter));
	}
}