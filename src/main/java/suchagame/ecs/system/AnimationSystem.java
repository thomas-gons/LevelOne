package suchagame.ecs.system;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import suchagame.ecs.component.AnimationComponent;
import suchagame.ecs.component.GraphicComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ui.Game;


public class AnimationSystem extends System {
    public static void update() {
        for (Entity entity : Game.em.getAllWithComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            GraphicComponent graphicComponent = entity.getComponent(GraphicComponent.class);
            long now = java.lang.System.currentTimeMillis();
            if (now - animationComponent.getLastUpdate() > (1000 / animationComponent.getFramerate())) {
                animationComponent.setCurrentFrameToNext();
                graphicComponent.getOrigin()[0] = graphicComponent.getWidth() * animationComponent.getCurrentFrame();
                graphicComponent.getOrigin()[1] = graphicComponent.getHeight() * animationComponent.getCurrentAction().ordinal();
                animationComponent.setLastUpdate(now);
            }
        }
    }

    public static void triggerAction(Entity entity, Runnable callback, AnimationComponent.ACTION action) {
        AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
        long duration = animationComponent.getDurationOfAction(action);
        entity.getComponent(AnimationComponent.class).setCurrentAction(action);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration), event -> {
            if (callback != null) {
                callback.run();
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

}
