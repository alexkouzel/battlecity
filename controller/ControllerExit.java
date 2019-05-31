package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControllerExit {
    @FXML
    public Button buttonYes;
    public Button buttonNo;
    private ControllerCreator controllerCreator;

    public void buttonAction(ActionEvent actionEvent) {
        if (((Button) actionEvent.getTarget()).getText().equals("Yes")) {
            Stage stage = (Stage) buttonYes.getScene().getWindow();
            ((Stage) stage.getOwner()).close();
        } else {
            Stage stage = (Stage) buttonNo.getScene().getWindow();
            stage.close();
        }
    }

    void initData(ControllerCreator controllerCreator)
    {
        this.controllerCreator = controllerCreator;
    }
}
