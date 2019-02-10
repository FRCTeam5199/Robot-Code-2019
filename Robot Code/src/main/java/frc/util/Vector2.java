/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc.util;

/**
 *
 * @author 18wakayamats
 */
public class Vector2 implements Comparable<Vector2> {

	public static final Vector2 ZERO = new Vector2(0, 0);
	public static final Vector2 FORWARDS = new Vector2(0, 1);
	public static final Vector2 BACKWARDS = new Vector2(0, -1);
	public static final Vector2 LEFT = new Vector2(-1, 0);
	public static final Vector2 RIGHT = new Vector2(1, 0);

	private static final double degToRad = Math.PI / 180;

	private double x;
	private double y;

	public Vector2() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 other) {
		this.x = other.x;
		this.y = other.y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double length() {
		return Math.sqrt((x * x) + (y * y));
	}

	// Deprecated now that we have the copy constructor?
	public Vector2 clone() {
		return new Vector2(x, y);
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	@Override
	public int compareTo(Vector2 other) {
		if (this.x == other.x) {
			return (int) (this.y - other.y);
		} else {
			return (int) (this.x - other.x);
		}
	}

	public static double getSlope(Vector2 v1, Vector2 v2) {
		return (v1.getY() - v2.getY()) / (v1.getX() - v2.getX());
	}

	public static Vector2 add(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2 subtract(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}

	public static Vector2 multiply(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x * v2.x, v1.y * v2.y);
	}

	public static Vector2 multiply(Vector2 v1, double n) {
		return new Vector2(v1.x * n, v1.y * n);
	}

	public static Vector2 divide(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x / v2.x, v1.y / v2.y);
	}

	public static Vector2 divide(Vector2 v, double n) {
		return new Vector2(v.x / n, v.y / n);
	}

	public static Vector2 rotate(Vector2 v, double d) {
		double sin = Math.sin(d);
		double cos = Math.cos(d);
		return new Vector2(v.getX() * cos - v.getY() * sin, v.getX() * sin + v.getY() * cos);
	}

	public static Vector2 rotateCW(Vector2 v, double d) {
		return rotate(v, -d);
	}

	public static Vector2 rotateDeg(Vector2 v, double d) {
		return rotate(v, d * degToRad);
	}

	public static Vector2 rotateDegCW(Vector2 v, double d) {
		return rotateDeg(v, -d);
	}

	public static double distance(Vector2 v1, Vector2 v2) {
		return Vector2.subtract(v1, v2).length();
	}

}
