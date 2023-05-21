package suchagame.ecs;

import suchagame.ecs.system.System;
import suchagame.ecs.system.*;
import suchagame.ui.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages the systems.
 */
public class SystemManager {
    public List<System> systems = new ArrayList<>();

    /**
     * Constructor that adds all the systems to the list.
     */
    public SystemManager() {
        this.systems.addAll(List.of(
                new InputSystem(Game.scene),
                new StatsSystem(),
                new GameplaySystem()
        ));
    }

    /**
     * Updates all the systems that need to be updated.
     */
    public void update() {
       MovementSystem.update();
       AnimationSystem.update();
       GraphicSystem.render(Game.gc);
    }

    /**
     * Returns the system of the specified class.
     *
     * @param systemClass the class of the system to return
     * @param <T>         the type of the system to return
     * @return the system of the specified class
     */
    public <T extends System> T get(Class<T> systemClass) {
        for (System system : systems) {
            if (systemClass.isInstance(system)) {
                return systemClass.cast(system);
            }
        }
        return null;
    }

    /**
     * Removes all the systems from the list and eventual listeners.
     */
    public void removeAllSystems() {
        InputSystem inputSystem = get(InputSystem.class);
        inputSystem.removeListeners();
        systems.clear();
    }
}
