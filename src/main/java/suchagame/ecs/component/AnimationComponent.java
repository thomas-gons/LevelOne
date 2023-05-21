package suchagame.ecs.component;


import java.util.Arrays;

/**
 * Component for managing animations of a graphical object using sprite sheet.
 */
public class AnimationComponent extends Component {
    private final int framerate;
    private ACTION currentAction;
    private int currentFrame;
    private final int[] framesCountPerRow;
    private final int actionCount;
    private long lastUpdate = java.lang.System.currentTimeMillis();

    /**
     * Constructs an AnimationComponent object.
     *
     * @param graphicComponent     the associated graphic component for the animation
     * @param framerate            the frame rate of the animations in frames per second
     * @param initAction           the initial action of the animation
     * @param initFrame            the initial frame of the animation in the sprite sheet
     * @param framesCountPerRow    an array specifying the number of frames per row for each action
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
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
        // max frame count per row is used because some actions have more frames than others

        graphicComponent.setWidth(graphicComponent.getWidth() / Arrays.stream(framesCountPerRow).max().getAsInt());
        graphicComponent.setHeight(graphicComponent.getHeight() / framesCountPerRow.length);
        // initialize the origin of the graphic component based on the initial frame and action
        graphicComponent.setOrigin(new int[]{
                graphicComponent.getWidth() * initFrame,
                graphicComponent.getHeight() * getActionRow(initAction)
        });
    }

    /**
     * Enum representing the possible actions of the animation.
     */
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

    /**
     * Sets the current frame to the next frame.
     */
    public void setCurrentFrameToNext() {
        this.currentFrame = (currentFrame + 1) % framesCountPerRow[currentAction.ordinal()];
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Returns the row of the sprite sheet corresponding to the given action.
     *
     * @param action the action
     * @return the row of the sprite sheet corresponding to the given action
     */
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

    /**
     * Returns the duration of the given action in milliseconds.
     * @param action the action
     * @return the duration of the given action in milliseconds
     */
    public long getDurationOfAction(ACTION action) {
        return (long) ((1000f / framerate) * (framesCountPerRow[Math.min(this.actionCount, action.ordinal())] - 1));
    }
}
