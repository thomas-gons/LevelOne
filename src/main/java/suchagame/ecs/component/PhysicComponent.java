package suchagame.ecs.component;

import javafx.geometry.BoundingBox;
import suchagame.utils.Vector2f;

public class PhysicComponent extends Component {

    private BoundingBox boundingBox;
    private final float mass;

    private Vector2f velocity = new Vector2f(0f);

    public PhysicComponent(
            int width, int height,
            int offsetX, int offsetY,
            float mass) {

        boundingBox = new BoundingBox(offsetX, offsetY, width, height);
        this.mass = mass;
    }

    public PhysicComponent(BoundingBox boundingBox, float mass) {
        this.boundingBox = boundingBox;
        this.mass = mass;
    }


    public BoundingBox getTranslatedBoundingBox(Vector2f position) {
        return new BoundingBox(
                boundingBox.getMinX() + position.getX(),
                boundingBox.getMinY() + position.getY(),
                boundingBox.getWidth(),
                boundingBox.getHeight()
        );
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public static Vector2f getIntersectionDepth(BoundingBox a, BoundingBox b) {
        float minOverlapX = (float) (a.getMaxX() - b.getMinX());
        float minOverlapY = (float) (a.getMaxY() - b.getMinY());
        float maxOverlapX = (float) (b.getMaxX() - a.getMinX());
        float maxOverlapY = (float) (b.getMaxY() - a.getMinY());

        float overlapX = 0;
        float overlapY = 0;

        if (minOverlapX > 0 && minOverlapY > 0) {
            if (minOverlapX < minOverlapY) {
                overlapX = minOverlapX;
            } else {
                overlapY = minOverlapY;
            }
        } else if (maxOverlapX > 0 && maxOverlapY > 0) {
            if (maxOverlapX < maxOverlapY) {
                overlapX = -maxOverlapX;
            } else {
                overlapY = -maxOverlapY;
            }
        }

        return new Vector2f(overlapX, overlapY);
    }

    public float getMass() {
        return mass;
    }

    public Vector2f getVelocity() {
        return this.velocity;
    }

    public Vector2f getVelocityDeepCopy() {
        return new Vector2f(this.velocity.getX(), this.velocity.getY());
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

}