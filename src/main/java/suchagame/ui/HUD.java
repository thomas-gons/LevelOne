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

/**
 * Heads-up display
 */
public class HUD {
    private final AnchorPane root;
    private final Font customFont;
    private Map<String, Image> itemIcons = new HashMap<>();
    private final Map<String, ImageView[]> statsIconsViews = new HashMap<>();

    private final Pair<String, Image>[] statsIcons = new Pair[]{
            new Pair<>("hp", new Image(Utils.getPathResource(Game.class, "images/heart.png"))),
            new Pair<>("mp", new Image(Utils.getPathResource(Game.class, "images/mana.png")))
    };
    private final int statsIconsCount = Game.em.getItemsCount();

    private final Image emptySlot = new Image(Utils.getPathResource(Game.class, "images/empty_slot.png"));
    private Map<Item.ItemType, ImageView> handItemsViews = new HashMap<>();

    private Label consumableItemAmount;
    private Label slimeDropAmount;

    /**
     * Initializes all HUD elements and load custom font
     * @param root root pane
     * @throws IOException if font file is not found
     */
    public HUD(AnchorPane root) throws IOException {
        this.root = root;
        URL fontUrl = getClass().getResource("fonts/disposabledroid-bb.regular.ttf");
        assert fontUrl != null;
        customFont = Font.loadFont(fontUrl.openStream(), 32);
        initAllStatBars();
        initItemsIcons();
        initHandSlot();
        initSlimeDrop();
    }

    /**
     * init all stats bars (e.g. HP, MP, ...)
     */
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

    /**
     * init all hand slots (e.g. spell, consumable, ...)
     */
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

        Map< Item.ItemType, Item> handItems = Game.em.getPlayer().getHandItems();
        for (Map.Entry<Item.ItemType, Item> entry : handItems.entrySet()) {
            Item item = entry.getValue();
            if (item == null) continue;
            Image itemImage = itemIcons.get(item.getTag());
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
        consumableItemAmount.setLayoutX(emptySlotViews[1].getLayoutX() + 128 - 22);
        consumableItemAmount.setLayoutY(emptySlotViews[1].getLayoutY() + 128 - 32);

        this.root.getChildren().addAll(emptySlotViews);
        this.root.getChildren().addAll(handItemsViews.values());
        this.root.getChildren().addAll(consumableItemBackground, consumableItemAmount);
    }

    /**
     * init slime drop icon and amount
     */
    private void initSlimeDrop() {
        Image slimeDrop = getItemIcon("slime_drop");
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
        slimeDropAmount.setText(String.valueOf(Game.em.getPlayer().getComponent(InventoryComponent.class).getSlimeDropAmount()));
        slimeDropAmount.setLayoutX(Game.width - slimeDrop.getWidth() * 3 + 32);
        slimeDropAmount.setLayoutY(Game.height - slimeDrop.getHeight() - 14);
        this.root.getChildren().add(slimeDropAmount);
    }

    /**
     * init all item icons
     */
    private void initItemsIcons() {
        for (Item item: Game.em.getAllItems()) {
            this.itemIcons.put(
                    item.getTag(),
                    new Image(Utils.getPathResource(Game.class, "images/items/" + item.getTag() + ".png")));
        }
    }

    /**
     * resize the custom font
     * @param size new font size
     * @return the resized font
     */
    public Font resizeFont(int size) {
        return Font.font(customFont.getFamily(), size);
    }

    /**
     * get the item icon by tag
     * @param tag item tag
     * @return the item icon
     */
    public Image getItemIcon(String tag) {
        return itemIcons.get(tag);
    }

    /**
     * get the custom font
     * @return the custom font
     */
    public Font getCustomFont() {
        return customFont;
    }

    /**
     * update the slime drop amount
     */
    public void updateSlimeDropAmount() {
        slimeDropAmount.setText(String.valueOf(Game.em.getPlayer().getComponent(InventoryComponent.class).getSlimeDropAmount()));
    }

    /**
     * update the stat bar of the given stat tag
     */
    public void updateStatBar(String statTag, float newStatValue) {
        /*
            as we have three possible icons for each stat (full, half, empty)
            we need to calculate how many full, half and empty icons we need to display
         */
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

    /**
     * update the hand slot with the given item
     * @param item the new item
     */
    public void updateHandSlot(Item item) {
        Image itemImage = itemIcons.get(item.getTag());
        ImageView itemImageView = handItemsViews.get(item.getType());
        itemImageView.setImage(itemImage);
        if (item.getType() == Item.ItemType.CONSUMABLE) {
            updateConsumableItemAmount();
        }
    }

    /**
     * update the consumable item amount
     */
    public void updateConsumableItemAmount() {
        String newAmount = String.valueOf(Game.sm.get(GameplaySystem.class).getAmountOfCurrentConsumable());
        consumableItemAmount.setText(newAmount);
        consumableItemAmount.setLayoutX(362 - (newAmount.length() - 1) * 4);
    }
}
