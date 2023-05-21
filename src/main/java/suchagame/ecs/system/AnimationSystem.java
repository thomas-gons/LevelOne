package suchagame.ecs.system;

import suchagame.ecs.component.AnimationComponent;
import suchagame.ecs.component.GraphicComponent;
import suchagame.ecs.entity.Entity;
import suchagame.ui.Game;
import suchagame.utils.Timer;

/**
 * System that handles animations.
 */
public class AnimationSystem extends System {

    /**
     * Updates the animation of all entities that have an animation component.
     */
    public static void update() {
        for (Entity entity : Game.em.getAllWithComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            GraphicComponent graphicComponent = entity.getComponent(GraphicComponent.class);
            long now = java.lang.System.currentTimeMillis();
            // If the time elapsed since the last update is greater than the time between two frames, we update the frame.
            if (now - animationComponent.getLastUpdate() > (1000 / animationComponent.getFramerate())) {
                animationComponent.setCurrentFrameToNext();
                // We update the origin of the graphic component to match the new frame.
                graphicComponent.getOrigin()[0] = graphicComponent.getWidth() * animationComponent.getCurrentFrame();
                graphicComponent.getOrigin()[1] = graphicComponent.getHeight() * animationComponent.getCurrentAction().ordinal();
                animationComponent.setLastUpdate(now);
            }
        }
    }

    /**
     * Triggers an action on an entity.
     * @param entity The entity on which to trigger the action.
     * @param callback The callback to run after the action is finished.
     * @param action The action to trigger.
     */
    public static void triggerAction(Entity entity, Runnable callback, AnimationComponent.ACTION action) {
        AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
        long duration = animationComponent.getDurationOfAction(action);
        animationComponent.setCurrentAction(action);
        new Timer(duration, callback);
    }
}
