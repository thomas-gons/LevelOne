package suchagame.ecs.entity;

import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.BoundingBox;
import suchagame.ecs.component.*;
import suchagame.ecs.component.AnimationComponent.ACTION;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

import java.util.HashMap;
import java.util.Map;



public class Projectile extends Entity {

    private final ProjectileType projectileType;

    public Projectile(String tag, Vector2f playerVelocity) {
        super();
        this.projectileType = ProjectileType.valueOf(tag.toUpperCase());
        Vector2f position = Game.em.getPlayer().getComponent(TransformComponent.class).getPosition();

        addComponent(new PhysicComponent(projectileType.boundingBox, projectileType.mass));

        addComponent(new TransformComponent(position.add(projectileType.graphicOffset)));
        if (projectileType.isStatic) {
            boolean isFacingRight = playerVelocity.getX() >= 0f;
            addComponent(new GraphicComponent(String.format("%s_%s.png", projectileType.tag, isFacingRight ? "right" : "left")));
            getComponent(PhysicComponent.class).setVelocity(new Vector2f(
                    isFacingRight ? projectileType.spd : -projectileType.spd,
                    0
            ));
        } else {
            addComponent(new GraphicComponent(String.format("%s.png", projectileType.tag)));
            getComponent(PhysicComponent.class).setVelocity(new Vector2f(0));
        }

        addComponent(new AnimationComponent(
                getComponent(GraphicComponent.class),
                projectileType.framerate, ACTION.IDLE, 0,
                projectileType.frameCount
        ));


        addComponent(new StatsComponent(
                new HashMap<>(Map.of(
                    "atk", projectileType.atk,
                    "spd", projectileType.spd
                )),
                new HashMap<>(Map.of(
                    "hp", new SimpleFloatProperty(projectileType.hp)
                ))
        ));
    }

    public int getManaCost() {
        return projectileType.manaCost;
    }

    public enum ProjectileType {
        FIREBALL("fireball", 10, true,
                12, new int[]{5, 7},
                new Vector2f(7, 37),
                new BoundingBox(-8, -6, 17, 15), 20,
                100f, 3f, 50000000f);

        private final String tag;
        private final int manaCost;
        private final boolean isStatic;
        private final int framerate;
        private final int[] frameCount;
        private final Vector2f graphicOffset;
        private final BoundingBox boundingBox;
        private final float mass;

        private final float atk;
        private final float spd;
        private final float hp;

        ProjectileType(String tag, int manaCost, boolean isOriented,
                       int framerate, int[] frameCount,
                       Vector2f graphicOffset, BoundingBox boundingBox, float mass,
                       float atk, float spd, float hp
        ) {

            this.tag = tag;
            this.manaCost = manaCost;
            this.isStatic = isOriented;
            this.framerate = framerate;
            this.frameCount = frameCount;
            this.graphicOffset = graphicOffset;
            this.boundingBox = boundingBox;
            this.mass = mass;
            this.atk = atk;
            this.spd = spd;
            this.hp = hp;
        }

        public static int getManaCost(String tag) {
            return ProjectileType.valueOf(tag.toUpperCase()).manaCost;
        }
    }
}
