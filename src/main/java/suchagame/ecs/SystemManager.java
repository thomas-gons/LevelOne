package suchagame.ecs;

import java.util.ArrayList;
import java.util.List;

public class SystemManager {
    List<System> systems = new ArrayList<>();

    public SystemManager() {
    }

    public void add(System system) {
        systems.add(system);
    }
    public void addAll(System ... systems) {

        this.systems.addAll(List.of(systems));
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
