package suchagame.ecs.component;

import suchagame.utils.Vector2;

public class AnimationComponent extends Component {
    public static final Class<? extends Component> dependency = GraphicComponent.class;
    private int currentFrame;
    private final int frameCount;
    private final int framerate;

    public AnimationComponent(GraphicComponent graphicComponent, int frameCount, int spriteSheetRow,
                              int initFrame, int framerate) {

        this.currentFrame = initFrame;
        this.frameCount = frameCount;
        this.framerate = framerate;
        graphicComponent.setWidth(graphicComponent.getWidth() / this.frameCount);
        graphicComponent.setHeight(graphicComponent.getHeight() / spriteSheetRow);
        graphicComponent.setPosition(new Vector2<>(
                graphicComponent.getWidth() * initFrame,
                graphicComponent.getHeight() * (spriteSheetRow - 1))
        );
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int frame) {
        this.currentFrame = frame;
    }

    public int getFramerate() {
        return framerate;
    }

    public int getFrameCount() {
        return frameCount;
    }
}
