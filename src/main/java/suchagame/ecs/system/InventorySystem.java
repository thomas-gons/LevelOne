package suchagame.ecs.system;

import suchagame.ecs.component.InventoryComponent;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Item;
import suchagame.ecs.entity.Mob;
import suchagame.ecs.entity.Player;
import suchagame.ui.Game;

import java.util.Map;

public class InventorySystem extends System {
    public static void leaveInventory(Entity entity) {
        InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
        if (inventoryComponent == null)
            return;
        Map<Item, Integer> entityInventory = inventoryComponent.getInventory();
        Map<Item, Integer> playerInventory = Game.em.getPlayer().getComponent(InventoryComponent.class).getInventory();
        if (entity instanceof Mob) {
            for (Map.Entry<Item, Integer> entry : entityInventory.entrySet()) {
                playerInventory.merge(entry.getKey(), entry.getValue(), Integer::sum);
                Game.hud.updateSlimeDropAmount();
            }
        }
    }

    public static void consumeItem(Item item) {
        Map<Item, Integer> inventory = Game.em.getPlayer().getComponent(InventoryComponent.class).getInventory();
        inventory.put(item, inventory.get(item) - 1);
    }
}
