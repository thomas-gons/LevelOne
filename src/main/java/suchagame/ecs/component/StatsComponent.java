package suchagame.ecs.component;

import com.fasterxml.jackson.annotation.JsonCreator;
import javafx.beans.property.SimpleFloatProperty;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Projectile;
import suchagame.ui.Game;

import java.util.HashMap;
import java.util.Map;

public class StatsComponent extends Component {

    public boolean isAlive = true;
    private Map<String, Float> stats;
    private Map<String, SimpleFloatProperty> observableStats;
    @JsonCreator
    public StatsComponent(Map<String, Float> baseStats,
                          Map<String, Float> baseObservableStats) {

        this.stats = baseStats;
        this.observableStats = new HashMap<>();
        if (baseObservableStats != null) {
            for (Map.Entry<String, Float> entry : baseObservableStats.entrySet()) {
                observableStats.put(entry.getKey(), new SimpleFloatProperty(entry.getValue()));
            }
        }
    }

    public Map<String, Float> getStats() {
        return stats;
    }

    public float getStat(String statName) throws NullPointerException {
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
        try {
            observableStats.get(statName).setValue(
                    Math.min(getObservableStat(statName) + value, getStat(statName + "_max"))
            );
        } catch (NullPointerException e) {
            observableStats.get(statName).setValue(getObservableStat(statName) + value);
        }
    }

    public void setObservableStat(String statName, float value) {
        observableStats.get(statName).setValue(value);
    }

    public void alterUnknownStat(String statName, float value) {
        if (stats.containsKey(statName))
            alterStat(statName, value);
        else if (observableStats.containsKey(statName))
            alterObservableStat(statName, value);
    }

}
