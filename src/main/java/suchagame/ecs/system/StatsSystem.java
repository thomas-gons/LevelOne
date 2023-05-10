package suchagame.ecs.system;

import javafx.beans.property.SimpleFloatProperty;
import suchagame.ecs.component.AnimationComponent;
import suchagame.ecs.component.PhysicComponent;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Mob;
import suchagame.ecs.entity.Player;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

import java.util.Map;

public class StatsSystem extends System {

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
                    if (entity instanceof Player)
                        Game.hud.updateStatBar("mp", newValue.floatValue());
                    ;
                });
                default -> {
                    ;
                }
            }
        }
    }

    public static void takeDamage(Entity source, Entity target) {
        float damage = source.getComponent(StatsComponent.class).getStat("atk");
        target.getComponent(StatsComponent.class).alterObservableStat("hp", -damage);
    }
}
