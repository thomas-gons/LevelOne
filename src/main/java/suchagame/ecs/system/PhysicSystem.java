package suchagame.ecs.system;

import javafx.geometry.BoundingBox;
import suchagame.ecs.component.LayersComponent;
import suchagame.ecs.component.PhysicComponent;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.MapEntity;
import suchagame.ecs.entity.Mob;
import suchagame.ecs.entity.Player;
import suchagame.ui.Game;
import suchagame.utils.Utils;
import suchagame.utils.Vector2f;

import java.util.Objects;


/**
 * The PhysicSystem class handles collision detection and resolution for entities in the game.
 * It checks for collisions between entities and the map boundaries, as well as collisions between entities themselves.
 */
public class PhysicSystem {

    /**
     * Checks for collision of an entity with the environment and other nearby entities.
     * If a collision occurs, appropriate actions are taken, such as dealing damage or adjusting positions.
     *
     * @param entity         the entity to check collision for
     * @param entityPosition the current position of the entity
     * @return true if no collision occurred, false otherwise
     */
    public static boolean checkCollision(Entity entity, Vector2f entityPosition) {
        if (!entity.hasComponent(PhysicComponent.class))
            return true;

        // Get the physics component and the bounding box of the entity
        PhysicComponent physicComponent = entity.getComponent(PhysicComponent.class);
        BoundingBox hitBox = Utils.translateHitBox(entityPosition, physicComponent.getHitBox());

        // Check for collision with nearby entities
        checkCollisionWithNearbyEntities(entity, hitBox);

        // Check for collision with the map boundaries
        return mapCheckCollision(entity, hitBox, entityPosition);
    }

    /**
     * Checks for collisions between the given entity and other nearby entities.
     * If a collision occurs, appropriate actions are taken, such as dealing damage.
     *
     * @param entity  the entity to check collision for
     * @param hitBox  the bounding box of the entity
     */
    private static void checkCollisionWithNearbyEntities(Entity entity, BoundingBox hitBox) {
        // Iterate through all entities with a physics component
        for (Entity otherEntity : Game.em.getAllWithComponent(PhysicComponent.class)) {
            if (entity == otherEntity)
                continue;

            // Skip entities with NaN positions (invalid or inactive entities)
            if (Objects.equals(otherEntity.getComponent(TransformComponent.class).getVirtualPosition(), new Vector2f(Float.NaN)))
                continue;

            PhysicComponent otherPhysicComponent = otherEntity.getComponent(PhysicComponent.class);
            Vector2f otherEntityPosition = otherEntity.getComponent(TransformComponent.class).getPosition();
            BoundingBox otherHitBox = Utils.translateHitBox(otherEntityPosition, otherPhysicComponent.getHitBox());

            // Check for collision between the two entities
            if (hitBox.intersects(otherHitBox)) {
                if (entity.getClass() != otherEntity.getClass()) {
                    // If the entity is a mob, it can only attack every 250ms
                    if (entity instanceof Mob && java.lang.System.currentTimeMillis() - ((Mob) entity).getLastAttack() > 250) {
                        // Deal damage to the other entity
                        StatsSystem.takeDamage(entity, otherEntity);
                        // Update the last attack time of the mob entity
                        ((Mob) entity).setLastAttack(java.lang.System.currentTimeMillis());
                    } else if (!(entity instanceof Mob)) {
                        // Deal damage to the other entity
                        StatsSystem.takeDamage(entity, otherEntity);
                    }
                }
                // handleCollision(physicComponent, otherPhysicComponent, entityPosition, otherEntityPosition);
            }
        }
    }

