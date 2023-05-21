package suchagame.ecs.component;

import javafx.beans.property.SimpleFloatProperty;

import java.util.Map;

/**
 * Component for managing stats-related data and actions.
 */
public class StatsComponent extends Component {

    public boolean isAlive = true;
    private final Map<String, Float> stats;

    // observable stats are stats that can be observed continuously by the systems using simple float properties
    private final Map<String, SimpleFloatProperty> observableStats;

    /**
     * Constructs a StatsComponent object with the specified stats.
     *
     * @param baseStats the initial stats to set for the component
     * @param baseObservableStats the initial observable stats to set for the component
     */
    public StatsComponent(Map<String, Float> baseStats,
                          Map<String, SimpleFloatProperty> baseObservableStats) {

        this.stats = baseStats;
        this.observableStats = baseObservableStats;
    }

    public Map<String, Float> getStats() {
        return stats;
    }

    /**
     * Returns the value of the specified stat.
     *
     * @param statName the name of the stat to get
     * @return the value of the specified stat
     */
    public float getStat(String statName) {
        return stats.get(statName);
    }

    /**
     * Sets the value of the specified stat.
     *
     * @param statName the name of the stat to set
     * @param value the value to set the stat to
     */
    public void alterStat(String statName, float value) {
        stats.put(statName, stats.get(statName) + value);
    }

    public Map<String, SimpleFloatProperty> getObservableStats() {
        return observableStats;
    }

    /**
     * Returns the value of the specified observable stat.
     * @param statName the name of the observable stat to get
     * @return the value of the specified observable stat
     */
    public float getObservableStat(String statName) {
        return observableStats.get(statName).getValue();
    }

    /**
     * Alters the value of the specified observable stat.
     * @param statName the name of the observable stat to alter
     * @param value the value to alter the observable stat by
     */
    public void alterObservableStat(String statName, float value) {
        try {
            observableStats.get(statName).setValue(
                    Math.min(getObservableStat(statName) + value, getStat(statName + "_max"))
            );
        } catch (NullPointerException e) {
            observableStats.get(statName).setValue(getObservableStat(statName) + value);
        }
    }

    /**
     * Sets the value of the specified observable stat.
     * @param statName the name of the observable stat to set
     * @param value the value to set the observable stat to
     */
    public void setObservableStat(String statName, float value) {
        observableStats.get(statName).setValue(value);
    }

    /**
     * More general version of alterStat() that can be used for both stats and observable stats.
     * @param statName the name of the stat to alter
     * @param value the value to alter the stat by
     */
    public void alterUnknownStat(String statName, float value) {
        if (stats.containsKey(statName))
            alterStat(statName, value);
        else if (observableStats.containsKey(statName))
            alterObservableStat(statName, value);
    }

}
