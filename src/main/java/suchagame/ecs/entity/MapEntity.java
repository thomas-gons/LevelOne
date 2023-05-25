package suchagame.ecs.entity;

import suchagame.ecs.component.LayersComponent;
import suchagame.ui.Game;

/**
 * Entity for managing map-related data and actions.
 */
public class MapEntity extends Entity {
    public static int layersCount;
    public static int defaultTileSize;

    // max number of tiles that can fit on the screen at once
    public static int globalTileCountRows;
    public static int globalTileCountCols;

    /**
     * Constructs a MapEntity object with a layers component.
     */
    public MapEntity(int layersCount, int defaultTileSize) {
        super();
        MapEntity.layersCount = layersCount;
        MapEntity.defaultTileSize = defaultTileSize;
        globalTileCountRows = (int) Math.ceil((double) Game.height / defaultTileSize);
        globalTileCountCols = (int) Math.ceil((double) Game.width / defaultTileSize);
    }

}