    /**
     * Handles the collision between two entities by resolving their positions and velocities.
     * The collision normal, relative velocity, and coefficients of restitution are used to calculate the impulse and update the entities' positions.
     *
     * @param entityA          the first entity involved in the collision
     * @param entityB          the second entity involved in the collision
     * @param physicComponentA the physics component of entityA
     * @param physicComponentB the physics component of entityB
     * @param positionA        the position of entityA
     * @param positionB        the position of entityB
     */
    @SuppressWarnings("unused")
    private static void handleCollision(
            Entity entityA, Entity entityB,
            PhysicComponent physicComponentA, PhysicComponent physicComponentB,
            Vector2f positionA, Vector2f positionB
    ) {
        // Get the velocities, collision normal, and relative velocity
        Vector2f velocityA = physicComponentA.getVelocity();
        Vector2f velocityB = physicComponentB.getVelocity();
        Vector2f collisionNormal = positionA.sub(positionB).normalize();
        Vector2f relativeVelocity = velocityA.sub(velocityB);

        // Calculate the impulse using the coefficients of restitution
        float coefficientOfRestitution = 0.7f;
        float impulse = -(1 + coefficientOfRestitution) * relativeVelocity.dot(collisionNormal) /
                ((1 / physicComponentA.getMass()) + (1 / physicComponentB.getMass()));

        // Apply forces to the entities based on the collision
        applyForce(entityA, positionA, collisionNormal.mul(impulse), velocityA, physicComponentA.getHitBox(), physicComponentA.getMass());
        applyForce(entityB, positionB, collisionNormal.mul(-impulse), velocityB, physicComponentB.getHitBox(), physicComponentB.getMass());

        // Calculate and resolve interpenetration
        float totalMass = physicComponentA.getMass() + physicComponentB.getMass();
        Vector2f interpenetration = collisionNormal.mul(getIntersectionDepth(
                Utils.translateHitBox(positionA, physicComponentA.getHitBox()),
                Utils.translateHitBox(positionB, physicComponentB.getHitBox()),
                collisionNormal).div(totalMass));

        positionA.translate(interpenetration);
        positionB.translate(interpenetration.mul(-1));
    }

    /**
     * Applies a force to an entity based on its mass and velocity.
     * The force is used to calculate the acceleration, which is then added to the velocity.
     * If a collision with the map occurs, the entity's position is adjusted accordingly.
     *
     * @param entity     the entity to apply the force to
     * @param position   the position of the entity
     * @param force      the force to apply
     * @param velocity   the velocity of the entity
     * @param bb         the bounding box of the entity
     * @param mass       the mass of the entity
     */
    private static void applyForce(
            Entity entity,
            Vector2f position,
            Vector2f force,
            Vector2f velocity,
            BoundingBox bb,
            float mass) {

        // Calculate the acceleration based on the force and mass
        Vector2f acceleration = force.div(mass);
        acceleration.translate(velocity);

        // Check for collision with the map
        if (mapCheckCollision(entity, bb, position.add(acceleration)))
            position.translate(acceleration);
    }

    /**
     * Calculates the intersection depth between two bounding boxes along a given normal vector.
     *
     * @param bbA    the first bounding box
     * @param bbB    the second bounding box
     * @param normal the normal vector along which to calculate the intersection depth
     * @return a vector representing the intersection depth along the given normal
     */
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

    /**
     * Checks for collision of an entity with the map boundaries and handles collision with the map tiles.
     * If the entity is outside the map boundaries or collides with a solid tile, it is considered a collision.
     * If the entity falls into a hole tile, it is killed if it is a player or a mob.
     *
     * @param entity         the entity to check collision for
     * @param entityBB       the bounding box of the entity
     * @param entityPosition the current position of the entity
     * @return true if no collision occurred, false otherwise
     */
    private static boolean mapCheckCollision(Entity entity, BoundingBox entityBB, Vector2f entityPosition) {
        // Check if the entity is outside the map boundaries
        if (entityPosition.getX() < 0 || entityPosition.getY() < 0 ||
                entityPosition.getX() > Game.width || entityPosition.getY() > Game.height) {
            return false;
        }

        // Check collision with the map
        int[] upperLeftInTiles = new int[]{
                (int) entityBB.getMinX() / MapEntity.defaultTileSize,
                (int) entityBB.getMinY() / MapEntity.defaultTileSize
        };
        int[] lowerRightInTiles = new int[]{
                (int) entityBB.getMaxX() / MapEntity.defaultTileSize,
                (int) entityBB.getMaxY() / MapEntity.defaultTileSize
        };
        LayersComponent layersComponent = Game.em.getMap().getComponent(LayersComponent.class);
        boolean[][] collisionLayerCheck = layersComponent.getCollidingLayer();

        try {
            for (int x = upperLeftInTiles[0]; x <= lowerRightInTiles[0]; x++) {
                for (int y = upperLeftInTiles[1]; y <= lowerRightInTiles[1]; y++) {
                    // Check for collision with solid tiles
                    if (collisionLayerCheck[y][x])
                        return false;

                    // Check if the entity falls into a hole tile and kill it if it is a player or a mob
                    if (layersComponent.isHoleTile(y, x) && (entity instanceof Player || entity instanceof Mob)) {
                        if (entity instanceof Player)
                            Player.setDeathCause("You've fell into a hole");
                        Game.sm.get(GameplaySystem.class).killEntity(entity, false);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }
}