package suchagame.ecs.system;

import suchagame.ecs.component.*;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Item;
import suchagame.ecs.entity.Player;
import suchagame.ecs.entity.Projectile;
import suchagame.ui.Game;
import suchagame.utils.Timer;
import suchagame.utils.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for the gameplay logic.
 */
public class GameplaySystem extends System {
    private static Timer cooldownTimer;

    // cool down timer for regenerating some stats (e.g. mana)
    private static Timer regenCooldownTimer;

    /**
     * This method is called when the player pressed the attack button.
     */
    public void castSpell() {
        /*
           check if the player is already casting a spell or if mana regeneration is still paused
           we stop the mana regeneration
        */
        if (cooldownTimer != null && regenCooldownTimer != null) {
            regenCooldownTimer.stop();
            return;
        }
        // get the current hand item spell
        String tag = Game.em.getPlayer().getHandItem(Item.ItemType.SPELL).getTag();
        StatsComponent statsComponent = Game.em.getPlayer().getComponent(StatsComponent.class);

        // check if the player has enough mana to cast the spell
        if (statsComponent.getObservableStat("mp") < Projectile.ProjectileType.getManaCost(tag))
            return;

        AnimationComponent animationComponent = Game.em.getPlayer().getComponent(AnimationComponent.class);

        // get the player's velocity to cast the spell in the same direction
        Vector2f playerVelocity = Game.em.getPlayer().getComponent(PhysicComponent.class).getVelocityDeepCopy();
        Runnable r = () -> {
            Projectile projectile = new Projectile(tag, playerVelocity);

            // remove the mana cost from the player's mana
            statsComponent.alterObservableStat("mp", -projectile.getManaCost());
//            Game.em.addEntity(projectile);

            // reset the player's animation
            animationComponent.setCurrentAction(AnimationComponent.ACTION.IDLE);

            // restart the mana regeneration after 200ms of cool down
            regenCooldownTimer = new Timer(200, () -> StatsSystem.regenTimeline.play());
        };
        // start the attack animation
        AnimationSystem.triggerAction(Game.em.getPlayer(), r, AnimationComponent.ACTION.ATTACK);
        // start the cool down timer for the spell
        cooldownTimer = new Timer(500, () -> cooldownTimer = null);
        StatsSystem.regenTimeline.pause();
    }

    /**
     * Switches the player's hand item of the given type.
     * @param type The type of the item to switch.
     */
    public void switchHandItem(Item.ItemType type) {
        InventoryComponent inventoryComponent = Game.em.getPlayer().getComponent(InventoryComponent.class);

        Item handItem = Game.em.getPlayer().getHandItem(type);

        List<Item> inventoryItems = new ArrayList<>(inventoryComponent.getInventory().keySet());

        // get the next item of the same type
        Item newItem = inventoryItems.stream()
                .filter(item -> item.getType() == type && !item.equals(handItem))
                .findFirst()
                .orElse(inventoryItems.stream().filter(item -> item.getType() == type).findFirst().orElse(handItem));

        setHandItem(newItem);

        // update the hand slot in the HUD
        Game.hud.updateHandSlot(newItem);
    }

    /**
     * Sets the player's hand item of the given type.
     * @param item The item to set.
     */
    public void setHandItem(Item item) {
        Game.em.getPlayer().setHandItem(item);
    }

    /**
     * get the amount of the current consumable item
     * @return the amount of the current consumable item
     */
    public int getAmountOfCurrentConsumable() {
        Player player = Game.em.getPlayer();
        InventoryComponent inventory = player.getComponent(InventoryComponent.class);
        return inventory.getItemAmount(player.getHandItem(Item.ItemType.CONSUMABLE).getTag());
    }

    /**
     * Uses the current consumable item.
     */
    public void useCurrentConsumable() {
        Player player = Game.em.getPlayer();
        InventoryComponent inventory = player.getComponent(InventoryComponent.class);
        Item item = player.getHandItem(Item.ItemType.CONSUMABLE);
        // check if the player has the item in his inventory
        if (inventory.getItemAmount(item.getTag()) > 0) {
            StatsSystem.useConsumable(Game.em.getPlayer(), item);
            InventorySystem.consumeItem(item);
            // update the consumable item amount in the HUD
            Game.hud.updateConsumableItemAmount();
        }
    }

    /**
     * Interacts with the NPC if the player is near it when pressing the interact button (e.g. A)
     */
    public void interactWithNPC() {
        if (isNearNPC()) {
            // display the NPC menu
            Game.npcMenu.toggleNPCMenuView();
        }
    }

    /**
     * Checks if the player is near the NPC.
     * @return true if the player is near the NPC, false otherwise
     */
    public boolean isNearNPC() {
        Vector2f playerPosition = Game.em.getPlayer().getComponent(TransformComponent.class).getPosition();
        Vector2f npcPosition = Game.em.getNPC().getComponent(TransformComponent.class).getPosition();
        return playerPosition.distance(npcPosition) < 100;
    }

    /**
     * Kills the given entity.
     * @param entity The entity to kill.
     * @param shouldDropInventory Whether the entity should drop its inventory or not.
     */
    public void killEntity(Entity entity, boolean shouldDropInventory) {
        Runnable r = () -> {
            Game.em.removeEntity(entity);
            if (entity instanceof Player) {
                // if the player dies, the game is over
                Game.endGame("GAME OVER");
            } else if (Game.em.getEntityCount() == Game.em.getItemsCount() + 3)
                // if the player kills all the enemies, the game is won
                Game.endGame("YOU WIN");
        };
        if (entity.hasComponent(StatsComponent.class)) {
            StatsComponent statsComponent = entity.getComponent(StatsComponent.class);
            // set the hp to 0 useful for the hud to display the correct hp
            statsComponent.setObservableStat("hp", 0);
            statsComponent.isAlive = false;
        }
        // stop the entity's movement and leave its inventory to player if needed
        entity.getComponent(PhysicComponent.class).setVelocity(new Vector2f(0));
        if (shouldDropInventory)
            InventorySystem.leaveInventory(entity);
        // start the death animation
        AnimationSystem.triggerAction(entity, r, AnimationComponent.ACTION.DEATH);
    }
}
