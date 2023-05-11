package suchagame.ecs.component;

import javafx.beans.property.SimpleFloatProperty;
import java.util.Map;

public class StatsComponent extends Component {

    public boolean isAlive = true;
    private Map<String, Float> stats;
    private Map<String, SimpleFloatProperty> observableStats;
    public StatsComponent(Map<String, Float> baseStats,
                          Map<String, SimpleFloatProperty> baseObservableStats) {

        this.stats = baseStats;
        this.observableStats = baseObservableStats;
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
