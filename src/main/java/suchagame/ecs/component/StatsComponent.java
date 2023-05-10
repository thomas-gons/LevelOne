package suchagame.ecs.component;

import javafx.beans.property.SimpleFloatProperty;
import java.util.HashMap;
import java.util.Map;

public class StatsComponent extends Component {

    private Map<String, Float> stats;
    private Map<String, SimpleFloatProperty> observableStats;
    public StatsComponent(Map<String, Float> baseStats,
                          Map<String, SimpleFloatProperty> baseObservableStats) {

        this.stats = baseStats;
        this.observableStats = baseObservableStats;
    }

    public float getStat(String statName) {
        return stats.get(statName);
    }

    public void alterStat(String statName, float value) {
        stats.put(statName, stats.get(statName) + value);
    }

    public Map<String, SimpleFloatProperty> getObservableStats() {
        return observableStats;
    }

    public float getObservableStat(String statName) {
        return observableStats.get(statName).getValue();
    }
    public void alterObservableStat(String statName, float value) {
        observableStats.get(statName).setValue(observableStats.get(statName).getValue() + value);
    }

    public void setObservableStat(String statName, float value) {
        observableStats.get(statName).setValue(value);
    }
}
