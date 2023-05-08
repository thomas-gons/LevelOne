package suchagame.ecs.component;

import suchagame.utils.Vector2;

import java.util.Arrays;

public class AnimationComponent extends Component {
    private final int framerate;
    private ACTION currentAction;
    private int currentFrame;
    private final int[] framesCountPerRow;
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
        graphicComponent.setWidth(graphicComponent.getWidth() / Arrays.stream(framesCountPerRow).max().getAsInt());
        graphicComponent.setHeight(graphicComponent.getHeight() / framesCountPerRow.length);
        graphicComponent.setOrigin(new Vector2<>(
                graphicComponent.getWidth() * initFrame,
                graphicComponent.getHeight() * getActionRow(initAction))
        );
    }

    public enum ACTION {
        IDLE,
        ATTACK,
        DEAD,
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

}
