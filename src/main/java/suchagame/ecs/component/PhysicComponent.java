package suchagame.ecs.component;

import javafx.geometry.BoundingBox;
import suchagame.utils.Vector2f;

/**
 * Component for managing physics-related data and actions.
 */
public class PhysicComponent extends Component {

    private final BoundingBox hitBox;
    private final float mass;

    private Vector2f velocity = new Vector2f(0f);

    /**
     * Constructs a PhysicComponent object with helper values for the hit box and mass.
     * @param width the width of the hit box
     * @param height the height of the hit box
     * @param offsetX the x offset of the hit box
     * @param offsetY the y offset of the hit box
     * @param mass the mass of the entity
     */
    public PhysicComponent(
            int width, int height,
            int offsetX, int offsetY,
            float mass) {

        hitBox = new BoundingBox(offsetX, offsetY, width, height);
        this.mass = mass;
    }

    /**
     * Constructs a PhysicComponent object with the specified hit box and mass.
     * @param hitBox the hit box of the entity
     * @param mass the mass of the entity
     */
    public PhysicComponent(BoundingBox hitBox, float mass) {
        this.hitBox = hitBox;
        this.mass = mass;
    }

    public BoundingBox getHitBox() {
        return hitBox;
    }

    /**
     * Calculates the intersection depth between two bounding boxes.
     *
     * @param a the first bounding box
     * @param b the second bounding box
     * @return a vector representing the intersection depth, where the x-component is the overlap in the x-axis
     *         and the y-component is the overlap in the y-axis
     */
    @SuppressWarnings("unused")
    public static Vector2f getIntersectionDepth(BoundingBox a, BoundingBox b) {
        // Calculate the overlap in the x-axis
        float minOverlapX = (float) (a.getMaxX() - b.getMinX());
        float maxOverlapX = (float) (b.getMaxX() - a.getMinX());

        // Calculate the overlap in the y-axis
        float minOverlapY = (float) (a.getMaxY() - b.getMinY());
        float maxOverlapY = (float) (b.getMaxY() - a.getMinY());

        float overlapX = 0;
        float overlapY = 0;

        // Determine the smallest positive overlap
        if (minOverlapX > 0 && minOverlapY > 0) {
            if (minOverlapX < minOverlapY) {
                overlapX = minOverlapX;
            } else {
                overlapY = minOverlapY;
            }
        }
        // Determine the largest negative overlap
        else if (maxOverlapX > 0 && maxOverlapY > 0) {
            if (maxOverlapX < maxOverlapY) {
                overlapX = -maxOverlapX;
            } else {
                overlapY = -maxOverlapY;
            }
        }

        // Create and return a vector representing the intersection depth
        return new Vector2f(overlapX, overlapY);
}


    public float getMass() {
        return mass;
    }

    public Vector2f getVelocity() {
        return this.velocity;
    }

    /**
     * Returns a deep copy of the velocity vector.
     * @return a deep copy of the velocity vector
     */
    public Vector2f getVelocityDeepCopy() {
        return new Vector2f(this.velocity.getX(), this.velocity.getY());
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

}