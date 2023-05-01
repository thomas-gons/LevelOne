package suchagame.ui;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Pair;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.entity.MapEntity;
import suchagame.ecs.entity.Player;
import suchagame.utils.Utils;
import suchagame.utils.Vector2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Camera {
    public static float scale = 5f;
    public static int relativeWidth = (int) (Game.width / Camera.scale);
    public static int relativeHeight = (int) (Game.height / Camera.scale);
    private final Map<Integer, Pair<Image, int[]>> tileSetsSpriteSheet = new HashMap<>();
    private int tileCountRows = (int) Math.ceil((double) relativeHeight / MapEntity.defaultTileSize);
    private int tileCountCols = (int) Math.ceil((double) relativeWidth / MapEntity.defaultTileSize);
    private int tileSize = Math.max(Game.width / tileCountCols, Game.height / tileCountRows);

    private Vector2<Integer> positionInTiles = new Vector2<>(0, 0);
    private Vector2<Float> offsetInTiles = new Vector2<>(0f, 0f);
    public Camera() {
        this.loadTileSets();
        Game.freeSpace = new BoundingBox(
            (float) relativeWidth / 2,
            (float) relativeHeight / 2,
            Game.width - relativeWidth,
            Game.height - relativeHeight
        );
    }

    private void loadTileSets() {
        for (int layerID = 0; layerID  < MapEntity.layersCount; layerID++) {
            Image spriteSheet = new Image(Utils.getPathResource(Game.class, "images/map_layer_" + (layerID + 1) + ".png"));
            int spriteSheetRows = (int) (spriteSheet.getHeight() / MapEntity.defaultTileSize);
            int spriteSheetCols = (int) (spriteSheet.getWidth() / MapEntity.defaultTileSize);
            tileSetsSpriteSheet.put(layerID, new Pair<>(spriteSheet, new int[]{spriteSheetRows, spriteSheetCols}));
        }
    }

    public void update() {
        Vector2<Float> playerPosition = Player.player.getComponent(TransformComponent.class).getPosition();
        this.offsetInTiles.set(0f, 0f);
        if (Game.freeSpace.getMinX() > playerPosition.getX())
            this.positionInTiles.setX(0);
        else if (Game.freeSpace.getMaxX() < playerPosition.getX())
            this.positionInTiles.setX((int) (Game.freeSpace.getWidth() / MapEntity.defaultTileSize));
        else {
            this.positionInTiles.setX((int) ((playerPosition.getX() - relativeWidth / 2) / MapEntity.defaultTileSize));
            this.offsetInTiles.setX((playerPosition.getX() - (float) relativeWidth / 2) % MapEntity.defaultTileSize * scale);
        }

        if (Game.freeSpace.getMinY() > playerPosition.getY())
            this.positionInTiles.setY(0);
        else if (Game.freeSpace.getMaxY() < playerPosition.getY())
            this.positionInTiles.setY((int) (Game.freeSpace.getHeight() / MapEntity.defaultTileSize));
        else {
            this.positionInTiles.setY((int) ((playerPosition.getY() - relativeHeight / 2) / MapEntity.defaultTileSize));
            this.offsetInTiles.setY((playerPosition.getY() - (float) relativeHeight / 2) % MapEntity.defaultTileSize * scale);
        }
    }
   public void render(GraphicsContext gc) {
        update();

        int boundsRows = (this.positionInTiles.getY() + tileCountRows) >= MapEntity.globalTileCountRows ?
                tileCountRows: tileCountRows + 1;

        int boundsCols = (this.positionInTiles.getX() + tileCountCols) >= MapEntity.globalTileCountCols ?
                tileCountCols: tileCountCols + 1;

        List<int[][]> layers = MapEntity.map.getLayers();
        for (int layerID = 0; layerID < MapEntity.layersCount; layerID++) {
            int[][] layer = layers.get(layerID);

            for (int y = this.positionInTiles.getY(), i = 0; i < boundsRows; y++, i++) {
                for (int x = this.positionInTiles.getX(), j = 0; j < boundsCols; x++, j++) {
                    gc.drawImage(
                            tileSetsSpriteSheet.get(layerID).getKey(),
                            (layer[y][x] % tileSetsSpriteSheet.get(layerID).getValue()[1]) * MapEntity.defaultTileSize,
                            (layer[y][x] / tileSetsSpriteSheet.get(layerID).getValue()[1]) * MapEntity.defaultTileSize,
                            MapEntity.defaultTileSize,
                            MapEntity.defaultTileSize,
                            (j * tileSize) - this.offsetInTiles.getX(),
                            (i * tileSize) - this.offsetInTiles.getY(),
                            tileSize,
                            tileSize
                    );
                }
            }
        }
//        gc.setLineWidth(1);
//        gc.setStroke(Color.LAWNGREEN);
//        for (int i = 0; i <= MapEntity.globalTileCountRows; i++) {
//            gc.strokeLine(0, i * tileSize - this.position.getX(), Game.width, i * tileSize - this.position.getX());
//        }
//        for (int i = 0; i < MapEntity.globalTileCountCols; i++) {
//            gc.strokeLine(i * tileSize - this.position.getY(), 0, i * tileSize - this.position.getY(), Game.height);
//        }
    }

    public void setScale(float scale) {
        Camera.scale = scale;
        Camera.relativeWidth = (int) (Game.width * Camera.scale);
        Camera.relativeHeight = (int) (Game.height * Camera.scale);
        this.tileCountRows = (int) Math.ceil(Game.height / (double) relativeHeight);
        this.tileCountCols = (int) Math.ceil(Game.width / (double) relativeWidth);
        this.tileSize = Math.max(Game.width / tileCountCols, Game.height / tileCountRows);

    }

}
