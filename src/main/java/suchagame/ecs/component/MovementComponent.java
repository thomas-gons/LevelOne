package suchagame.ecs.component;

import suchagame.ecs.entity.MapEntity;

public class MovementComponent extends Component {
    private float velocity;
//    private static int[][] heuristic = new int[MapEntity.rowsTilesCount][MapEntity.colsTilesCount];

    public MovementComponent(float velocity) {
        this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

//    public static int[][] getHeuristic() {
//        return heuristic;
//    }
}
