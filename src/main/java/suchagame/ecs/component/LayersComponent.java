package suchagame.ecs.component;

import suchagame.Main;
import suchagame.ecs.entity.MapEntity;
import suchagame.utils.Vector2f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Component for managing map-related data and actions.
 */
public class LayersComponent extends Component {

    // list of layers, where each layer is a 2D array of tile IDs
     private final List<int[][]> layers;

     // 2D array of booleans representing whether a tile is transparent
     private final boolean[][] collidingLayer;

     private static final int[] transparentTilesID = {
             0, 26, 31, 34, 59, 60, 69, 117, 118,
             120, 121, 126, 127, 128, 129, 148, 149,
             152, 153, 157, 158, 159, 160, 161, 162,
             163, 164, 165, 166, 167, 168

     };
     /**
      * Constructs a LayersComponent object with the static map layers count.
      */
     public LayersComponent() {
         this.layers = new ArrayList<>(MapEntity.layersCount);
         for (int layerID = 0; layerID < MapEntity.layersCount; layerID++) {
             try {
                 loadLayerData(layerID);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         this.collidingLayer = new boolean[MapEntity.globalTileCountRows][MapEntity.globalTileCountCols];

         for (int i = 0; i < MapEntity.globalTileCountRows; i++) {
                boolean[] rows = new boolean[MapEntity.globalTileCountCols];
                Arrays.fill(rows, true);
                this.collidingLayer[i] = rows;
         }
         initCollisionLayer();
     }

    /**
     * Loads the layer data as a 2D matrix of tileID from the CSV file with the specified layer ID.
     * @param layerID the ID of the layer to load
     * @throws RuntimeException if the file cannot be parsed
     * @throws IOException if the file cannot be found
     */
     private void loadLayerData(int layerID) throws RuntimeException, IOException {
        int[][] rawLayer = new int[MapEntity.globalTileCountRows][MapEntity.globalTileCountCols];
        // load the layer data from the CSV file in the resources folder
        URL resourceUrl = Main.class.getResource("map_layer_" + (layerID + 1) + ".csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(resourceUrl).openStream()));
        String line;
        int rowIndex = 0;
        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] values = line.split(",");
            int[] row = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                row[i] = Integer.parseInt(values[i]);
            }
            rawLayer[rowIndex++] = row;
        }
        br.close();
        layers.add(rawLayer);
    }

    /**
     * Initializes the collision layer by setting the tiles that are transparent to false using to top layer.
     */
    private void initCollisionLayer()  {
         int[][] collidingLayer = layers.get(MapEntity.layersCount - 1);
         for (int x = 0; x < MapEntity.globalTileCountRows; x++) {
             for (int y = 0; y < MapEntity.globalTileCountCols; y++) {
                 int tileID = collidingLayer[x][y];
                 for (int j : transparentTilesID) {
                     if (j == tileID) {
                         this.collidingLayer[x][y] = false;
                         break;
                     }
                 }
            }
        }
    }

    /**
     * Returns the tile ID at the specified position in the specified layer.
     * @param layerID the ID of the layer to get the tile from
     * @param position the position of the tile to get
     * @return the tile ID at the specified position in the specified layer
     */
    public int getTile(int layerID, Vector2f position) {
        return layers.get(layerID)[((int) position.getY()) / MapEntity.defaultTileSize]
                                  [((int) position.getX()) / MapEntity.defaultTileSize];
    }

    public List<int[][]> getLayers() {
        return layers;
    }

    public boolean[][] getCollidingLayer() {
         return this.collidingLayer;
    }

    /**
     * Returns whether the tile at the specified position is a hole tile (i.e. the player can fall through it).
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @return whether the tile at the specified position is a hole tile
     */
    public boolean isHoleTile(int y, int x) {
         int tileID = this.layers.get(0)[y][x];
         return tileID == 79;
    }

}
