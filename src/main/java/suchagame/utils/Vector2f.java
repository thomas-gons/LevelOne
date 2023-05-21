package suchagame.utils;


import java.util.Objects;

/**
 * A simple 2D vector class.
 */
public class Vector2f {
    private float x, y;

    /**
     * Creates a new vector.
     * @param x abscissa
     * @param y ordinate
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new vector with the same abscissa and ordinate.
     * @param c The value to set abscissa and ordinate to.
     */
    public Vector2f(float c) {
        this.x = c;
        this.y = c;
    }

    /**
     * translates the vector by the given vector.
     * @param other The vector to translate by.
     */
    public void translate(Vector2f other) {
        this.x += other.x;
        this.y += other.y;
    }

    /**
     * Sets the abscissa and ordinate of the vector.
     * @param x abscissa
     * @param y ordinate
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
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


    /**
     * Returns a new vector with the sum of this vector and the other vector.
     * @param other the vector to sum with
     * @return sum of this vector and the other vector
     */
    public Vector2f add(Vector2f other) {
        return new Vector2f(this.x + other.x, this.y + other.y);
    }

    /**
     * Returns a new vector with the difference of this vector and the other vector.
     * @param other the vector to subtract with
     * @return difference of this vector and the other vector
     */
    public Vector2f sub(Vector2f other) {
        return new Vector2f(this.x - other.x, this.y - other.y);
    }

    /**
     * Returns a new vector with the product of this vector and the scalar.
     * @param scalar the scalar to multiply with
     * @return product of this vector and the scalar
     */
    public Vector2f mul(float scalar) {
        return new Vector2f(this.x * scalar, this.y * scalar);
    }

    /**
     * Returns a new vector with the product of this vector and the other vector.
     * @param other the vector to multiply with
     * @return product of this vector and the other vector
     */
    public Vector2f mul(Vector2f other) {
        return new Vector2f(this.x * other.x, this.y * other.y);
    }

    /**
     * Returns a new vector with the quotient of this vector and the scalar.
     * @param scalar the scalar to divide with
     * @return quotient of this vector and the scalar
     */
    public Vector2f div(float scalar) {
        return new Vector2f(this.x / scalar, this.y / scalar);
    }

    /**
     * Returns the normalized vector of this vector.
     * @return normalized vector of this vector
     */
    public Vector2f normalize() {
        float length = (float) Math.sqrt(this.x * this.x + this.y * this.y);
        return new Vector2f(this.x / length, this.y / length);
    }

    /**
     * Returns the dot product of this vector and the other vector.
     * @param other the vector to dot with
     * @return dot product of this vector and the other vector
     */
    public float dot(Vector2f other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Returns the distance between this vector and the other vector.
     * @param other the vector to calculate distance with
     * @return Euclidean distance between this vector and the other vector
     */
    public float distance(Vector2f other) {
        return (float) Math.sqrt((this.x - other.x) * (this.x - other.x) +
                                 (this.y - other.y) * (this.y - other.y));
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

}
