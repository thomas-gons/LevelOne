module suchagame.suchagame {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.yaml.snakeyaml;
    requires annotations;


    exports suchagame.ui;
    opens suchagame.ui to javafx.fxml;
}