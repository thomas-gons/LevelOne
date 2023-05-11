package suchagame.ui;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Pair;
import suchagame.ecs.component.GraphicComponent;
import suchagame.ecs.component.PhysicComponent;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.entity.MapEntity;
import suchagame.ecs.entity.Player;
import suchagame.ecs.system.GraphicSystem;
import suchagame.utils.Utils;
import suchagame.utils.Vector2f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Camera {
    public static float scale = 5f;
    public static int relativeWidth = (int) (Game.width / Camera.scale);
    public static int relativeHeight = (int) (Game.height / Camera.scale);

    public static BoundingBox viewport = new BoundingBox(0, 0, relativeWidth, relativeHeight);
    private final Map<Integer, Pair<Image, int[]>> tileSetsSpriteSheet = new HashMap<>();
    private int tileCountRows = (int) Math.ceil((double) relativeHeight / MapEntity.defaultTileSize);
    private int tileCountCols = (int) Math.ceil((double) relativeWidth / MapEntity.defaultTileSize);
    private int tileSize = Math.max(Game.width / tileCountCols, Game.height / tileCountRows);

    private final Vector2f position = new Vector2f(0f, 0f);
    private final Vector2f offsetInTiles = new Vector2f(0f, 0f);
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
        Vector2f playerPosition = Game.em.getPlayer().getComponent(TransformComponent.class).getPosition();
        this.offsetInTiles.set(0f, 0f);
        if (Game.freeSpace.getMinX() > playerPosition.getX())
            this.position.setX(0f);
        else if (Game.freeSpace.getMaxX() < playerPosition.getX())
            this.position.setX((float) Game.freeSpace.getWidth());
        else {
            this.position.setX(playerPosition.getX() - relativeWidth / 2f);
            this.offsetInTiles.setX((playerPosition.getX() - relativeWidth / 2f) % MapEntity.defaultTileSize * scale);
        }

        if (Game.freeSpace.getMinY() > playerPosition.getY())
            this.position.setY(0f);
        else if (Game.freeSpace.getMaxY() < playerPosition.getY())
            this.position.setY((float) Game.freeSpace.getHeight());
        else {
            this.position.setY(playerPosition.getY() - relativeHeight / 2f);
            this.offsetInTiles.setY((playerPosition.getY() - relativeHeight / 2f) % MapEntity.defaultTileSize * scale);
        }

        Camera.viewport = new BoundingBox(
                this.position.getX(),
                this.position.getY(),
                relativeWidth,
                relativeHeight
        );
    }
   public void render(GraphicsContext gc) {
        update();

        int[] positionInTiles = new int[]{
                (int) (this.position.getX() / MapEntity.defaultTileSize),
                (int) (this.position.getY() / MapEntity.defaultTileSize)
        };
        int boundsRows = (positionInTiles[1] + tileCountRows) >= MapEntity.globalTileCountRows ?
                tileCountRows: tileCountRows + 1;

        int boundsCols = (positionInTiles[0] + tileCountCols) >= MapEntity.globalTileCountCols ?
                tileCountCols: tileCountCols + 1;

        List<int[][]> layers = Game.em.getMap().getLayers();
        for (int layerID = 0; layerID < MapEntity.layersCount; layerID++) {
            int[][] layer = layers.get(layerID);

            for (int y = positionInTiles[1], i = 0;i < boundsRows; y++, i++) {
                for (int x = positionInTiles[0], j = 0;j < boundsCols; x++, j++) {
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
    }

    public void alterScale(float delta) {
        if (Camera.scale + delta < 2f || Camera.scale + delta > 7f)
            return;
        Camera.scale += delta;
        Camera.relativeWidth = (int) (Game.width / Camera.scale);
        Camera.relativeHeight = (int) (Game.height / Camera.scale);
         Game.freeSpace = new BoundingBox(
            (float) relativeWidth / 2,
            (float) relativeHeight / 2,
            Game.width - relativeWidth,
            Game.height - relativeHeight
        );
        this.tileCountRows =  (int) Math.ceil((double) relativeHeight / MapEntity.defaultTileSize);
        this.tileCountCols = (int) Math.ceil((double) relativeWidth / MapEntity.defaultTileSize);
        this.tileSize =  Math.max(Game.width / tileCountCols, Game.height / tileCountRows);
    }

    public Vector2f getPosition() {
        return position;
    }

}

