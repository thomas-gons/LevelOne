package suchagame.ui;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;
import suchagame.ecs.component.InventoryComponent;
import suchagame.ecs.component.StatsComponent;
import suchagame.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.stream.IntStream;

public class HUD {
    private final AnchorPane root;

    private final Pair<String, Image>[] statsIcons = new Pair[]{
            new Pair<>("hp", new Image(Utils.getPathResource(Game.class, "images/heart.png"))),
//            new Pair<>("mp", new Image(Utils.getPathResource(Game.class, "images/mana.png")))
    };

    private final int statsIconsCount = 5;

    private final Image emptySlot = new Image(Utils.getPathResource(Game.class, "images/empty_slot.png"));
    private final Image slimeDrop = new Image(Utils.getPathResource(Game.class, "images/slime_drop.png"));
    private final Font customFont;
    private Label slimeDropAmount;
    public HUD(AnchorPane root) throws IOException {
        this.root = root;
        URL fontUrl = getClass().getResource("fonts/disposabledroid-bb.regular.ttf");
        customFont = Font.loadFont(fontUrl.openStream(), 32);
        initAllStatBars();
        initEmptySlot();
        initSlimeDrop();
    }

    private void initAllStatBars() {
        for (int i = 0; i < statsIcons.length; i++) {
            Image statIcon = statsIcons[i].getValue();
            for (int j = 0; j < statsIconsCount; j++) {
                ImageView statBarView = new ImageView(statIcon);
                statBarView.setPreserveRatio(true);
                statBarView.setSmooth(false);
                statBarView.setViewport(new Rectangle2D(
                        0, 0,
                        statIcon.getWidth() / 3, statIcon.getHeight()
                ));

                statBarView.setFitWidth(48);
                statBarView.setLayoutX(10 + 48 * j);
                statBarView.setLayoutY(10 + 48 * i);

                this.root.getChildren().add(statBarView);
            }
        }
    }

    private void initEmptySlot() {
        ImageView[] emptySlotViews = new ImageView[3];
        int offset = 64;
        for (int i = 0; i < 3; i++) {
            emptySlotViews[i] = new ImageView(emptySlot);
            emptySlotViews[i].setPreserveRatio(true);
            emptySlotViews[i].setSmooth(false);
            emptySlotViews[i].setFitWidth(128);
        }

        emptySlotViews[0].setLayoutX(offset);
        emptySlotViews[0].setLayoutY(Game.height - emptySlot.getHeight() - 1.5 * offset);

        emptySlotViews[1].setLayoutX(emptySlot.getWidth() + offset);
        emptySlotViews[1].setLayoutY(Game.height - 2 * emptySlot.getHeight() - 2.5 * offset);

        emptySlotViews[2].setLayoutX(2 * emptySlot.getWidth() + offset);
        emptySlotViews[2].setLayoutY(Game.height - emptySlot.getHeight() - 1.5 * offset);


        this.root.getChildren().addAll(emptySlotViews);
    }

    private void initSlimeDrop() {
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

    public void updateSlimeDropAmount() {
        slimeDropAmount.setText(String.valueOf(Game.em.getPlayer().getComponent(InventoryComponent.class).getItemAmount("slimeDrop")));
    }

    public void updateStatBar(String statTag, float newStatValue) {
        float maxStatValue = Game.em.getPlayer().getComponent(StatsComponent.class).getStat(String.format("%s_max", statTag));
        int fullSymbolCount = (int) Math.ceil((newStatValue / maxStatValue * statsIconsCount));
        int halfSymbolCount = (newStatValue / maxStatValue * statsIconsCount % 1.0f < 0.5 ? 1: 0);
        int statIconIndex = IntStream.range(0, statsIcons.length)
                                 .filter(i -> statsIcons[i].getKey().equals(statTag))
                                 .findFirst()
                                 .orElse(-1);

        Image statIcon = statsIcons[statIconIndex].getValue();
        for (int i = 0; i < statsIconsCount; i++) {
            int finalI = i;
            ImageView statIconView = this.root.getChildren().stream()
                    .filter(node -> node instanceof ImageView)
                    .map(node -> (ImageView) node)
                    .filter(node -> node.getImage().equals(statIcon))
                    .filter(node -> node.getLayoutX() == 10 + 48 * finalI)
                    .filter(node -> node.getLayoutY() == 10 + 48 * statIconIndex)
                    .findFirst()
                    .orElseThrow();

            if (i < fullSymbolCount) {
                statIconView.setViewport(new Rectangle2D(
                        0, 0,
                        statIcon.getWidth() / 3, statIcon.getHeight()
                ));

            } else if (i < fullSymbolCount + halfSymbolCount) {
                statIconView.setViewport(new Rectangle2D(
                        statIcon.getWidth() / 3, 0,
                        statIcon.getWidth() / 3, statIcon.getHeight()
                ));

            } else {
                statIconView.setViewport(new Rectangle2D(
                        statIcon.getWidth() / 3 * 2, 0,
                        statIcon.getWidth() / 3, statIcon.getHeight()
                ));
            }
        }
    }

    public Font getCustomFont() {
        return customFont;
    }
}
