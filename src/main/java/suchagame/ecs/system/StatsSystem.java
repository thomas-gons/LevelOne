package suchagame.ecs.system;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleFloatProperty;
import javafx.util.Duration;
import suchagame.ecs.component.AnimationComponent;
import suchagame.ecs.component.PhysicComponent;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Player;
import suchagame.ecs.entity.Projectile;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

import java.util.Map;

public class StatsSystem extends System {

    public static Timeline regenTimeline;
    public StatsSystem() {
        super();
        statsRegenerate();
    }
    public static void addObserver(Entity entity) {
        StatsComponent stats = entity.getComponent(StatsComponent.class);
        Map<String, SimpleFloatProperty> observableStats = stats.getObservableStats();
        if (observableStats == null)
            return;
        for (Map.Entry<String, SimpleFloatProperty> entry : observableStats.entrySet()) {
            switch (entry.getKey()) {
                case "hp" -> entry.getValue().addListener((observable, oldValue, newValue) -> {
                    if (entity instanceof Player)
                        Game.hud.updateStatBar("hp", newValue.floatValue());

                    if (newValue.floatValue() <= 0) {
                        Runnable r = () -> {
                            Game.em.removeEntity(entity);
                        };
                        entity.getComponent(PhysicComponent.class).setVelocity(new Vector2f(0));
                        InventorySystem.leaveInventory(entity);
                        AnimationSystem.triggerAction(entity, r, AnimationComponent.ACTION.DEATH);
                    }
                });
                case "mp" -> entry.getValue().addListener((observable, oldValue, newValue) -> {
                    if (entity instanceof Player) {
                        Game.hud.updateStatBar("mp", newValue.floatValue());
                    }
                });
                default -> {
                    ;
                }
            }
        }
    }

    public static void takeDamage(Entity source, Entity target) {
        if (source instanceof Player && target instanceof Projectile ||
        source instanceof Projectile && target instanceof Player)
            return;
        float damage = source.getComponent(StatsComponent.class).getStat("atk");
        target.getComponent(StatsComponent.class).alterObservableStat("hp", -damage);
    }

    public static void statsRegenerate() {
        StatsComponent statsComponent = Game.em.getPlayer().getComponent(StatsComponent.class);
        regenTimeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
            if (statsComponent.getObservableStat("mp") < statsComponent.getStat("mp_max"))
                Game.em.getPlayer().getComponent(StatsComponent.class).alterObservableStat("mp", 9);
        }));

        regenTimeline.setCycleCount(Timeline.INDEFINITE);
        regenTimeline.play();
    }
}
