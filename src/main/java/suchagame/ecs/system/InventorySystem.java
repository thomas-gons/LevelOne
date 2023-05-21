package suchagame.ecs.system;

import suchagame.ecs.component.InventoryComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Item;
import suchagame.ecs.entity.Mob;
import suchagame.ui.Game;

import java.util.Map;

/**
 * System that handles the inventory.
 */
public class InventorySystem extends System {

    /**
     * Leaves the inventory of the mob to player.
     * @param entity Entity to leave the inventory of.
     */
    public static void leaveInventory(Entity entity) {
        InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
        if (inventoryComponent == null)
            return;
        Map<Item, Integer> entityInventory = inventoryComponent.getInventory();
        Map<Item, Integer> playerInventory = Game.em.getPlayer().getComponent(InventoryComponent.class).getInventory();
        if (entity instanceof Mob) {
            for (Map.Entry<Item, Integer> entry : entityInventory.entrySet()) {
                // merge the inventory of the mob to the player
                playerInventory.merge(entry.getKey(), entry.getValue(), Integer::sum);
                Game.hud.updateSlimeDropAmount();
                Game.hud.updateConsumableItemAmount();
            }
        }
    }

    /**
     * Consumes an item from the inventory of the player.
     * @param item Item to consume.
     */
    public static void consumeItem(Item item) {
        Map<Item, Integer> inventory = Game.em.getPlayer().getComponent(InventoryComponent.class).getInventory();
        // remove one item from the inventory
        inventory.put(item, inventory.get(item) - 1);
    }
}
