package suchagame.ecs.system;


import suchagame.ecs.component.FlagComponent;
import suchagame.ui.Game;

import java.util.Map;

public class FlagSystem extends System {
    public static void resetFlagsState(Map<String, Boolean> previousFlagsState) {
        Map<String, Boolean> playerFlags = Game.em.getPlayer().getComponent(FlagComponent.class).getFlags();
        playerFlags.putAll(previousFlagsState);
    }
}
