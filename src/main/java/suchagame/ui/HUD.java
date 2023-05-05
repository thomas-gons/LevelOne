package suchagame.ui;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import suchagame.utils.Utils;

import java.io.IOException;
import java.net.URL;

public class HUD {
    private final AnchorPane root;
    private final Image heart = new Image(Utils.getPathResource(Game.class, "images/heart.png"));
    private final Image slimeDrop = new Image(Utils.getPathResource(Game.class, "images/slime_drop.png"));
    private final Font customFont;
    private Label slimeDropAmount;
    public HUD(AnchorPane root) throws IOException {
        this.root = root;
        URL fontUrl = getClass().getResource("fonts/disposabledroid-bb.regular.ttf");
        customFont = Font.loadFont(fontUrl.openStream(), 32);
        initHealthBar();
        initSlimeDrop();
    }

    public void initHealthBar() {
        for (int i = 0; i < 5; i++) {
            ImageView heartView = new ImageView(heart);
            heartView.setPreserveRatio(true);
            heartView.setSmooth(false);
            heartView.setFitWidth(48);
            heartView.setViewport(new Rectangle2D(0, 0, heart.getWidth() / 3, heart.getHeight()));
            heartView.setLayoutX(10 + 48 * i);
            heartView.setLayoutY(10);
            this.root.getChildren().add(heartView);
        }
    }

    public void initSlimeDrop() {
        ImageView slimeDropView = new ImageView(slimeDrop);
        slimeDropView.setPreserveRatio(true);
        slimeDropView.setSmooth(false);
        slimeDropView.setFitWidth(32);
        slimeDropView.setLayoutX(Game.width - slimeDrop.getWidth() * 3.5);
        slimeDropView.setLayoutY(Game.height - slimeDrop.getHeight() * 1.5);
        this.root.getChildren().add(slimeDropView);

        slimeDropAmount = new Label();
        slimeDropAmount.setTextFill(Color.LIGHTGRAY);
        slimeDropAmount.setFont(customFont); // set the font of the label to the custom font
        slimeDropAmount.setText("100");
        slimeDropAmount.setLayoutX(Game.width - slimeDrop.getWidth() * 3 + 32);
        slimeDropAmount.setLayoutY(Game.height - slimeDrop.getHeight() - 14);
        this.root.getChildren().add(slimeDropAmount);
    }

    public void updateSlimeDropAmount(int amount) {
        slimeDropAmount.setText(String.valueOf(amount));
    }
}
