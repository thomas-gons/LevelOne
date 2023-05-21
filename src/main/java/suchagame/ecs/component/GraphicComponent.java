package suchagame.ecs.component;


import javafx.scene.image.Image;
import suchagame.ui.Game;
import suchagame.utils.Utils;

/**
 * Component for base graphic (can be extended for animation @see AnimationComponent).
 */
public class GraphicComponent extends Component {
    private final Image sprite;
    private int width, height;
    private int[] origin;

    /**
     * Constructs a GraphicComponent object.
     * @param spriteFileName name of the sprite file (must be in src/main/java/suchagame/images).
     */
    public GraphicComponent(String spriteFileName) {
        this.sprite = new Image(Utils.getPathResource(Game.class, "images/" + spriteFileName));
        this.width = (int) sprite.getWidth();
        this.height = (int) sprite.getHeight();
        this.origin = new int[]{0, 0};
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

    public int[] getOrigin() {
        return origin;
    }

    public void setOrigin(int[] new_origin) {
        this.origin = new_origin;
    }
}
