package suchagame.ecs.component;

import javafx.geometry.BoundingBox;
import suchagame.utils.Vector2;

public class PhysicComponent extends Component {

    private BoundingBox boundingBox;
    private final float mass;
    private final float rigidness;
    private final float friction;
    private final float elasticity;
    private final float coefficientOfRestitution;

    public PhysicComponent(
            int width, int height,
            int offsetX, int offsetY,
            float mass, float rigidness,
            float friction, float elasticity,
            float coefficientOfRestitution) {

        boundingBox = new BoundingBox(offsetX, offsetY, width, height);
        this.mass = mass;
        this.rigidness = rigidness;
        this.friction = friction;
        this.elasticity = elasticity;
        this.coefficientOfRestitution = coefficientOfRestitution;
    }


    public BoundingBox getTranslatedBoundingBox(Vector2<Float> position) {
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

    public float getMass() {
        return mass;
    }

    public float getRigidness() {
        return rigidness;
    }

    public float getFriction() {
        return friction;
    }

    public float getElasticity() {
        return elasticity;
    }

    public float getCoefficientOfRestitution() {
        return coefficientOfRestitution;
    }
}