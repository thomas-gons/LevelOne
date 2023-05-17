package suchagame.ecs.system;

import suchagame.ecs.component.*;
import suchagame.ecs.entity.Item;
import suchagame.ecs.entity.Projectile;
import suchagame.ui.Game;
import suchagame.utils.Timer;
import suchagame.utils.Vector2f;

public class GameplaySystem extends System {
    private static Timer cooldownTimer;
    private static Timer regenCooldownTimer;
    public void fireProjectile() {
       if (cooldownTimer != null && regenCooldownTimer != null) {
           regenCooldownTimer.stop();
           return;
       }
       String tag = Game.em.getPlayer().getComponent(GameplayComponent.class).getHandItem(Item.ItemType.SPELL).getTag();
       StatsComponent statsComponent = Game.em.getPlayer().getComponent(StatsComponent.class);
       if (statsComponent.getObservableStat("mp") < ((Projectile) Game.em.getLastEntity()).getManaCost())
             return;

       AnimationComponent animationComponent = Game.em.getPlayer().getComponent(AnimationComponent.class);
       Runnable r = () -> {
           Game.em.addEntity(Projectile.class, tag);
           int manaCost = ((Projectile) Game.em.getLastEntity()).getManaCost();
           statsComponent.alterObservableStat("mp", -manaCost);
           animationComponent.setCurrentAction(AnimationComponent.ACTION.IDLE);
           regenCooldownTimer = new Timer(200, () -> {
               StatsSystem.regenTimeline.play();

           });
       };
       AnimationSystem.triggerAction(Game.em.getPlayer(), r, AnimationComponent.ACTION.ATTACK);
       cooldownTimer = new Timer(500, () -> cooldownTimer = null);
       StatsSystem.regenTimeline.pause();
   }
    public void switchHandItem(Item.ItemType type) {
        GameplayComponent characterComponent = Game.em.getPlayer().getComponent(GameplayComponent.class);
        InventoryComponent inventoryComponent = Game.em.getPlayer().getComponent(InventoryComponent.class);

        Item handItem = characterComponent.getHandItems().get(type);
        Item newItem = handItem;
        for (Item item : inventoryComponent.getInventory().keySet()) {
            if (item.getType() == type && !item.equals(handItem)) {
                newItem = item;
                characterComponent.getHandItems().put(type, item);
                break;
            }
        }
        Game.hud.updateHandSlot(newItem);
    }

    public int getAmountOfCurrentConsumable() {
        InventoryComponent inventory = Game.em.getPlayer().getComponent(InventoryComponent.class);
        GameplayComponent character = Game.em.getPlayer().getComponent(GameplayComponent.class);
        return inventory.getItemAmount(character.getHandItem(Item.ItemType.CONSUMABLE).getTag());
    }

    public void useCurrentConsumable() {
        InventoryComponent inventory = Game.em.getPlayer().getComponent(InventoryComponent.class);
        GameplayComponent character = Game.em.getPlayer().getComponent(GameplayComponent.class);
        Item item = character.getHandItem(Item.ItemType.CONSUMABLE);
        if (inventory.getItemAmount(item.getTag()) > 0) {
            StatsSystem.useConsumable(Game.em.getPlayer(), item);
            InventorySystem.consumeItem(item);
            Game.hud.updateConsumableItemAmount();
        }
    }

    public void interactWithNPC() {
        Vector2f playerPosition = Game.em.getPlayer().getComponent(TransformComponent.class).getPosition();
        Vector2f npcPosition = Game.em.getNPC().getComponent(TransformComponent.class).getPosition();
        if (playerPosition.distance(npcPosition) < 75) {
            Game.npcMenu.toggleNPCMenuView();
        }
    }
}
