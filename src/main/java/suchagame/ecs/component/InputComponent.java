package suchagame.ecs.component;


import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class InputComponent extends Component {
    public HashMap<KeyCode, Boolean> keyPressed;
    public InputComponent(KeyCode[] keycodes) {
        keyPressed = new HashMap<>();
        for (KeyCode keycode : keycodes) {
            keyPressed.put(keycode, false);
        }
    }
}
