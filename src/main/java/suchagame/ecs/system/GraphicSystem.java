package suchagame.ecs.system;

import javafx.scene.canvas.GraphicsContext;
import suchagame.ecs.EntityManager;
import suchagame.ecs.component.GraphicComponent;
import suchagame.ecs.component.InputComponent;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.MapEntity;
import suchagame.ecs.entity.Player;
import suchagame.ui.Camera;
import suchagame.ui.Game;
import suchagame.utils.Vector2;

import java.util.concurrent.atomic.AtomicReferenceArray;

public class GraphicSystem extends System {
    public static void render(GraphicsContext gc) {
        AnimationSystem.update();
        for (Entity entity : EntityManager.instance.getAllWithComponent(GraphicComponent.class)) {
            GraphicComponent graphicComponent = entity.getComponent(GraphicComponent.class);
            Vector2<Float> position = entity.getComponent(TransformComponent.class).getPosition();
            Vector2<Float> correctedPosition = new Vector2<>(position.getX(), position.getY());
            if (entity.hasComponent(InputComponent.class)) {

                 correctedPosition.setY((float) ((position.getY() < Game.freeSpace.getMinY()) ?
                         Game.freeSpace.getMinY() - ((Game.freeSpace.getMinY() -1.25 * position.getY() - graphicComponent.getHeight()) * Camera.scale): (position.getY() > Game.freeSpace.getMaxY()) ?
                            (position.getY() - Game.freeSpace.getMaxY() - 2 * graphicComponent.getHeight()) * Camera.scale + Game.freeSpace.getMaxY():
                            (Game.height - graphicComponent.getHeight() / Camera.scale) / 2));

                 correctedPosition.setX((float) ((position.getX() < Game.freeSpace.getMinX()) ?
                         Game.freeSpace.getMinX() - ((Game.freeSpace.getMinX() - position.getX() - graphicComponent.getWidth() * 3.16) * Camera.scale): (position.getX() > Game.freeSpace.getMaxX()) ?
                            (position.getX() - Game.freeSpace.getMaxX() - 4.13 * graphicComponent.getWidth()) * Camera.scale + Game.freeSpace.getMaxX():
                            (Game.width - graphicComponent.getWidth() * Camera.scale) / 2));

                 entity.getComponent(TransformComponent.class).setVirtualPosition(correctedPosition);

            } else if (entity.hasComponent(TransformComponent.class)) {
                entity.getComponent(TransformComponent.class).setVirtualPosition(
                        new Vector2<>(((correctedPosition.getX() - Game.width) * Camera.scale), (correctedPosition.getY() * Camera.scale)));
            }
            gc.drawImage(
                    graphicComponent.getSprite(),
                    graphicComponent.getX(),
                    graphicComponent.getY(),
                    graphicComponent.getWidth(),
                    graphicComponent.getHeight(),
                    correctedPosition.getX(),
                    correctedPosition.getY(),
                    graphicComponent.getWidth() * Camera.scale,
                    graphicComponent.getHeight() * Camera.scale
            );
        }
    }
}
