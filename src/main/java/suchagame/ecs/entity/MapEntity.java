package suchagame.ecs.entity;

import suchagame.ecs.component.LayersComponent;
import suchagame.ui.Game;

import java.util.List;

public class MapEntity extends Entity {
    public static final int layersCount = 2;
    public static final int defaultTileSize = 32;
    public static final int globalTileCountRows = (int) Math.ceil((double) Game.height / defaultTileSize);
    public static final int globalTileCountCols = (int) Math.ceil((double) Game.width / defaultTileSize);
    public MapEntity() {
        this.addComponent(new LayersComponent());
    }
    public List<int[][]> getLayers() {
        return (this.getComponent(LayersComponent.class)).getAllLayers();
    }
}
