package suchagame.ecs.system;

import suchagame.ecs.EntityManager;
import suchagame.ecs.component.AnimationComponent;
import suchagame.ecs.component.GraphicComponent;
import suchagame.ecs.entity.Entity;

public class AnimationSystem extends System {
    private static long lastUpdate = java.lang.System.currentTimeMillis();

    public static void update() {
        for (Entity entity : EntityManager.instance.getAllWithComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            GraphicComponent graphicComponent = entity.getComponent(GraphicComponent.class);
            long now = java.lang.System.currentTimeMillis();
            if (now - lastUpdate > (1000 / animationComponent.getFramerate())) {
                animationComponent.setCurrentFrame((animationComponent.getCurrentFrame() + 1) % animationComponent.getFrameCount());
                graphicComponent.getPosition().setX(graphicComponent.getWidth() * animationComponent.getCurrentFrame());
                lastUpdate = now;
            }
        }
    }
}
