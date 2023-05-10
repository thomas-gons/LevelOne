package suchagame.ecs.system;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import suchagame.ecs.component.AnimationComponent;
import suchagame.ecs.component.InputComponent;
import suchagame.ecs.entity.Projectile;
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
                   case E -> {
                       fireProjectile();
                   }
               }
           }
           event.consume();
       });
   }

   private void fireProjectile() {
       AnimationComponent animationComponent = Game.em.getPlayer().getComponent(AnimationComponent.class);
       Runnable r = () -> {
           animationComponent.setCurrentAction(AnimationComponent.ACTION.IDLE);
           Game.em.addEntity(new Projectile());
       };
       AnimationSystem.triggerAction(Game.em.getPlayer(), r, AnimationComponent.ACTION.ATTACK);
   }
}
