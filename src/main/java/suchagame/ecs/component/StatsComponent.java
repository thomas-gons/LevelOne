package suchagame.ecs.component;

import java.util.Map;

public class StatsComponent extends Component{
    private Map<String, Float> stats;

    public StatsComponent(Map<String, Float> baseStats) {
        this.stats = baseStats;
    }

    public float getStat(String statName) {
        return stats.get(statName);
    }

    public void alterStat(String statName, float value) {
        stats.put(statName, stats.get(statName) + value);
    }

    public void setStat(String statName, float value) {
        stats.put(statName, value);
    }
}