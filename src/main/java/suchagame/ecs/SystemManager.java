package suchagame.ecs;

import suchagame.ecs.system.*;
import suchagame.ecs.system.System;
import suchagame.ui.Game;

import java.util.ArrayList;
import java.util.List;

public class SystemManager {
    public List<System> systems = new ArrayList<>();

    public SystemManager() {
        this.systems.addAll(List.of(
                new InputSystem(Game.scene),
                new StatsSystem(),
                new GameplaySystem()
        ));
    }

    public void update() {
       MovementSystem.update();
       AnimationSystem.update();
       GraphicSystem.render(Game.gc);
    }

    public <T extends System> T get(Class<T> systemClass) {
        for (System system : systems) {
            if (systemClass.isInstance(system)) {
                return systemClass.cast(system);
            }
        }
        return null;
    }
}
