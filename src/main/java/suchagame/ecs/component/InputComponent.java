package suchagame.ecs.component;


import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Set;

/**
 * Component for managing input-related data and actions.
 */
public class InputComponent extends Component {

    // map of keycodes to booleans, where the boolean value represents whether the key is currently down
    public HashMap<KeyCode, Boolean> keyDown;

    // set of keycodes that represent keys that are pressed
    public Set<KeyCode> keyPressed;

    /**
     * Constructs an InputComponent object with the specified keycodes separated into pressed and down keycodes arrays.
     * @param keyCodesDown the keycodes that represent keys that are down
     * @param keyCodesPressed the keycodes that represent keys that are pressed
     */
    public InputComponent(KeyCode[] keyCodesDown, KeyCode[] keyCodesPressed) {
        keyDown = new HashMap<>();
        for (KeyCode keycode : keyCodesDown) {
            keyDown.put(keycode, false);
        }

        keyPressed = Set.of(keyCodesPressed);
    }
}
