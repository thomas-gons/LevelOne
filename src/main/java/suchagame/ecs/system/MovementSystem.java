package suchagame.ecs.system;

import javafx.scene.input.KeyCode;
import suchagame.ecs.EntityManager;
import suchagame.ecs.component.*;
import suchagame.ecs.entity.*;
import suchagame.ui.Game;
import suchagame.utils.Vector2;

public class MovementSystem extends System {
    public static void update() {
        for (Entity entity : EntityManager.instance.getAllWithComponent(TransformComponent.class)) {
            if (entity instanceof Mob) {
                updateMob((Mob) entity);
            } else {
                updatePlayer((Player) entity);
            }
        }
    }

    public static void updatePlayer(Player player) {
        TransformComponent transformComponent = player.getComponent(TransformComponent.class);
        InputComponent inputComponent = player.getComponent(InputComponent.class);
        float speed = player.getComponent(StatsComponent.class).getStat("spd");

        float dx = 0, dy = 0;
        if (inputComponent.keyPressed.get(KeyCode.Z)) dy -= 1;
        if (inputComponent.keyPressed.get(KeyCode.D)) dx += 1;
        if (inputComponent.keyPressed.get(KeyCode.S)) dy += 1;
        if (inputComponent.keyPressed.get(KeyCode.Q)) dx -= 1;

        if (dx != 0 && dy != 0) {
            dx /= 1.4142;
            dy /= 1.4142;
        }

        Vector2<Float> checkPosition = transformComponent.getPosition();
        checkPosition.setX(checkPosition.getX() + dx * speed);
        checkPosition.setY(checkPosition.getY() + dy * speed);

        if (!PhysicSystem.checkCollision(player, checkPosition))
            (player.getComponent(TransformComponent.class)).setPosition(checkPosition);
    }

    public static void updateMob(Mob mob) {
    }
}
        // RTA*
//        Vector2 start = ((PositionComponent) mob.getComponent(PositionComponent.class)).getPosition();
//        Vector2 goal = ((PositionComponent) EntityManager.instance.getPlayer().getComponent(PositionComponent.class)).getPosition();
//        int[][] heuristic = updateHeuristic(start, goal);
//        boolean[][] closed = new boolean[MapEntity.rowsTilesCount][MapEntity.colsTilesCount];
//        int[][] action = new int[MapEntity.rowsTilesCount][MapEntity.colsTilesCount];

//    }

//    private static List<Node> updateHeuristic(Node start, Node goal) {
//        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
//        Set<Node> closedSet = new HashSet<>();
//        Map<Node, Node> cameFrom = new HashMap<>();
//        Map<Node, Integer> gScore = new HashMap<>();
//        gScore.put(start, 0);
//        start.setF(start.getManhattanDistance(goal));
//
//        openSet.offer(start);
//
//        while (!openSet.isEmpty()) {
//            Node current = openSet.poll();
//
//            if (current.equals(goal)) {
//                return reconstructPath(cameFrom, current);
//            }
//
//            closedSet.add(current);
//
//            for (Node neighbor : getNeighbors(current)) {
//                if (closedSet.contains(neighbor)) {
//                    continue;
//                }
//
//                int tentativeGScore = gScore.get(current) + 1;
//                if (!openSet.contains(neighbor)) {
//                    gScore.put(neighbor, tentativeGScore);
//                    neighbor.setF(tentativeGScore + neighbor.getManhattanDistance(goal));
//                    openSet.offer(neighbor);
//                } else if (tentativeGScore < gScore.get(neighbor)) {
//                    gScore.put(neighbor, tentativeGScore);
//                    neighbor.setF(tentativeGScore + neighbor.getManhattanDistance(goal));
//                    openSet.remove(neighbor);
//                    openSet.offer(neighbor);
//                }
//            }
//      }
//      return null; // Pas de chemin trouvé
//    }
//
//    private static List<Node> getNeighbors(Node node) {
//        List<Node> neighbors = new ArrayList<>();
//        int x = node.getPosition().x;
//        int y = node.getPosition().y;
//
//        if (x > 0) {
//            Node neighbor = getNode(x - 1, y);
//            if (!neighbor.isObstacle()) {
//                neighbors.add(neighbor);
//            }
//        }
//
//        if (y > 0) {
//            Node neighbor = getNode(x, y - 1);
//            if (!neighbor.isObstacle()) {
//                neighbors.add(neighbor);
//            }
//        }
//
//        if (x < Map.WIDTH - 1) {
//            Node neighbor = getNode(x + 1, y);
//            if (!neighbor.isObstacle()) {
//                neighbors.add(neighbor);
//            }
//        }
//
//        if (y < Map.HEIGHT - 1) {
//            Node neighbor = getNode(x, y + 1);
//            if (!neighbor.isObstacle()) {
//                neighbors.add(neighbor);
//            }
//        }
//
//        return neighbors;
//}

//    private static List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
//        List<Node> path = new ArrayList<>();
//        path.add(current);
//        while (cameFrom.containsKey(current)) {
//            current = cameFrom.get(current);
//            path.add(0, current);
//        }
//        return path;
//    }
//}
