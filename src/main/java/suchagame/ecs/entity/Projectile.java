package suchagame.ecs.entity;

import javafx.beans.property.SimpleFloatProperty;
import suchagame.ecs.component.*;
import suchagame.ecs.component.AnimationComponent.ACTION;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

import java.util.HashMap;
import java.util.Map;



public class Projectile extends Entity {

    private static final Map<String, Integer> manaCostPerProjectile = Map.of(
            "fireball", 1
    );
    private final int manaCost;

    public Projectile(String projectileType) {
        super();
        this.manaCost = manaCostPerProjectile.get(projectileType);

        Vector2f position = Game.em.getPlayer().getComponent(TransformComponent.class).getPosition();
        boolean isFacingRight = Game.em.getPlayer().getComponent(PhysicComponent.class).getVelocity().getX() >= 0f;

        addComponent(new TransformComponent(position.getX() + 7, position.getY() + 37));
        addComponent(new GraphicComponent(isFacingRight ? "fireball_right.png" : "fireball_left.png"));
        addComponent(new AnimationComponent(
                getComponent(GraphicComponent.class),
                12, ACTION.IDLE, 0,
                new int[]{5, 7}));


        addComponent(new PhysicComponent(
                17, 15, -8, -6,
                20
        ));

        addComponent(new StatsComponent(
                new HashMap<>(Map.of(
                    "atk", 100f,
                    "def", 1f,
                    "spd", 3f
                )),
                new HashMap<>(Map.of(
                    "hp", new SimpleFloatProperty(50f)
                ))
        ));

        getComponent(PhysicComponent.class).setVelocity(new Vector2f(
                    isFacingRight ? 3f : -3f,
                0
        ));
    }

    public int getManaCost() {
        return manaCost;
    }
}
