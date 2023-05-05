package suchagame.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import suchagame.ecs.EntityManager;
import suchagame.ecs.component.LayersComponent;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.entity.MapEntity;
import suchagame.ecs.entity.Mob;
import suchagame.ecs.entity.Player;
import suchagame.ecs.system.InputSystem;
import suchagame.utils.Vector2;


public class Game extends Application {
    public final static int width = 1920;
    public final static int height = 1080;
    public static Scene scene;
    public static Parent root;
    public static Canvas canvas;
    public static GraphicsContext gc;
    public static Camera camera = new Camera();
    public static BoundingBox freeSpace;
    public static boolean lightEnabled = true;
    public static HUD hud;

    public static int check = 100;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("game.fxml"));
        root = fxmlLoader.load();
        canvas = (Canvas) root.lookup("#game_canvas");
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        hud = new HUD((AnchorPane) root.lookup("#game_hud"));
        MapEntity.map = new MapEntity();
        EntityManager.instance.addEntity(MapEntity.map);
        EntityManager.instance.addEntity(Player.player);
        for (int i = 0; i < 16; i++)
            EntityManager.instance.addEntity(new Mob());
        scene = new Scene(root, width, height);
        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            camera.alterScale((float) event.getDeltaY() / 1000);
        });
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                lightEnabled = !lightEnabled;
                Game.canvas.setEffect(null);
            }
        });
        new InputSystem(scene);
        new GameLoop();
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }
}
