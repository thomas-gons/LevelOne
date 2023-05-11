package suchagame.ecs.system;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import suchagame.ecs.component.AnimationComponent;
import suchagame.ecs.component.InputComponent;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.Item;
import suchagame.ecs.entity.Projectile;
import suchagame.ui.Game;
import suchagame.utils.Timer;

public class InputSystem extends System {

    private static Timer cooldownTimer;
    private static Timer regenCooldownTimer;
   public InputSystem(Scene scene) {
       if (scene == null) {
           throw new IllegalArgumentException("Node cannot be null");
       }
       initKeyDownEvent(scene);
       initKeyPressedEvent(scene);
   }

   private void initKeyDownEvent(Scene scene) {
       InputComponent inputComponent = Game.em.getPlayer().getComponent(InputComponent.class);
       scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
           inputComponent.keyDown.put(event.getCode(), true);
       });

       scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
           inputComponent.keyDown.put(event.getCode(), false);
       });
   }

   private void initKeyPressedEvent(Scene scene) {
       InputComponent inputComponent = Game.em.getPlayer().getComponent(InputComponent.class);
       scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
           if (inputComponent.keyPressed.contains(event.getCode())) {
               switch (event.getCode()) {
                   case AMPERSAND  -> {
                       CharacterSystem.switchHandItem(Item.ItemType.SPELL);
                   }
                   case DEAD_ACUTE -> {
                       CharacterSystem.switchHandItem(Item.ItemType.ARTEFACT);
                   }
                   case QUOTEDBL   -> {
                       CharacterSystem.switchHandItem(Item.ItemType.CONSUMABLE);
                   }
                   case E -> {
                       fireProjectile();
                   }
               }
           }
           event.consume();
       });
   }

   private void fireProjectile() {
       if (cooldownTimer != null) {
           regenCooldownTimer.stop();
           return;
       }
       StatsComponent statsComponent = Game.em.getPlayer().getComponent(StatsComponent.class);
       if (statsComponent.getObservableStat("mp") < 10)
             return;

       AnimationComponent animationComponent = Game.em.getPlayer().getComponent(AnimationComponent.class);
       Runnable r = () -> {
           Projectile projectile = new Projectile("fireball");
           statsComponent.alterObservableStat("mp", -projectile.getManaCost());
           Game.em.addEntity(projectile);
           animationComponent.setCurrentAction(AnimationComponent.ACTION.IDLE);
           regenCooldownTimer = new Timer(200, () -> {
               StatsSystem.regenTimeline.play();

           });
       };
       AnimationSystem.triggerAction(Game.em.getPlayer(), r, AnimationComponent.ACTION.ATTACK);
       cooldownTimer = new Timer(500, () -> cooldownTimer = null);
       StatsSystem.regenTimeline.pause();
   }
}
