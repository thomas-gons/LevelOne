package suchagame.ecs.system;

import suchagame.ecs.EntityManager;
import suchagame.ecs.component.AnimationComponent;
import suchagame.ecs.component.AnimationComponent.ACTION;
import suchagame.ecs.component.GraphicComponent;
import suchagame.ecs.entity.Entity;

public class AnimationSystem extends System {
    public static void update() {
        for (Entity entity : EntityManager.instance.getAllWithComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            GraphicComponent graphicComponent = entity.getComponent(GraphicComponent.class);
            long now = java.lang.System.currentTimeMillis();
            if (now - animationComponent.getLastUpdate() > (1000 / animationComponent.getFramerate())) {
                animationComponent.setCurrentFrameToNext();
                graphicComponent.getOrigin().setX(graphicComponent.getWidth() * animationComponent.getCurrentFrame());
                animationComponent.setLastUpdate(now);
            }
        }
    }
}
