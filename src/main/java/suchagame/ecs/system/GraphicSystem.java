package suchagame.ecs.system;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import suchagame.ecs.component.GraphicComponent;
import suchagame.ecs.component.InputComponent;
import suchagame.ecs.component.PhysicComponent;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ui.Camera;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

import static java.lang.Float.NaN;


/**
 * System that handles the rendering of the entities
 */
public class GraphicSystem extends System {

    public static boolean renderHitBoxes = false;

    /**
     * Renders the visible entities
     *
     * @param gc the graphics context
     */
    public static void render(GraphicsContext gc) {
        for (Entity entity : Game.em.getAllWithComponent(GraphicComponent.class)) {
            GraphicComponent graphicComponent = entity.getComponent(GraphicComponent.class);
            // Update the position to render
            Vector2f virtualPosition = updateVirtualPosition(entity, graphicComponent);

            // Skip if the entity is not visible
            if (Float.isNaN(virtualPosition.getX()) || Float.isNaN(virtualPosition.getY()))
                continue;

            // Render the entity
            gc.drawImage(
                    graphicComponent.getSprite(),
                    graphicComponent.getOrigin()[0],
                    graphicComponent.getOrigin()[1],
                    graphicComponent.getWidth(),
                    graphicComponent.getHeight(),
                    virtualPosition.getX(),
                    virtualPosition.getY(),
                    graphicComponent.getWidth() * Camera.scale,
                    graphicComponent.getHeight() * Camera.scale
            );
            if (renderHitBoxes)
                renderHitBoxes();
        }
    }

    /**
     * Updates the virtual position of the entity
     *
     * @param entity           the entity
     * @param graphicComponent the graphic component of the entity
     * @return the virtual position of the entity
     */
    public static Vector2f updateVirtualPosition(Entity entity, GraphicComponent graphicComponent) {

        Vector2f position = entity.getComponent(TransformComponent.class).getPosition();
        Vector2f virtualPosition = entity.getComponent(TransformComponent.class).getVirtualPosition();
        BoundingBox viewport = Camera.viewport;

        // player entity
        if (entity.hasComponent(InputComponent.class)) {
            /*
                If the entity is in the free space, we center it
                Otherwise, we render it at its position
             */
            virtualPosition.setX((position.getX() > Game.freeSpace.getMinX() && position.getX() < Game.freeSpace.getMaxX()) ?
                    (Game.width - graphicComponent.getWidth() * Camera.scale) / 2 :
                    (float) (position.getX() - viewport.getMinX() - (float) graphicComponent.getWidth() / 2) * Camera.scale);

            virtualPosition.setY((position.getY() > Game.freeSpace.getMinY() && position.getY() < Game.freeSpace.getMaxY()) ?
                    (Game.height - graphicComponent.getHeight() * Camera.scale) / 2 :
                    (float) (position.getY() - viewport.getMinY() - (float) graphicComponent.getHeight() / 2) * Camera.scale);


        }
        // other entities that have transform and physic components
        else if (entity.hasComponent(TransformComponent.class)) {
            BoundingBox boundingBox = entity.getComponent(PhysicComponent.class).getHitBox();
            // If the entity is in the viewport, we render it
            if (viewport.intersects(new BoundingBox(
                    position.getX(),
                    position.getY(),
                    boundingBox.getWidth(),
                    boundingBox.getHeight()
            ))) {
                // Update the virtual position of the entity
                virtualPosition.setX(
                        (float) (position.getX() - viewport.getMinX() - graphicComponent.getWidth() / 2f) * Camera.scale
                );
                virtualPosition.setY(
                        (float) ((position.getY() - viewport.getMinY() - graphicComponent.getHeight() * 0.9f) * Camera.scale)
                );
            } else {
                // Otherwise, we set the virtual position to NaN
                virtualPosition.setX(NaN);
                virtualPosition.setY(NaN);
            }
        }
        return virtualPosition;
    }

    /**
     * Renders the hit boxes of the entities
     */
    public static void renderHitBoxes() {
        for (Entity entity : Game.em.getAllWithComponent(GraphicComponent.class)) {
            GraphicComponent graphicComponent = entity.getComponent(GraphicComponent.class);
            updateVirtualPosition(entity, graphicComponent);
            Vector2f virtualPosition = entity.getComponent(TransformComponent.class).getVirtualPosition();
            // Skip if the entity is not visible
            if (Float.isNaN(virtualPosition.getX()) || Float.isNaN(virtualPosition.getY()))
                continue;

            // Render the hit box for the entity
            BoundingBox boundingBox = entity.getComponent(PhysicComponent.class).getHitBox();
            Game.gc.setStroke(Color.LAWNGREEN);
            Game.gc.strokeRect(
                    virtualPosition.getX() + (boundingBox.getMinX() + graphicComponent.getWidth() / 2f) * Camera.scale,
                    virtualPosition.getY() + (boundingBox.getMinY() + graphicComponent.getHeight() / 2f) * Camera.scale,
                    boundingBox.getWidth() * Camera.scale,
                    boundingBox.getHeight() * Camera.scale
            );
        }
    }
}
