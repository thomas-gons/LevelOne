package suchagame.ecs.system;

import javafx.scene.input.KeyCode;
import suchagame.ecs.component.PhysicComponent;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Mob;
import suchagame.ecs.entity.Player;
import suchagame.ecs.entity.Projectile;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

/**
 * System that handles the movement of the entities
 */
public class MovementSystem extends System {

    /**
     * Updates the position of the entity according to its speed and collisions and eventual input
     */
    public static void update() {
        for (Entity entity : Game.em.getAllWithComponent(TransformComponent.class)) {
            if (entity instanceof Mob) {
                updateMob((Mob) entity);
            } else if (entity instanceof Projectile) {
                updateProjectile((Projectile) entity);
            } else if (entity instanceof Player) {
                updatePlayer((Player) entity);
            }
        }
    }

    /**
     * Updates the position of the player according to its speed and collisions and eventual input
     * @param player Player to update
     */
    public static void updatePlayer(Player player) {
        TransformComponent transformComponent = player.getComponent(TransformComponent.class);
        PhysicComponent physicComponent = player.getComponent(PhysicComponent.class);
        float speed = player.getComponent(StatsComponent.class).getStat("spd");

        float dx = 0, dy = 0;
        if (Game.sm.get(InputSystem.class).isKeyDown(KeyCode.Z)) dy -= 1;
        if (Game.sm.get(InputSystem.class).isKeyDown(KeyCode.D)) dx += 1;
        if (Game.sm.get(InputSystem.class).isKeyDown(KeyCode.S)) dy += 1;
        if (Game.sm.get(InputSystem.class).isKeyDown(KeyCode.Q)) dx -= 1;

        // Normalize the vector if it is diagonal
        if (dx != 0 && dy != 0) {
            dx /= 1.4142;
            dy /= 1.4142;
        }

        // Update the velocity and check if the player can move to the new position
        Vector2f checkPosition = transformComponent.getPositionDeepCopy();
        physicComponent.setVelocity(new Vector2f(dx, dy).mul(speed));
        checkPosition.translate(physicComponent.getVelocity());

        if (PhysicSystem.checkCollision(player, checkPosition))
            (player.getComponent(TransformComponent.class)).setPosition(checkPosition);
    }

    /**
     * Updates the position of the projectile according to its speed and collisions
     * @param projectile Projectile to update
     */
    public static void updateProjectile(Projectile projectile) {
        Vector2f projectilePosition = projectile.getComponent(TransformComponent.class).getPositionDeepCopy();
        Vector2f projectileVelocity = projectile.getComponent(PhysicComponent.class).getVelocity();
        Vector2f checkPosition = projectilePosition.add(projectileVelocity);
        // if the projectile collides with the map, it is destroyed
        if (PhysicSystem.checkCollision(projectile, checkPosition))
            (projectile.getComponent(TransformComponent.class)).setPosition(checkPosition);
        else {
            projectile.getComponent(StatsComponent.class).setObservableStat("hp", 0f);
        }
    }
    /**
     * Updates the position of the mob according to its speed and collisions
     * @param mob Mob to update
     */
    public static void updateMob(Mob mob) {
        Vector2f mobPosition = mob.getComponent(TransformComponent.class).getPositionDeepCopy();
        Vector2f playerPosition = Game.em.getPlayer().getComponent(TransformComponent.class).getPosition();
        playerPosition = playerPosition.add(new Vector2f(8, 8));

        // move the mob towards the player
        float speed = mob.getComponent(StatsComponent.class).getStat("spd");
        Vector2f direction = playerPosition.sub(mobPosition).normalize();
        PhysicComponent physicComponent = mob.getComponent(PhysicComponent.class);
        physicComponent.setVelocity(direction.mul(speed));
        Vector2f checkPosition = mobPosition.add(physicComponent.getVelocity());

        // if the mob doesn't collide with the map, it is moved to the new position
        if (PhysicSystem.checkCollision(mob, checkPosition))
            (mob.getComponent(TransformComponent.class)).setPosition(checkPosition);
    }
}
