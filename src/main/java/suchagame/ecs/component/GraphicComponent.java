package suchagame.ecs.component;


import javafx.scene.image.Image;
import suchagame.ui.Game;
import suchagame.utils.Utils;
import suchagame.utils.Vector2;

public class GraphicComponent extends Component {
    private final Image sprite;
    private int width, height;
    private Vector2<Integer> position;

    public GraphicComponent(String spritePath) {
        this.sprite = new Image(Utils.getPathResource(Game.class, spritePath));
        this.width = (int) sprite.getWidth();
        this.height = (int) sprite.getHeight();
        this.position = new Vector2<>(0, 0);
    }

    public Image getSprite() {
        return sprite;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2<Integer> getPosition() {
        return position;
    }

    public void setPosition(Vector2<Integer> position) {
        this.position = position;
    }
}
