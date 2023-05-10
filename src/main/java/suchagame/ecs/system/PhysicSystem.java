package suchagame.ecs.system;

import javafx.geometry.BoundingBox;
import suchagame.ecs.component.*;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.MapEntity;
import suchagame.ecs.entity.Mob;
import suchagame.ecs.entity.Projectile;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

public class PhysicSystem {

    private static long lastTime = java.lang.System.currentTimeMillis();
    public static boolean checkCollision(Entity entity, Vector2f entityPosition) {
        if (!entity.hasComponent(suchagame.ecs.component.PhysicComponent.class))
            return true;

        PhysicComponent physicComponent = entity.getComponent(PhysicComponent.class);
        BoundingBox boundingBox = physicComponent.getTranslatedBoundingBox(entityPosition);

        // check collision with other entities
        for (Entity otherEntity : Game.em.getAllWithComponent(PhysicComponent.class)) {
            if (entity == otherEntity)
                continue;

            PhysicComponent otherPhysicComponent = otherEntity.getComponent(PhysicComponent.class);
            Vector2f otherEntityPosition = otherEntity.getComponent(TransformComponent.class).getPosition();
            BoundingBox otherBoundingBox = otherPhysicComponent.getTranslatedBoundingBox(otherEntityPosition);
            if (boundingBox.intersects(otherBoundingBox)) {
                if (entity.getClass() != otherEntity.getClass() && (java.lang.System.currentTimeMillis() - lastTime > 100)) {
                    StatsSystem.takeDamage(entity, otherEntity);
                    StatsSystem.takeDamage(otherEntity, entity);
                    lastTime = java.lang.System.currentTimeMillis();
                }
                handleCollision(physicComponent, otherPhysicComponent, entityPosition, otherEntityPosition);
            }
        }
        return !mapCheckCollision(boundingBox, entityPosition);
    }

    private static void handleCollision(
            PhysicComponent physicComponentA, PhysicComponent physicComponentB,
            Vector2f positionA, Vector2f positionB) {


        Vector2f velocityA = physicComponentA.getVelocity();
        Vector2f velocityB = physicComponentB.getVelocity();
        Vector2f collisionNormal = positionA.sub(positionB).normalize();
        Vector2f relativeVelocity = velocityA.sub(velocityB);
        float impulse = -(1 + 0.7f) * relativeVelocity.dot(collisionNormal) /
                collisionNormal.dot(collisionNormal) * (1 / physicComponentA.getMass() + 1 / physicComponentB.getMass());
        applyForce(positionA, collisionNormal.mul(impulse), velocityA, physicComponentA.getMass());
        applyForce(positionB, collisionNormal.mul(-impulse), velocityB, physicComponentB.getMass());
    }

    private static void applyForce(Vector2f position, Vector2f force, Vector2f velocity, float mass) {
        Vector2f acceleration = force.div(mass);
        acceleration.translate(velocity);
        position.translate(acceleration.div(4));
    }

    private static boolean mapCheckCollision(BoundingBox entityBB, Vector2f entityPosition) {
        if (entityPosition.getX() < 0 || entityPosition.getY() < 0 ||
                entityPosition.getX() > Game.width || entityPosition.getY() > Game.height) {
            return true;
        }
        // check collision with map
        int[] upperLeftInTiles = new int[]{
                (int) entityBB.getMinX() / MapEntity.defaultTileSize,
                (int) entityBB.getMinY() / MapEntity.defaultTileSize
        };
        int[] lowerRightInTiles = new int[]{
                (int) entityBB.getMaxX() / MapEntity.defaultTileSize,
                (int) entityBB.getMaxY() / MapEntity.defaultTileSize
        };
        boolean[][] collisionLayerCheck = (Game.em.getMap().getComponent(LayersComponent.class)).getCollisionLayerCheck();
        try {
            for (int x = upperLeftInTiles[0]; x <= lowerRightInTiles[0]; x++) {
                for (int y = upperLeftInTiles[1]; y <= lowerRightInTiles[1]; y++) {
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
