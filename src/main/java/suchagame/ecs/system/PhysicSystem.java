package suchagame.ecs.system;

import javafx.geometry.BoundingBox;
import javafx.scene.paint.Color;
import suchagame.ecs.EntityManager;
import suchagame.ecs.component.LayersComponent;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.component.PhysicComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.MapEntity;
import suchagame.ui.Game;
import suchagame.utils.Vector2;

public class PhysicSystem {
    public static boolean checkCollision(Entity entity, Vector2<Float> checkPosition) {
        if (!entity.hasComponent(suchagame.ecs.component.PhysicComponent.class))
            return false;

        PhysicComponent physicComponent = entity.getComponent(PhysicComponent.class);
        BoundingBox boundingBox = physicComponent.getTranslatedBoundingBox(checkPosition);
        if (mapCheckCollision(boundingBox, checkPosition)) {
            return true;
        }

        Game.gc.setStroke(Color.RED);
        Game.gc.setLineWidth(10);
        Game.gc.strokeRect(
                boundingBox.getMinX(),
                boundingBox.getMinY(),
                boundingBox.getWidth(),
                boundingBox.getHeight()
        );
        // check collision with other entities
        for (Entity otherEntity : EntityManager.instance.getAllWithComponent(PhysicComponent.class)) {
            if (entity == otherEntity)
                continue;

            PhysicComponent otherPhysicComponent = otherEntity.getComponent(PhysicComponent.class);
            Vector2<Float> otherEntityPosition = otherEntity.getComponent(TransformComponent.class).getPosition();
            BoundingBox otherBoundingBox = otherPhysicComponent.getTranslatedBoundingBox(otherEntityPosition);
            if (boundingBox.intersects(otherBoundingBox))
                return true;
        }

        return false;
    }

    private static boolean mapCheckCollision(BoundingBox entityBB, Vector2<Float> checkPosition) {
        if (checkPosition.getX() < 0 || checkPosition.getY() < 0 ||
                checkPosition.getX() > Game.width || checkPosition.getY() > Game.height) {
            return true;
        }
        // check collision with map
        Vector2<Integer> upperLeftInTiles = new Vector2<>(
                (int) entityBB.getMinX() / MapEntity.defaultTileSize,
                (int) entityBB.getMinY() / MapEntity.defaultTileSize
        );
        Vector2<Integer> lowerRightInTiles = new Vector2<>(
                (int) entityBB.getMaxX() / MapEntity.defaultTileSize,
                (int) entityBB.getMaxY() / MapEntity.defaultTileSize
        );
        boolean[][] collisionLayerCheck = (MapEntity.map.getComponent(LayersComponent.class)).getCollisionLayerCheck();
        try {
            for (int x = upperLeftInTiles.getX(); x <= lowerRightInTiles.getX(); x++) {
                for (int y = upperLeftInTiles.getY(); y <= lowerRightInTiles.getY(); y++) {
                    if (collisionLayerCheck[y][x])
                        return true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }

        return false;
    }
}
