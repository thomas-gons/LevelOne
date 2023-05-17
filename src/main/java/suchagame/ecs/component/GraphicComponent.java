package suchagame.ecs.component;


import com.fasterxml.jackson.annotation.JsonCreator;
import javafx.scene.image.Image;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Projectile;
import suchagame.ui.Game;
import suchagame.utils.Utils;
import suchagame.utils.Vector2f;

public class GraphicComponent extends Component {
    private final Image sprite;
    private int width, height;
    private int[] origin;
    @JsonCreator
    public GraphicComponent(String spritePath) {
        Entity entity = Game.em.getLastEntity();
        if (entity instanceof Projectile && ((Projectile) entity).getOrientation() != Projectile.SIDE.NO) {
            String spritePath_ = spritePath.substring(0, spritePath.length() - 4);
            this.sprite = new Image(Utils.getPathResource(
                    Game.class, String.format(
                            "images/%s_%s.png", spritePath_, ((Projectile) entity).getOrientation().toString().toLowerCase()
                    )
            ));
        } else {
            this.sprite = new Image(Utils.getPathResource(Game.class, "images/" + spritePath));
        }

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
