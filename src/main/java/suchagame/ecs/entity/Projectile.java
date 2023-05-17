package suchagame.ecs.entity;

import suchagame.ecs.component.PhysicComponent;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

public class Projectile extends Entity {
    private SIDE side;
    private boolean isStatic;
    private int manaCost;

    public Projectile(boolean isOriented, boolean isStatic, int manaCost) {
        super();
        if (isOriented) {
            Vector2f playerVelocity = Game.em.getPlayer().getComponent(PhysicComponent.class).getVelocity();
            this.side = playerVelocity.getX() > 0 ? SIDE.RIGHT : SIDE.LEFT;
        } else
            this.side = SIDE.NO;

        this.isStatic = isStatic;
        this.manaCost = manaCost;
    }

    public int getManaCost() {
        return this.manaCost;
    }

    public SIDE getOrientation() {
        return this.side;
    }

    public enum SIDE {
        RIGHT,
        LEFT,
        NO,
    }
}