package suchagame.ecs.component;


import javafx.geometry.Rectangle2D;
import suchagame.ecs.entity.MapEntity;
import suchagame.utils.Vector2;


public class TransformComponent extends Component {
    private Vector2<Float> position;
    private Vector2<Float> virtualPosition = new Vector2<>(0f, 0f);

    public TransformComponent(float x, float y) {
        this.position = new Vector2<>(x, y);
    }

    public TransformComponent(Rectangle2D area) {
        LayersComponent layersComponent = MapEntity.map.getComponent(LayersComponent.class);

        // rejection sampling
        Vector2<Float> testPosition = new Vector2<>(0f, 0f);
        do {
            testPosition.setX((float) (Math.random() * area.getWidth() + area.getMinX()));
            testPosition.setY((float) (Math.random() * area.getHeight() + area.getMinY()));
        } while (layersComponent.getTile(1, testPosition) != 0);

        this.position = testPosition;
    }

    public Vector2<Float> getPosition() {
        return new Vector2<>(this.position.getX(), this.position.getY());
    }

    public void setPosition(Vector2<Float> position) {
        this.position = position;
    }

    public Vector2<Float> getVirtualPosition() {
        return this.virtualPosition;
    }

    public void setVirtualPosition(Vector2<Float> virtualPosition) {
        this.virtualPosition = virtualPosition;
    }
}