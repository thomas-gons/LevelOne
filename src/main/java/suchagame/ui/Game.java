package suchagame.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import suchagame.ecs.EntityManager;
import suchagame.ecs.SystemManager;


/**
 * The Game class represents the main entry point for the game application.
 * It extends the JavaFX Application class and handles the initialization and setup of the game.
 */
public class Game extends Application {
    public final static int width = 1920;
    public final static int height = 1080;
    public static Scene scene;
    public static Parent root;
    public static Canvas canvas;
    public static GraphicsContext gc;
    public static Camera camera;
    public static BoundingBox freeSpace;
    public static boolean lightEnabled = true;
    public static HUD hud;
    public static NPCMenu npcMenu;

    public static EntityManager em;
    public static SystemManager sm;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main entry point for the JavaFX application.
     *
     * @param stage The primary stage for the application.
     * @throws Exception if an error occurs during application startup.
     *
     */
   @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("game.fxml"));
        root = fxmlLoader.load();

        canvas = (Canvas) root.lookup("#game_canvas");
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        scene = new Scene(root, width, height);

        camera = new Camera();
        em = new EntityManager();
        em.initEntities();
        sm = new SystemManager();

        hud = new HUD((AnchorPane) root.lookup("#game_hud"));
        npcMenu = new NPCMenu((AnchorPane) root.lookup("#game_npc_menu"));

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            camera.alterScale((float) event.getDeltaY() / 1000 * 25f);
        });
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                lightEnabled = !lightEnabled;
                Game.canvas.setEffect(null);
            }
        });
        new GameLoop();
        new Debug((AnchorPane) root.lookup("#game_debug"));

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
   }
}
