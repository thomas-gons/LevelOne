package suchagame.ecs.component;


import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import suchagame.ecs.entity.MapEntity;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;


public class TransformComponent extends Component {
    private Vector2f position;
    private Vector2f virtualPosition = new Vector2f(0f, 0f);

    public TransformComponent(float x, float y) {
        this.position = new Vector2f(x, y);
    }

    public TransformComponent(BoundingBox area) {
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