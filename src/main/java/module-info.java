module com.battlecity {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens com.battlecity to javafx.fxml;
    exports com.battlecity;

    opens com.battlecity.controllers to javafx.fxml;
    exports com.battlecity.controllers;
}