package suchagame.ecs.component;


import javafx.scene.image.Image;
import suchagame.ui.Game;
import suchagame.utils.Utils;

public class GraphicComponent extends Component {
    private final Image sprite;
    private int width, height;
    private int x, y;

    public GraphicComponent(String spritePath) {
        this.sprite = new Image(Utils.getPathResource(Game.class, spritePath));
        this.width = (int) sprite.getWidth();
        this.height = (int) sprite.getHeight();
        this.x = 0;
        this.y = 0;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
