package suchagame.ecs.system;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import suchagame.ecs.EntityManager;
//import suchagame.ecs.component.CameraComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.component.InputComponent;
import suchagame.ecs.entity.Player;
import suchagame.ui.Light;

public class InputSystem extends System {
   public InputSystem(Scene scene) {
       if (scene == null) {
           throw new IllegalArgumentException("Node cannot be null");
       }
       for (Entity entity : EntityManager.instance.getAllWithComponent(InputComponent.class)) {
           InputComponent inputComponent = entity.getComponent(InputComponent.class);
           Player player = EntityManager.instance.getPlayer();
           scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
               inputComponent.keyPressed.put(event.getCode(), true);
           });

           scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
               inputComponent.keyPressed.put(event.getCode(), false);
           });
       }
   }
}
