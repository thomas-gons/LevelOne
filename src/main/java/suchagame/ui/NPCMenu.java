package suchagame.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import suchagame.utils.Utils;

public class NPCMenu {
    private final AnchorPane root;
    private boolean isNPCMenuView = false;
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
    }

    public void toggleNPCMenuView() {
        this.isNPCMenuView = !this.isNPCMenuView;
        this.root.visibleProperty().setValue(this.isNPCMenuView);
    }
}

