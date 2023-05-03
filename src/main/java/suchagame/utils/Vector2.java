package suchagame.utils;


import java.util.Objects;

public class Vector2<T extends Number> {
    private T x, y;
    public Vector2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public float fastDistance(Vector2<T> other) {
        float dx = Math.abs(this.x.floatValue() - other.x.floatValue());
        float dy = Math.abs(this.y.floatValue() - other.y.floatValue());
        return (dx + dy) / 1.4142f;
    }

    public void set(T x, T y) {
        this.x = x;
        this.y = y;
    }
    public void setX(T x) {
        this.x = x;
    }
    public void setY(T y) {
        this.y = y;
    }
    public T getX() {
        return this.x;
    }

    public T getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final Vector2<T> other = (Vector2<T>) obj;
        return (Objects.equals(this.x, other.x) && Objects.equals(this.y, other.y));
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

}
