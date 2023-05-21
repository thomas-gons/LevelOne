package suchagame.ecs.entity;

import javafx.beans.property.SimpleFloatProperty;
import suchagame.ecs.component.*;
import suchagame.ui.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all NPCs.
 */
public class NPC extends Entity {

    /**
     * Constructs a NPC object with all the necessary components.
     * hard coded for now
     */
    public NPC() {
        super();
        addComponent(new TransformComponent(700, 700));
        addComponent(new GraphicComponent("blacksmith.png"));
        addComponent(new AnimationComponent(
                getComponent(GraphicComponent.class),
                10, AnimationComponent.ACTION.IDLE, 0,
                new int[]{6}
        ));
        addComponent(new PhysicComponent(
                25, 35, -12, -8,
                100));

        addComponent(new StatsComponent(
                new HashMap<>(Map.of(
                    "hp_max", 10000000f,
                    "mp_max", 100f,
                    "atk", 5f,
                    "def", 10f,
                    "spd", 2f
                )), new HashMap<>(Map.of(
                    "hp", new SimpleFloatProperty(1000000f),
                    "mp", new SimpleFloatProperty(100f)
                ))
        ));

        addComponent(new InventoryComponent(new HashMap<>(Map.of(
                Game.em.getItem("heal_potion"), 10,
                Game.em.getItem("mana_potion"), 10,
                Game.em.getItem("speed_potion"), 5
            ))
        ));

    }
}
