package suchagame.ecs.entity;

import suchagame.ecs.component.LayersComponent;
import suchagame.ui.Game;

import java.util.List;

public class MapEntity extends Entity {
    public static int layersCount;
    public static int defaultTileSize;
    public static int globalTileCountRows;
    public static int globalTileCountCols;
    public MapEntity(int layersCount, int defaultTileSize) {
        super();
        MapEntity.layersCount = layersCount;
        MapEntity.defaultTileSize = defaultTileSize;
        globalTileCountRows = (int) Math.ceil((double) Game.height / defaultTileSize);
        globalTileCountCols = (int) Math.ceil((double) Game.width / defaultTileSize);
    }
    public List<int[][]> getLayers() {
        return (this.getComponent(LayersComponent.class)).getAllLayers();
    }
}
