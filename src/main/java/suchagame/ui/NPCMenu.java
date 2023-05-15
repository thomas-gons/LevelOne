package suchagame.ui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import suchagame.ecs.component.InventoryComponent;
import suchagame.ecs.entity.Item;
import suchagame.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

public class NPCMenu {
    private final AnchorPane root;
    private boolean isNPCMenuView = false;

    private final ArrayList<AnchorPane> itemContainers = new ArrayList<>();
    private final ArrayList<Item> itemsForSale = new ArrayList<>();
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
    }

    private void initMouseControl() {
        Map<Item, Integer> npcInventory = Game.em.getNPC().getComponent(InventoryComponent.class).getInventory();
        Map<Item, Integer> playerInventory = Game.em.getPlayer().getComponent(InventoryComponent.class).getInventory();

        for (AnchorPane itemContainer : itemContainers) {
            itemContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                int index = itemContainers.indexOf(itemContainer);
                Item item = itemsForSale.get(index);
                int quantity = npcInventory.get(item);
                if (npcInventory.get(item) == 0) {
                    return;
                }
                npcInventory.put(item, quantity - 1);
                playerInventory.merge(item, 1, Integer::sum);
                Game.hud.updateHandSlot(item);
                Label itemLabel = (Label) itemContainer.getChildren().get(0);
                itemLabel.setText(String.format("%s x%d", item.getTag().replace("_", " "), quantity - 1));
            });
        }
    }

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


            String itemCost = String.valueOf(item.getSlimeDropValue());
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

    public void toggleNPCMenuView() {
        this.isNPCMenuView = !this.isNPCMenuView;
        this.root.visibleProperty().setValue(this.isNPCMenuView);
    }
}

