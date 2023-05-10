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

    public void setBoundingBox(BoundingBox newBoundingBox) {
        this.boundingBox = newBoundingBox;
    }
    public float getMass() {
        return mass;
    }

    public Vector2f getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

}