package suchagame.ecs.component;

import suchagame.utils.Vector2f;

import java.util.Arrays;

public class AnimationComponent extends Component {
    private final int framerate;
    private ACTION currentAction;
    private int currentFrame;
    private final int[] framesCountPerRow;

    private final int actionCount;
    private long lastUpdate = java.lang.System.currentTimeMillis();


    public AnimationComponent(
            GraphicComponent graphicComponent, int framerate,
            ACTION initAction, int initFrame,
            int[] framesCountPerRow
    ) {
        this.framerate = framerate;
        this.currentAction = initAction;
        this.currentFrame = (initFrame != -1) ?
                initFrame : (int) (Math.random() * framesCountPerRow[initAction.ordinal()]);

        this.framesCountPerRow = framesCountPerRow;
        this.actionCount = framesCountPerRow.length - 1;
        graphicComponent.setWidth(graphicComponent.getWidth() / Arrays.stream(framesCountPerRow).max().getAsInt());
        graphicComponent.setHeight(graphicComponent.getHeight() / framesCountPerRow.length);
        graphicComponent.setOrigin(new int[]{
                graphicComponent.getWidth() * initFrame,
                graphicComponent.getHeight() * getActionRow(initAction)
        });
    }

    public enum ACTION {
        IDLE,
        ATTACK,
        DEATH,
    }

    public int getFramerate() {
        return framerate;
    }
    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrameToNext() {
        this.currentFrame = (currentFrame + 1) % framesCountPerRow[currentAction.ordinal()];
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getActionRow(ACTION action) {
        return action.ordinal();
    }

    public void setCurrentAction(ACTION currentAction) {
        this.currentAction = (this.actionCount < currentAction.ordinal()) ?
                ACTION.values()[this.actionCount] : currentAction;
    }

    public ACTION getCurrentAction() {
        return currentAction;
    }

    public long getDurationOfAction(ACTION action) {
        return (long) (((float) 1000 / framerate) * (framesCountPerRow[Math.min(this.actionCount, currentAction.ordinal())] - 1));
    }
}
