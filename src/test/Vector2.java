package test;

public class Vector2 {
	public final double
		x,
		y;
	
	public Vector2() {
		this(0d, 0d);
	}
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "<" + this.x + ", " + this.y + ">";
	}
	
	public Vector2 add(Vector2 other) {
		return new Vector2(this.x + other.x, this.y + other.y);
	}
	
	public Vector2 subtract(Vector2 other) {
		return new Vector2(this.x - other.x, this.y - other.y);
	}
	
	public Vector2 multiply(double scalar) {
		return new Vector2(this.x * scalar, this.y * scalar);
	}
	
	public Vector2 divide(double scalar) {
		return new Vector2(this.x / scalar, this.y / scalar);
	}
	
	public Vector2 modulus(double modulus) {
		return new Vector2(this.x % modulus, this.y % modulus);
	}
	
	public double dotProduct(Vector2 other) {
		return this.x * other.x + this.y * other.y;
	}
	
	public double crossProductZComponent(Vector2 other) {
		// (u x v) == | i   j   k |.z == u.x * v.y - u.y * v.x
		//			  | u.x u.y 0 |
		//			  | v.x v.y 0 |
		return this.x * other.y - this.y * other.x;
	}
	
	public double magnitudeSquared() {
		return this.dotProduct(this);
	}
	
	public double magnitude() {
		return Math.sqrt(this.magnitudeSquared());
	}
	
	public Vector2 negate() {
		return new Vector2(-this.x, -this.y);
	}
	
	public Vector2 normal() {
		return this.divide(this.magnitude());
	}
}
