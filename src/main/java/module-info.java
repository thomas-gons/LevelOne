module suchagame.suchagame {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;


    exports suchagame.ui;
    opens suchagame.ui to javafx.fxml;
}