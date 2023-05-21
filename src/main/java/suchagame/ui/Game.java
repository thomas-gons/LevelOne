package suchagame.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import suchagame.ecs.EntityManager;
import suchagame.ecs.SystemManager;
import suchagame.ecs.entity.Player;

/**
 * Main class of the game. Initializes the game loop and the game scene.
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

    private static GameLoop gameLoop;

    public static Debug debug;

    private static boolean isGameRunning;

    /**
     * Main method of the game. Launches the game.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the game. Initializes the game loop, the game scene and ECS system.
     * @param stage the stage to be used
     * @throws Exception if the game scene cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("game.fxml"));
        root = fxmlLoader.load();

        canvas = (Canvas) root.lookup("#game_canvas");
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        scene = new Scene(root, width, height);

        // ecs init
        em = new EntityManager();
        em.initEntities();
        sm = new SystemManager();

        hud = new HUD((AnchorPane) root.lookup("#game_hud"));
        npcMenu = new NPCMenu((AnchorPane) root.lookup("#game_npc_menu"));

        // camera alter scale on scroll
        scene.addEventHandler(ScrollEvent.SCROLL, event -> camera.alterScale((float) event.getDeltaY() / 1000 * 25f));

        // toggle light on space
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                lightEnabled = !lightEnabled;
                Game.canvas.setEffect(null);
            }
        });

        camera = new Camera();
        gameLoop = new GameLoop();
        isGameRunning = true;
        debug = new Debug((AnchorPane) root.lookup("#game_debug"));

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    /**
     * Ends the game. Stops the game loop and removes all entities and systems.
     * Finally, displays the end game screen with the given message.
     * @param end_message the message to be displayed
     */
    public static void endGame(String end_message) {
        if (!isGameRunning)
            return;

        isGameRunning = false;
        Game.gameLoop.stop();
        Game.debug.stop();
        Game.sm.removeAllSystems();
        Game.em.removeAllEntities();

        AnchorPane end_game = (AnchorPane) root.lookup("#end_game");
        end_game.setStyle("-fx-background-color: black");

        Label end_game_label = new Label(end_message);
        end_game_label.setFont(hud.resizeFont(100));
        end_game_label.setStyle("-fx-text-fill: rgba(190, 190, 190 , 1)");
        end_game_label.setLayoutX(1920 / 2f - end_message.length() * 25);
        end_game_label.setLayoutY(480);
        end_game.getChildren().add(end_game_label);

        if (end_message.equals("GAME OVER")) {
            Label death_cause_label = new Label(Player.getDeathCause());
            death_cause_label.setFont(hud.resizeFont(40));
            death_cause_label.setStyle("-fx-text-fill: rgba(150, 150, 150 , 1)");
            death_cause_label.setLayoutX(1920 / 2f - Player.getDeathCause().length() * 8.5);
            death_cause_label.setLayoutY(580);
            end_game.getChildren().add(death_cause_label);
        }


        Label end_game_quit = new Label("Press ESC to quit");
        end_game_quit.setFont(hud.resizeFont(25));
        end_game_quit.setStyle("-fx-text-fill: rgba(190, 190, 190 , 1)");
        end_game_quit.setLayoutX(1715);
        end_game_quit.setLayoutY(1030);

        end_game.getChildren().add(end_game_quit);

        end_game.toFront();

        // add fade in effect to end game screen
        Timeline fade_in_effect = new Timeline(new KeyFrame(Duration.millis(50), event -> end_game.setOpacity(end_game.getOpacity() + 0.05)));
        fade_in_effect.setCycleCount(Timeline.INDEFINITE);
        fade_in_effect.play();

        // quit on ESC
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });
    }

}
