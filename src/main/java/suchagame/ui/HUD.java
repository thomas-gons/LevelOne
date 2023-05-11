package suchagame.ui;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Pair;
import suchagame.ecs.component.GameplayComponent;
import suchagame.ecs.component.InventoryComponent;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.Item;
import suchagame.ecs.system.GameplaySystem;
import suchagame.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class HUD {
    private final AnchorPane root;
    private final Font customFont;
    private final Map<String, ImageView[]> statsIconsViews = new HashMap<>();

    private final Pair<String, Image>[] statsIcons = new Pair[]{
            new Pair<>("hp", new Image(Utils.getPathResource(Game.class, "images/heart.png"))),
            new Pair<>("mp", new Image(Utils.getPathResource(Game.class, "images/mana.png")))
    };
    private final int statsIconsCount = 5;

    private final Image emptySlot = new Image(Utils.getPathResource(Game.class, "images/empty_slot.png"));
    private Map<Item.ItemType, ImageView> handItemsViews = new HashMap<>();

    private Label consumableItemAmount;
    private final Image slimeDrop = new Image(Utils.getPathResource(Game.class, "images/items/slime_drop.png"));
    private Label slimeDropAmount;

    public HUD(AnchorPane root) throws IOException {
        this.root = root;
        URL fontUrl = getClass().getResource("fonts/disposabledroid-bb.regular.ttf");
        customFont = Font.loadFont(fontUrl.openStream(), 32);
        initAllStatBars();
        initHandSlot();
        initSlimeDrop();
    }

    private void initAllStatBars() {
        for (int i = 0; i < statsIcons.length; i++) {
            Image statIcon = statsIcons[i].getValue();
            statsIconsViews.put(statsIcons[i].getKey(), new ImageView[statsIconsCount]);
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
                statBarView.setLayoutY(5 + 48 * i);

                statsIconsViews.get(statsIcons[i].getKey())[j] = statBarView;
                this.root.getChildren().add(statBarView);
            }
        }
    }

    private void initHandSlot() {
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

        emptySlotViews[1].setLayoutX(2 * emptySlot.getWidth() + offset);
        emptySlotViews[1].setLayoutY(Game.height - emptySlot.getHeight() - 1.5 * offset);


        emptySlotViews[2].setLayoutX(emptySlot.getWidth() + offset);
        emptySlotViews[2].setLayoutY(Game.height - 2 * emptySlot.getHeight() - 2.5 * offset);

        Map< Item.ItemType, Item> handItems = Game.em.getPlayer().getComponent(GameplayComponent.class).getHandItems();
        for (Map.Entry<Item.ItemType, Item> entry : handItems.entrySet()) {
            Item item = entry.getValue();
            if (item == null) continue;
            Image itemImage = new Image(Utils.getPathResource(Game.class, "images/items/" + item.getTag() + ".png"));
            ImageView itemImageView = new ImageView(itemImage);
            itemImageView.setPreserveRatio(true);
            itemImageView.setSmooth(false);
            itemImageView.setLayoutX(emptySlotViews[entry.getKey().ordinal()].getLayoutX() +
                    (128 - itemImage.getWidth()) / 2f);
            itemImageView.setLayoutY(emptySlotViews[entry.getKey().ordinal()].getLayoutY() +
                    (128 - itemImage.getHeight()) / 2f);
            this.handItemsViews.put(entry.getKey(), itemImageView);
        }

        Circle consumableItemBackground = new Circle();
        consumableItemBackground.setRadius(24);
        consumableItemBackground.setFill(Color.color(0.30, 0.26, 0.20));
        consumableItemBackground.setLayoutX(emptySlotViews[1].getLayoutX() + 128 - 16);
        consumableItemBackground.setLayoutY(emptySlotViews[1].getLayoutY() + 128 - 16);

        consumableItemAmount = new Label();
        consumableItemAmount.setTextFill(Color.LIGHTGRAY);
        consumableItemAmount.setFont(customFont);
        consumableItemAmount.setText(String.valueOf(Game.sm.get(GameplaySystem.class).getAmountOfCurrentConsumable()));
        consumableItemAmount.setLayoutX(emptySlotViews[1].getLayoutX() + 128 - 20);
        consumableItemAmount.setLayoutY(emptySlotViews[1].getLayoutY() + 128 - 32);

        this.root.getChildren().addAll(emptySlotViews);
        this.root.getChildren().addAll(handItemsViews.values());
        this.root.getChildren().addAll(consumableItemBackground, consumableItemAmount);
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
        slimeDropAmount.setFont(customFont);
        slimeDropAmount.setText("100");
        slimeDropAmount.setLayoutX(Game.width - slimeDrop.getWidth() * 3 + 32);
        slimeDropAmount.setLayoutY(Game.height - slimeDrop.getHeight() - 14);
        this.root.getChildren().add(slimeDropAmount);
    }

    public Font getCustomFont() {
        return customFont;
    }

    public void updateSlimeDropAmount() {
        slimeDropAmount.setText(String.valueOf(Game.em.getPlayer().getComponent(InventoryComponent.class).getItemAmount("slimeDrop")));
    }

    public void updateStatBar(String statTag, float newStatValue) {
        float maxStatValue = Game.em.getPlayer().getComponent(StatsComponent.class).getStat(String.format("%s_max", statTag));
        int fullSymbolCount = (int) Math.floor(statsIconsCount * newStatValue / maxStatValue);
        int halfSymbolCount = (statsIconsCount * newStatValue / maxStatValue) % 1f > 0.5f ? 1: 0;
        int statIconIndex = IntStream.range(0, statsIcons.length)
                                 .filter(i -> statsIcons[i].getKey().equals(statTag))
                                 .findFirst()
                                 .orElse(-1);

        Image statIcon = statsIcons[statIconIndex].getValue();
        for (int i = 0; i < statsIconsCount; i++) {
            ImageView statIconView = statsIconsViews.get(statTag)[i];

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

    public void updateHandSlot(Item item) {
        Image itemImage = new Image(Utils.getPathResource(Game.class, "images/items/" + item.getTag() + ".png"));
        ImageView itemImageView = handItemsViews.get(item.getType());
        itemImageView.setImage(itemImage);
        if (item.getType() == Item.ItemType.CONSUMABLE) {
            updateConsumableItemAmount();
        }
    }

    public void updateConsumableItemAmount() {
        consumableItemAmount.setText(String.valueOf(Game.sm.get(GameplaySystem.class).getAmountOfCurrentConsumable()));
    }
}
