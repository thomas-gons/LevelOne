package suchagame.ecs.component;

import com.fasterxml.jackson.annotation.JsonCreator;
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

public class LayersComponent extends Component {
     private List<int[][]> layers;
     private boolean[][] collisionLayerCheck;

     private static final int[] transparentTilesID = {
             0, 26, 31, 34, 59, 60, 69, 117, 118,
             120, 121, 126, 127, 128, 129, 148, 149,
             152, 153, 157, 158, 159, 160, 161, 162,
             163, 164, 165, 166, 167, 168

     };
     @JsonCreator
     public LayersComponent() {
         this.layers = new ArrayList<>(MapEntity.layersCount);
         for (int layerID = 0; layerID < MapEntity.layersCount; layerID++) {
             try {
                 loadLayerData(layerID);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         this.collisionLayerCheck = new boolean[MapEntity.globalTileCountRows][MapEntity.globalTileCountCols];

         for (int i = 0; i < MapEntity.globalTileCountRows; i++) {
                boolean[] rows = new boolean[MapEntity.globalTileCountCols];
                Arrays.fill(rows, true);
                this.collisionLayerCheck[i] = rows;
         }
         loadCollisionLayerData();
     }

     private void loadLayerData(int layerID) throws RuntimeException, IOException {
        int[][] rawLayer = new int[MapEntity.globalTileCountRows][MapEntity.globalTileCountCols];
        URL resourceUrl = Main.class.getResource("map_layer_" + (layerID + 1) + ".csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(resourceUrl).openStream()));
        String line;
        int rowIndex = 0;
        while ((line = br.readLine()) != null) {
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

    private void loadCollisionLayerData()  {
         int[][] collidingLayer = layers.get(MapEntity.layersCount - 1);
         for (int x = 0; x < MapEntity.globalTileCountRows; x++) {
             for (int y = 0; y < MapEntity.globalTileCountCols; y++) {
                 int tileID = collidingLayer[x][y];
                 for (int j : transparentTilesID) {
                     if (j == tileID) {
                         this.collisionLayerCheck[x][y] = false;
                         break;
                     }
                 }
            }
        }
    }

    public int getTile(int layerID, Vector2f position) {
        return layers.get(layerID)[((int) position.getY()) / MapEntity.defaultTileSize]
                                  [((int) position.getX()) / MapEntity.defaultTileSize];
    }

    public int[][] getLayer(int layerID) {
        return layers.get(layerID);
    }

    public List<int[][]> getAllLayers() {
         return this.layers;
    }

    public boolean[][] getCollisionLayerCheck() {
         return this.collisionLayerCheck;
    }

}
