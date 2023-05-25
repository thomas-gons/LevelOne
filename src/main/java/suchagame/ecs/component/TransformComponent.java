package suchagame.ecs.component;


import javafx.geometry.BoundingBox;
import suchagame.ecs.entity.Mob;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

/**
 * Component for managing position-related data and actions.
 */
public class TransformComponent extends Component {
    private Vector2f position;
    private final Vector2f spawnOrigin;

    // virtual position is used for rendering
    private final Vector2f virtualPosition = new Vector2f(0f, 0f);

    /**
     * Creates a new TransformComponent based on a given position.
     * @param position initial position.
     */
    public TransformComponent(Vector2f position) {
        this.spawnOrigin = position;
        // deep copy
        this.position = new Vector2f(position.getX(), position.getY());
    }

    /**
     * Creates a new TransformComponent based on a given position.
     * @param x initial x position.
     * @param y initial y position.
     */
    public TransformComponent(float x, float y) {
        this.position = new Vector2f(x, y);
        this.spawnOrigin = new Vector2f(x, y);
    }

    /**
     * Creates a new TransformComponent based on a given position.
     * @param eventualSpawnAreas possible spawn areas.
     */
    public TransformComponent(String[] eventualSpawnAreas) {
        BoundingBox area = Mob.getRandomSpawnArea(eventualSpawnAreas);
        this.spawnOrigin = new Vector2f((float) area.getCenterX(), (float) area.getCenterY());
        LayersComponent layersComponent = Game.em.getMap().getComponent(LayersComponent.class);

        // rejection sampling
        Vector2f testPosition = new Vector2f(0f, 0f);
        do {
            testPosition.setX((float) (Math.random() * area.getWidth() + area.getMinX()));
            testPosition.setY((float) (Math.random() * area.getHeight() + area.getMinY()));
        } while (layersComponent.getTile(1, testPosition) != 0);

        this.position = testPosition;
    }

    public Vector2f getPosition() {
        return this.position;
    }

    /**
     * Returns a mutable deep copy of the position.
     * @return deep copy of the position.
     */
    public Vector2f getPositionDeepCopy() {
        return new Vector2f(this.position.getX(), this.position.getY());
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getVirtualPosition() {
        return this.virtualPosition;
    }

    public Vector2f getSpawnOrigin() {
        return this.spawnOrigin;
    }

}