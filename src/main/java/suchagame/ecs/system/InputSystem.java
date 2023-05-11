package suchagame.ecs.system;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import suchagame.ecs.component.*;
import suchagame.ecs.entity.Item;
import suchagame.ui.Game;

public class InputSystem extends System {


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
                       Game.sm.get(GameplaySystem.class).switchHandItem(Item.ItemType.ARTEFACT);
                   }
                   case DEAD_ACUTE -> {
                       Game.sm.get(GameplaySystem.class).switchHandItem(Item.ItemType.SPELL);
                   }
                   case QUOTEDBL  -> {
                       Game.sm.get(GameplaySystem.class).switchHandItem(Item.ItemType.CONSUMABLE);
                   }
                   case B -> {
                       Game.sm.get(GameplaySystem.class).useCurrentConsumable();
                   }
                   case E -> {
                       Game.sm.get(GameplaySystem.class).fireProjectile();
                   }
                   case A -> {
                       Game.sm.get(GameplaySystem.class).interactWithNPC();
                   }
               }
           }
           event.consume();
       });
   }
}
