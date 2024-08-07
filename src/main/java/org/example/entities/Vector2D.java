package org.example.entities;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D limit(double max) {

        if (getMagnitude() > max) {
            return this.normalize().scale(max);
        }

        return this;
    }

    public Vector2D add(Vector2D vector) {
        return new Vector2D(x + vector.getX(), y + vector.getY());
    }

    public Vector2D subtract(Vector2D v)
    {
        return new Vector2D(x - v.getX(), y - v.getY());
    }

    public Vector2D normalize() {

        double magnitude = getMagnitude();

        return new Vector2D(x / magnitude, y / magnitude);
    }

    public double getAngle() {
        return Math.asin(y/getMagnitude());
    }

    public Vector2D scale(double scale) {
        return new Vector2D(x * scale, y * scale);
    }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D setDirection(double angle) {
        double magnitude = getMagnitude();

        return new Vector2D(Math.cos(angle) * magnitude, Math.sin(angle) * magnitude);
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
}
