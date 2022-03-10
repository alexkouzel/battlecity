package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ExitController {
    private CreatorController creatorController;

    @FXML
    public Button buttonYes;
    public Button buttonNo;

    public void buttonAction(ActionEvent actionEvent) {
        if (((Button) actionEvent.getTarget()).getText().equals("Yes")) {
            Stage stage = (Stage) buttonYes.getScene().getWindow();
            ((Stage) stage.getOwner()).close();
        } else {
            Stage stage = (Stage) buttonNo.getScene().getWindow();
            stage.close();
        }
    }

    public void initData(CreatorController creatorController) {
        this.creatorController = creatorController;
    }
}
