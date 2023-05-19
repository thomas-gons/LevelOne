package suchagame.ecs.system;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import suchagame.ecs.entity.Item;
import suchagame.ui.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InputSystem extends System {

     public HashMap<KeyCode, Boolean> keyDown = new HashMap<>(Map.of(
                KeyCode.Z, false,
                KeyCode.Q, false,
                KeyCode.S, false,
                KeyCode.D, false
     ));
     public Set<KeyCode> keyPressed = Set.of(
                KeyCode.AMPERSAND, KeyCode.DEAD_ACUTE, KeyCode.QUOTEDBL,
                KeyCode.B, KeyCode.E, KeyCode.A
     );
    public InputSystem(Scene scene) {
        if (scene == null) {
           throw new IllegalArgumentException("Node cannot be null");
        }

        initKeyDownEvent(scene);
        initKeyPressedEvent(scene);
    }

    private void initKeyDownEvent(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
           keyDown.put(event.getCode(), true);
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
           keyDown.put(event.getCode(), false);
        });
        }

    private void initKeyPressedEvent(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
           if (keyPressed.contains(event.getCode())) {
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

    public boolean isKeyDown(KeyCode keyCode) {
        return keyDown.get(keyCode);
    }
}
