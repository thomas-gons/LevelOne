package suchagame.ecs.system;

import javafx.geometry.BoundingBox;
import suchagame.ecs.component.*;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.MapEntity;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

public class PhysicSystem {

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
                if (entity.getClass() != otherEntity.getClass()) {
                    StatsSystem.takeDamage(entity, otherEntity);
                    StatsSystem.takeDamage(otherEntity, entity);
                }
                handleCollision(physicComponent, otherPhysicComponent, entityPosition, otherEntityPosition);
            }
        }
        return mapCheckCollision(boundingBox, entityPosition);
    }

    private static void handleCollision(
        PhysicComponent physicComponentA, PhysicComponent physicComponentB,
        Vector2f positionA, Vector2f positionB
    ) {
        Vector2f velocityA = physicComponentA.getVelocity();
        Vector2f velocityB = physicComponentB.getVelocity();
        Vector2f collisionNormal = positionA.sub(positionB).normalize();
        Vector2f relativeVelocity = velocityA.sub(velocityB);

        // Calculate impulse
        float coeffOfRestitution = 0.7f;
        float impulse = -(1 + coeffOfRestitution) * relativeVelocity.dot(collisionNormal) /
            ((1 / physicComponentA.getMass()) + (1 / physicComponentB.getMass()));

        // Apply impulse
        applyForce(positionA, collisionNormal.mul(impulse), velocityA, physicComponentA.getBoundingBox(), physicComponentA.getMass());
        applyForce(positionB, collisionNormal.mul(-impulse), velocityB, physicComponentB.getBoundingBox(), physicComponentB.getMass());

        // Resolve interpenetration
         float totalMass = physicComponentA.getMass() + physicComponentB.getMass();
//        Vector2f interpenetration = collisionNormal.mul(getIntersectionDepth(
//                    physicComponentA.getTranslatedBoundingBox(positionA),
//                    physicComponentB.getTranslatedBoundingBox(positionB),
//                    collisionNormal).div(totalMass));
//
//        positionA.translate(interpenetration);
//        positionB.translate(interpenetration.mul(-1));
    }

     private static void applyForce(Vector2f position, Vector2f force, Vector2f velocity, BoundingBox bb, float mass) {
        Vector2f acceleration = force.div(mass);
        acceleration.translate(velocity);
        if (mapCheckCollision(bb, position.add(acceleration)))
            position.translate(acceleration);
    }

    private static Vector2f getIntersectionDepth(BoundingBox bbA, BoundingBox bbB, Vector2f normal) {
        float minIntervalDistance = (float) Math.abs(bbB.getMinX() - bbA.getMaxX());

        float intervalDistance = (float) Math.abs(bbA.getMinX() - bbB.getMaxX());
        if (intervalDistance < minIntervalDistance) {
            minIntervalDistance = intervalDistance;
            normal.set(-normal.getX(), normal.getY());
        }

        intervalDistance = (float) Math.abs(bbB.getMinY() - bbA.getMaxY());
        if (intervalDistance < minIntervalDistance) {
            minIntervalDistance = intervalDistance;
            normal.set(normal.getX(), -normal.getY());
        }

        intervalDistance = (float) Math.abs(bbA.getMinY() - bbB.getMaxY());
        if (intervalDistance < minIntervalDistance) {
            normal.set(normal.getX(), -normal.getY());
        }

        float depthX = normal.getX() * minIntervalDistance;
        float depthY = normal.getY() * minIntervalDistance;
        return new Vector2f(depthX, depthY);
    }


    private static boolean mapCheckCollision(BoundingBox entityBB, Vector2f entityPosition) {
        if (entityPosition.getX() < 0 || entityPosition.getY() < 0 ||
                entityPosition.getX() > Game.width || entityPosition.getY() > Game.height) {
            return false;
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
                        return false;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }
}
