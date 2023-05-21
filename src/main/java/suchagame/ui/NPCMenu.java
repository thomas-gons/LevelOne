package suchagame.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import suchagame.ecs.component.InventoryComponent;
import suchagame.ecs.entity.Item;
import suchagame.ecs.system.GameplaySystem;
import suchagame.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class representing the NPC menu.
 */
public class NPCMenu {
    private final AnchorPane root;
    private boolean isNPCMenuView = false;
    private final ArrayList<AnchorPane> itemContainers = new ArrayList<>();
    private final ArrayList<Item> itemsForSale = new ArrayList<>();
    private static Timeline checkPlayerPosition = new Timeline();

    /**
     * Creates the NPC menu with all the items for sale. The menu is hidden by default but
     * can be show when the player interacts with the NPC with A key.
     * @param root the root pane of the game
     */
    public NPCMenu(AnchorPane root) {
        this.root = root;
        this.root.visibleProperty().setValue(false);

        Image npcMenuImage = new Image(Utils.getPathResource(Game.class, "images/npc_background_menu.png"));

        ImageView npcMenuView = new ImageView(npcMenuImage);
        npcMenuView.setPreserveRatio(true);
        npcMenuView.setSmooth(false);
        npcMenuView.setLayoutX((1920 - npcMenuImage.getWidth()) / 2);
        npcMenuView.setLayoutY((1080 - npcMenuImage.getHeight()) / 2);
        this.root.getChildren().add(npcMenuView);
        fillNPCMenu();
        initMouseControl();

        checkPlayerPosition = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if (!Game.sm.get(GameplaySystem.class).isNearNPC()) {
                toggleNPCMenuView();
            }
        }));
        checkPlayerPosition.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Initializes the mouse control for the NPC menu e.g. the player can buy items by clicking on them.
     */
    private void initMouseControl() {
        Map<Item, Integer> npcInventory = Game.em.getNPC().getComponent(InventoryComponent.class).getInventory();
        InventoryComponent inventoryComponent = Game.em.getPlayer().getComponent(InventoryComponent.class);
        Map<Item, Integer> playerInventory = inventoryComponent.getInventory();

        for (AnchorPane itemContainer : itemContainers) {
            itemContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                int index = itemContainers.indexOf(itemContainer);
                Item item = itemsForSale.get(index);
                int quantity = npcInventory.get(item);
                if (inventoryComponent.getSlimeDropAmount() < item.getSlimeDropValue() * 1.5 || npcInventory.get(item) == 0) {
                    return;
                }
                playerInventory.put(
                        Game.em.getItem("slime_drop"),
                        (int) (inventoryComponent.getSlimeDropAmount() - item.getSlimeDropValue() * 1.5)
                );

                npcInventory.put(item, quantity - 1);
                playerInventory.merge(item, 1, Integer::sum);
                Game.sm.get(GameplaySystem.class).setHandItem(item);
                Game.hud.updateHandSlot(item);
                Game.hud.updateSlimeDropAmount();
                Label itemLabel = (Label) itemContainer.getChildren().get(0);
                itemLabel.setText(String.format("%s x%d", item.getTag().replace("_", " "), quantity - 1));
            });
        }
    }


    /**
     * Fills the NPC menu with all the items for sale (from the NPC inventory).
     */
    private void fillNPCMenu() {
        Label title = new Label("--- SHOP ---");
        title.setFont(Game.hud.resizeFont(50));
        title.setLayoutX(825);
        title.setLayoutY(200);

        this.root.getChildren().add(title);

        InventoryComponent inventoryComponent = Game.em.getNPC().getComponent(InventoryComponent.class);
        int i = 0;
        for (Map.Entry<Item, Integer> entry : inventoryComponent.getInventory().entrySet()) {
            Item item = entry.getKey();
            Label itemLabel = new Label(String.format("%s x%d",
                    item.getTag().replace("_", " "), entry.getValue()));

            itemLabel.setFont(Game.hud.getCustomFont());
            itemLabel.setLayoutX(100);
            itemLabel.setLayoutY(25);


            String itemCost = String.valueOf(item.getSlimeDropValue() * 1.5);
            Label itemCostLabel = new Label(itemCost);
            itemCostLabel.setFont(Game.hud.getCustomFont());
            itemCostLabel.setLayoutX(400 - 15 * itemCost.length());
            itemCostLabel.setLayoutY(25);

            Image itemImage = Game.hud.getItemIcon(item.getTag());
            ImageView itemImageView = new ImageView(itemImage);
            itemImageView.setPreserveRatio(true);
            itemImageView.setSmooth(false);
            itemImageView.setFitWidth(72);

            AnchorPane itemContainer = new AnchorPane();
            itemContainer.setLayoutX(750);
            itemContainer.setLayoutY(300 + 96 * i);
            itemContainer.setPrefWidth(400);
            itemContainer.setPrefHeight(96);
            itemContainer.getChildren().addAll(itemLabel, itemCostLabel, itemImageView);

            this.root.getChildren().add(itemContainer);

            itemContainers.add(itemContainer);
            itemsForSale.add(item);
            i++;
        }
    }

    /**
     * Toggles the NPC menu view.
     */
    public void toggleNPCMenuView() {
        this.isNPCMenuView = !this.isNPCMenuView;
        if (this.isNPCMenuView) {
            checkPlayerPosition.play();
        } else {
            checkPlayerPosition.stop();
        }
        this.root.visibleProperty().setValue(this.isNPCMenuView);
    }
}

