module suchagame.suchagame {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotations;


    exports suchagame.ui;
    opens suchagame.ui to javafx.fxml;
}