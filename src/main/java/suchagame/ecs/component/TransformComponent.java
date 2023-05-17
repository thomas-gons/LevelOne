package suchagame.ecs.component;


import com.fasterxml.jackson.annotation.JsonCreator;
import javafx.geometry.BoundingBox;

import suchagame.ecs.entity.Mob;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;


public class TransformComponent extends Component {
    private Vector2f position;
    private Vector2f virtualPosition = new Vector2f(0f, 0f);

    private final Vector2f spawnOrigin;
    @JsonCreator
    public TransformComponent(int offsetX, int offsetY) {
        Vector2f offset = new Vector2f(offsetX, offsetY);
        this.spawnOrigin = Game.em.getPlayer().getComponent(TransformComponent.class)
                .getPositionDeepCopy().add(offset);

        this.position = this.spawnOrigin;
    }
    @JsonCreator
    public TransformComponent(float x, float y) {
        this.spawnOrigin = new Vector2f(x, y);
        this.position = this.spawnOrigin;
    }
    @JsonCreator
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

    public Vector2f getPositionDeepCopy() {
        return new Vector2f(this.position.getX(), this.position.getY());
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getVirtualPosition() {
        return this.virtualPosition;
    }

}