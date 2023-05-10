package suchagame.ecs.component;


import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Set;

public class InputComponent extends Component {
    public HashMap<KeyCode, Boolean> keyDown;
    public Set<KeyCode> keyPressed;
    public InputComponent(KeyCode[] keyCodesDown, KeyCode[] keyCodesPressed) {
        keyDown = new HashMap<>();
        for (KeyCode keycode : keyCodesDown) {
            keyDown.put(keycode, false);
        }

        keyPressed = Set.of(keyCodesPressed);
    }
}
