package suchagame.ecs.system;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleFloatProperty;
import javafx.util.Duration;
import suchagame.ecs.component.FlagComponent;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Item;
import suchagame.ecs.entity.Player;
import suchagame.ecs.entity.Projectile;
import suchagame.ui.Game;

import java.util.Map;

/**
 * System that handles the stats of the entities.
 */
public class StatsSystem extends System {

    // Timeline for the passive regeneration of the stats
    public static Timeline regenTimeline;

    public StatsSystem() {
        super();
        statsRegenerate();
    }

    /**
     * Adds an observer to the entity's observable stats and defines the behavior when the stats change.
     *
     * @param entity the entity to observe
     */
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

                    if (newValue.floatValue() <= 0 && stats.isAlive) {
                        if (entity instanceof Player) {
                            Player.setDeathCause("You has been slain by a crowd of slimes!");
                        }
                        Game.sm.get(GameplaySystem.class).killEntity(entity, true);
                    }
                });
                case "mp" -> entry.getValue().addListener((observable, oldValue, newValue) -> {
                    if (entity instanceof Player) {
                        Game.hud.updateStatBar("mp", newValue.floatValue());
                    }
                });
                default -> {
                    // Handle other stats if necessary
                }
            }
        }
    }

    /**
     * Inflicts damage to the target entity based on the attack stat of the source entity.
     *
     * @param source the entity that deals damage
     * @param target the entity that receives damage
     */
    public static void takeDamage(Entity source, Entity target) {
        // Ignore damage between players and projectiles to prevent friendly fire
        if (source instanceof Player && target instanceof Projectile ||
                source instanceof Projectile && target instanceof Player)
            return;

        FlagComponent targetFlag = target.getComponent(FlagComponent.class);
        if (targetFlag != null && targetFlag.getFlag("invincibility"))
            return;

        float damage = source.getComponent(StatsComponent.class).getStat("atk");
        target.getComponent(StatsComponent.class).alterObservableStat("hp", -damage);
    }

    /**
     * Applies the effects of using a consumable item to the entity's stats.
     *
     * @param entity the entity using the consumable item
     * @param item   the consumable item being used
     */
    public static void useConsumable(Entity entity, Item item) {
        StatsComponent itemStats = item.getComponent(StatsComponent.class);
        for (Map.Entry<String, Float> entry : itemStats.getStats().entrySet()) {
            entity.getComponent(StatsComponent.class).alterUnknownStat(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sets up the timeline for the passive regeneration of the player's stats.
     */
    public static void statsRegenerate() {
        StatsComponent statsComponent = Game.em.getPlayer().getComponent(StatsComponent.class);
        regenTimeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
            if (statsComponent.getObservableStat("mp") < statsComponent.getStat("mp_max")) {
                // Increment the MP stat up to the maximum value or 9 points per regeneration cycle
                statsComponent.alterObservableStat("mp",
                        Math.min(Math.abs(statsComponent.getStat("mp_max") - statsComponent.getObservableStat("mp")), 9));
            }
        }));

        regenTimeline.setCycleCount(Timeline.INDEFINITE);
        regenTimeline.play();
    }
}