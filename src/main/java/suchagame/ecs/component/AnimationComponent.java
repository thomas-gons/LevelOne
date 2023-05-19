package suchagame.ecs.component;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public class AnimationComponent extends Component {
    private final int framerate;
    private ACTION currentAction;
    private int currentFrame;
    private final int[] framesCountPerRow;

    private final int actionCount;
    private long lastUpdate = java.lang.System.currentTimeMillis();

    private AnimationComponent(
            GraphicComponent graphicComponent, int framerate,
            int[] framesCountPerRow)
    {
        this.framerate = framerate;
        this.framesCountPerRow = framesCountPerRow;
        this.actionCount = framesCountPerRow.length - 1;
        graphicComponent.setWidth(graphicComponent.getWidth() / Arrays.stream(framesCountPerRow).max().getAsInt());
        graphicComponent.setHeight(graphicComponent.getHeight() / framesCountPerRow.length);
    }
    @JsonCreator
    public AnimationComponent(
            GraphicComponent graphicComponent, int framerate,
            ACTION initAction, int initFrame,
            int[] framesCountPerRow
    ) {
        this(graphicComponent, framerate, framesCountPerRow);
        this.currentAction = initAction;
        this.currentFrame = initFrame;

        graphicComponent.setOrigin(new int[]{
                graphicComponent.getWidth() * initFrame,
                graphicComponent.getHeight() * getActionRow(initAction)
        });
    }
    @JsonCreator
    public AnimationComponent(
            GraphicComponent graphicComponent, int framerate,
            String specialAction, String specialFrame,
            int[] framesCountPerRow)
    {
        this(graphicComponent, framerate, framesCountPerRow);

        if (specialAction.equals("random")) {
            this.currentAction = ACTION.values()[(int) (Math.random() * framesCountPerRow.length)];
        }
        if (specialFrame.equals("random")) {
            assert currentAction != null;
            this.currentFrame = (int) (Math.random() * framesCountPerRow[currentAction.ordinal()]);
        }

        graphicComponent.setOrigin(new int[]{
                graphicComponent.getWidth() * currentFrame,
                graphicComponent.getHeight() * getActionRow(currentAction)
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

        this.currentFrame = 0;
    }

    public ACTION getCurrentAction() {
        return currentAction;
    }

    public long getDurationOfAction(ACTION action) {
        return (long) ((1000f / framerate) * (framesCountPerRow[Math.min(this.actionCount, action.ordinal())] - 1));
    }
}
