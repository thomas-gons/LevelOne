//package suchagame.ui;
//
//import javafx.scene.image.Image;
//import javafx.scene.image.PixelReader;
//import javafx.scene.image.WritableImage;
//import javafx.scene.layout.*;
//import suchagame.ecs.component.LayersComponent;
//import suchagame.ecs.entity.MapEntity;
//import suchagame.utils.Utils;
//
//import java.util.HashMap;
//
//public class MapUI {
//    private final int tileSize = 32;
//    public MapUI(MapEntity mapEntity) {
//        initializeMapGroup(mapEntity);
//    }
//
//    private void initializeMapGroup(MapEntity mapEntity) {
//        for (int layerID = 0; layerID < MapEntity.layersCount; layerID++) {
//            HashMap<Integer, Background> backgroundTiles = new HashMap<>();
//            int[][] rawLayer = ((LayersComponent) mapEntity.getComponent(LayersComponent.class)).getLayer(layerID);
//            TilePane layer = (TilePane) Game.root.lookup("#map_layer_" + (layerID + 1));
//            Image tileSet = new Image(Utils.getPathResource(Game.class, "images/map_layer_" + (layerID + 1) + ".png"));
//            for (int[] row : rawLayer) {
//                for (int tileId : row) {
//                    if (!backgroundTiles.containsKey(tileId)) {
//                        backgroundTiles.put(tileId, this.createBackgroundTile(tileId, tileSet,
//                                (int) tileSet.getHeight() / tileSize,
//                                (int) tileSet.getWidth() / tileSize));
//                    }
//                    Pane tile = new Pane();
//                    tile.setPrefSize(tileSize, tileSize);
//                    tile.setBackground(backgroundTiles.get(tileId));
//                    tile.setSnapToPixel(true);
//                    layer.getChildren().add(tile);
//                }
//            }
//        }
//    }
//    public Background createBackgroundTile(int tileID, Image tileset, int tileSetRows, int tileSetColumns) {
//        int row = tileID / tileSetRows;
//        int column = tileID % tileSetColumns;
//        PixelReader pixelReader = tileset.getPixelReader();
//        WritableImage tileImage = new WritableImage(pixelReader, column * tileSize, row * tileSize,
//                tileSize, tileSize);
//        BackgroundSize backgroundSize = new BackgroundSize(tileSize, tileSize,
//                true, true, true, false);
//        BackgroundImage backgroundImage = new BackgroundImage(tileImage,
//                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
//                BackgroundPosition.DEFAULT, backgroundSize);
//
//        return new Background(backgroundImage);
//    }
//}
