package suchagame.ecs.system;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import suchagame.ecs.component.InputComponent;
import suchagame.ecs.entity.Item;
import suchagame.ui.Game;

/**
 * This class is responsible for handling user input.
 */
public class InputSystem extends System {

    /**
     * Constructs a new InputSystem.
     *
     * @param scene the scene to listen to
     */
    public InputSystem(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }
        initKeyDownEvent(scene);
        initKeyPressedEvent(scene);
    }

    /**
     * Initializes the key down event.
     *
     * @param scene the scene to listen to
     */
    private void initKeyDownEvent(Scene scene) {
        InputComponent inputComponent = Game.em.getPlayer().getComponent(InputComponent.class);
        // toggle corresponding value to simulate key down
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> inputComponent.keyDown.put(event.getCode(), true));

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> inputComponent.keyDown.put(event.getCode(), false));
    }

    /**
     * Initializes the key pressed event.
     *
     * @param scene the scene to listen to
     */
    private void initKeyPressedEvent(Scene scene) {
        InputComponent inputComponent = Game.em.getPlayer().getComponent(InputComponent.class);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (inputComponent.keyPressed.contains(event.getCode())) {
                switch (event.getCode()) {
                    // switch hand artefact
                    case AMPERSAND -> Game.sm.get(GameplaySystem.class).switchHandItem(Item.ItemType.ARTEFACT);

                    // switch hand spell
                    case DEAD_ACUTE -> Game.sm.get(GameplaySystem.class).switchHandItem(Item.ItemType.SPELL);

                    // switch hand consumable
                    case QUOTEDBL -> Game.sm.get(GameplaySystem.class).switchHandItem(Item.ItemType.CONSUMABLE);

                    // use current consumable
                    case B -> Game.sm.get(GameplaySystem.class).useCurrentConsumable();

                    // cast spell
                    case E -> Game.sm.get(GameplaySystem.class).castSpell();

                    // interact with NPC
                    case A -> Game.sm.get(GameplaySystem.class).interactWithNPC();
                }
            }
            // toggle hit boxes rendering of all entities
            if (event.getCode() == KeyCode.F4) {
                GraphicSystem.renderHitBoxes = !GraphicSystem.renderHitBoxes;
            }
            event.consume();
        });
    }

    /**
     * Remove all listeners
     */
    public void removeListeners() {
        Game.scene.removeEventHandler(KeyEvent.KEY_PRESSED, event -> {
        });
        Game.scene.removeEventHandler(KeyEvent.KEY_RELEASED, event -> {
        });
    }
}
