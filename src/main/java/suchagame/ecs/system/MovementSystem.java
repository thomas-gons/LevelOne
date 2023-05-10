package suchagame.ecs.system;

import javafx.scene.input.KeyCode;
import suchagame.ecs.component.*;
import suchagame.ecs.entity.*;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

public class MovementSystem extends System {
    public static void update() {
        for (Entity entity : Game.em.getAllWithComponent(TransformComponent.class)) {
            if (entity instanceof Mob) {
                updateMob((Mob) entity);
            } else if (entity instanceof Projectile) {
                updateProjectile((Projectile) entity);
            } else {
                updatePlayer((Player) entity);
            }
        }
    }

    public static void updatePlayer(Player player) {
        TransformComponent transformComponent = player.getComponent(TransformComponent.class);
        InputComponent inputComponent = player.getComponent(InputComponent.class);
        PhysicComponent physicComponent = player.getComponent(PhysicComponent.class);
        float speed = player.getComponent(StatsComponent.class).getStat("spd");

        float dx = 0, dy = 0;
        if (inputComponent.keyDown.get(KeyCode.Z)) dy -= 1;
        if (inputComponent.keyDown.get(KeyCode.D)) dx += 1;
        if (inputComponent.keyDown.get(KeyCode.S)) dy += 1;
        if (inputComponent.keyDown.get(KeyCode.Q)) dx -= 1;

        if (dx != 0 && dy != 0) {
            dx /= 1.4142;
            dy /= 1.4142;
        }

        Vector2f checkPosition = transformComponent.getPositionDeepCopy();
        physicComponent.setVelocity(new Vector2f(dx, dy).mul(speed));
        checkPosition.translate(physicComponent.getVelocity());

        if (PhysicSystem.checkCollision(player, checkPosition))
            (player.getComponent(TransformComponent.class)).setPosition(checkPosition);
    }

    public static void updateProjectile(Projectile projectile) {
        Vector2f projectilePosition = projectile.getComponent(TransformComponent.class).getPositionDeepCopy();
        Vector2f projectileVelocity = projectile.getComponent(PhysicComponent.class).getVelocity();
        Vector2f checkPosition = projectilePosition.add(projectileVelocity);
        if (PhysicSystem.checkCollision(projectile, checkPosition))
            (projectile.getComponent(TransformComponent.class)).setPosition(checkPosition);
        else {
            projectile.getComponent(StatsComponent.class).setObservableStat("hp", 0f);
        }
    }

    public static void updateMob(Mob mob) {
        Vector2f mobPosition = mob.getComponent(TransformComponent.class).getPositionDeepCopy();
        Vector2f playerPosition = Game.em.getPlayer().getComponent(TransformComponent.class).getPosition();
        GraphicComponent graphicComponent = Game.em.getPlayer().getComponent(GraphicComponent.class);
        playerPosition = playerPosition.add(new Vector2f(graphicComponent.getWidth(), graphicComponent.getHeight()).div(2));
        float speed = mob.getComponent(StatsComponent.class).getStat("spd");
        Vector2f direction = playerPosition.sub(mobPosition).normalize();
        PhysicComponent physicComponent = mob.getComponent(PhysicComponent.class);
        physicComponent.setVelocity(direction.mul(speed));
        Vector2f checkPosition = mobPosition.add(physicComponent.getVelocity());
        if (PhysicSystem.checkCollision(mob, checkPosition))
            (mob.getComponent(TransformComponent.class)).setPosition(checkPosition);
        }
}
