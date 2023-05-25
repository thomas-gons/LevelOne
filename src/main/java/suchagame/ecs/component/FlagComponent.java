package suchagame.ecs.component;

import java.util.HashMap;
import java.util.Map;

public class FlagComponent extends Component {
    private Map<String, Boolean> flags = new HashMap<>();

    public FlagComponent(Map<String, Boolean> flags) {
        super();
        this.flags = flags;
    }

    public Map<String, Boolean> getFlags() {
        return flags;
    }

    public void setFlags(boolean value) {
        for (Map.Entry<String, Boolean> entry : flags.entrySet()) {
            entry.setValue(value);
        }
    }

    public boolean getFlag(String flag) {
        return flags.getOrDefault(flag, false);
    }

    public void setFlag(String flag, boolean value) {
        flags.put(flag, value);
    }
}
