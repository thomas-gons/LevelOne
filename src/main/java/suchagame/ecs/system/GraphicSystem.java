package suchagame.ecs.system;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import suchagame.ecs.EntityManager;
import suchagame.ecs.component.GraphicComponent;
import suchagame.ecs.component.InputComponent;
import suchagame.ecs.component.PhysicComponent;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ui.Camera;
import suchagame.ui.Game;
import suchagame.utils.Vector2;

import static java.lang.Float.NaN;


public class GraphicSystem extends System {
    public static void render(GraphicsContext gc) {
        AnimationSystem.update();
        for (Entity entity : EntityManager.instance.getAllWithComponent(GraphicComponent.class)) {
            GraphicComponent graphicComponent = entity.getComponent(GraphicComponent.class);
            updateVirtualPosition(entity, graphicComponent);
            Vector2<Float> virtualPosition = entity.getComponent(TransformComponent.class).getVirtualPosition();
            if (virtualPosition.getX() == NaN || virtualPosition.getY() == NaN)
                continue;
            gc.drawImage(
                    graphicComponent.getSprite(),
                    graphicComponent.getOrigin().getX(),
                    graphicComponent.getOrigin().getY(),
                    graphicComponent.getWidth(),
                    graphicComponent.getHeight(),
                    virtualPosition.getX(),
                    virtualPosition.getY(),
                    graphicComponent.getWidth() * Camera.scale,
                    graphicComponent.getHeight() * Camera.scale
            );
        }
    }

    public static void updateVirtualPosition(Entity entity, GraphicComponent graphicComponent) {

         Vector2<Float> position = entity.getComponent(TransformComponent.class).getPosition();
         Vector2<Float> virtualPosition = entity.getComponent(TransformComponent.class).getVirtualPosition();
         BoundingBox viewport = Camera.viewport;

         if (entity.hasComponent(InputComponent.class)) {

             virtualPosition.setX((position.getX() > Game.freeSpace.getMinX() && position.getX() < Game.freeSpace.getMaxX()) ?
                     (Game.width - graphicComponent.getWidth() * Camera.scale) / 2:
                     (float) (position.getX() - viewport.getMinX() - (float) graphicComponent.getWidth() / 2) * Camera.scale);

             virtualPosition.setY((position.getY() > Game.freeSpace.getMinY() && position.getY() < Game.freeSpace.getMaxY()) ?
                     (Game.height - graphicComponent.getHeight() * Camera.scale) / 2:
                     (float) (position.getY() - viewport.getMinY() - (float) graphicComponent.getHeight() / 2) * Camera.scale);


         } else if (entity.hasComponent(TransformComponent.class)) {
             BoundingBox boundingBox = entity.getComponent(PhysicComponent.class).getBoundingBox();
             if (viewport.intersects(new BoundingBox(
                        position.getX(),
                        position.getY(),
                        boundingBox.getWidth(),
                        boundingBox.getHeight()
             ))) {
                virtualPosition.setX(
                        (float) (position.getX() - viewport.getMinX() - graphicComponent.getWidth() / 2f) * Camera.scale
                );
                virtualPosition.setY(
                        (float) ((position.getY() - viewport.getMinY() - graphicComponent.getHeight() * 0.9f) * Camera.scale)
                );
             } else {
                virtualPosition.setX(NaN);
                virtualPosition.setY(NaN);
             }
         }
    }

    public static void render_bounding_boxes(GraphicsContext gc) {
        for (Entity entity : EntityManager.instance.getAllWithComponent(GraphicComponent.class)) {
            Vector2<Float> virtualPosition = entity.getComponent(TransformComponent.class).getVirtualPosition();
            BoundingBox boundingBox = entity.getComponent(PhysicComponent.class).getBoundingBox();
            if (virtualPosition.getX() == NaN || virtualPosition.getY() == NaN)
                continue;
            gc.setStroke(javafx.scene.paint.Color.RED);
            gc.setLineWidth(2);
        }
    }
}
