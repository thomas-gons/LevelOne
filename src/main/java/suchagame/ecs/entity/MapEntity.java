package suchagame.ecs.entity;

import suchagame.ecs.component.LayersComponent;
import suchagame.ui.Game;

/**
 * Entity for managing map-related data and actions.
 */
public class MapEntity extends Entity {
    public static final int layersCount = 2;
    public static final int defaultTileSize = 32;

    // max number of tiles that can fit on the screen at once
    public static final int globalTileCountRows = (int) Math.ceil((double) Game.height / defaultTileSize);
    public static final int globalTileCountCols = (int) Math.ceil((double) Game.width / defaultTileSize);

    /**
     * Constructs a MapEntity object with a layers component.
     */
    public MapEntity() {
        this.addComponent(new LayersComponent());
    }

}
