package suchagame.utils;


import java.util.Objects;

public class Vector2f {
    private float x, y;
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(float c) {
        this.x = c;
        this.y = c;
    }
    public void translate(float dc) {
        this.x += dc;
        this.y += dc;
    }
    public void translate(Vector2f other) {
        this.x += other.x;
        this.y += other.y;
    }
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final Vector2f other = (Vector2f) obj;
        return (Objects.equals(this.x, other.x) && Objects.equals(this.y, other.y));
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }


    public Vector2f add(Vector2f other) {
        return new Vector2f(this.x + other.x, this.y + other.y);
    }

    public Vector2f sub(Vector2f other) {
        return new Vector2f(this.x - other.x, this.y - other.y);
    }

    public Vector2f mul(float scalar) {
        return new Vector2f(this.x * scalar, this.y * scalar);
    }

    public Vector2f div(float scalar) {
        return new Vector2f(this.x / scalar, this.y / scalar);
    }

    public Vector2f normalize() {
        float length = (float) Math.sqrt(this.x * this.x + this.y * this.y);
        return new Vector2f(this.x / length, this.y / length);
    }

    public float dot(Vector2f other) {
        return this.x * other.x + this.y * other.y;
    }

    public float distance(Vector2f other) {
        return (float) Math.sqrt((this.x - other.x) * (this.x - other.x) +
                                 (this.y - other.y) * (this.y - other.y));
    }

}
