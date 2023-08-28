package com.battlecity.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.battlecity.Resources;
import java.io.IOException;

public class CreatorController {
    private final int[] tankCount = new int[5];
    private final Label[] counts = new Label[5];
    private GameController game;
    private boolean destroyMode;
    private boolean buildMode;
    private boolean usualMode = true;
    private Image block;

    public Label count1;
    public Label count2;
    public Label count3;
    public Label count4;
    public Label count5;
    public GridPane gridPane;
    public Button changeCount1;
    public Button changeCount2;
    public Button changeCount3;
    public Button changeCount4;
    public Button changeCount5;
    public Button brick;
    public Button leaf;
    public Button solid;
    public Button water;
    public Button ice;
    public Pane root;

    void initData(GameController game) {
        this.game = game;
    }

    @FXML
    private void initialize() {
        counts[0] = count1;
        counts[1] = count2;
        counts[2] = count3;
        counts[3] = count4;
        counts[4] = count5;

        for (int i = 0; i < 13; ++i) {
            for (int j = 0; j < 13; ++j) {
                ImageView imageView = new ImageView();
                gridPane.getChildren().add(imageView);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                GridPane.setConstraints(imageView, j, i);

                if (j == 6 && i == 12) {
                    imageView.setImage(Resources.base);
                }

                if (!(j == 3 && i == 12) && !(j == 0 && i == 3) && !(j == 2 && i == 0) && !(j == 6 && i == 0) &&
                        !(j == 10 && i == 0) && !(j == 12 && i == 3) && !(j == 6 && i == 12)) {
                    root.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                        boolean inBounds = imageView.getBoundsInParent().contains(
                                e.getSceneX() - 50, e.getSceneY() - 50);

                        if (!usualMode && inBounds) {
                            if (destroyMode) {
                                imageView.setImage(null);
                            } else {
                                if (buildMode) {
                                    imageView.setImage(block);
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public void close(ActionEvent actionEvent) throws IOException {
        // create popup scene
        Stage popup = new Stage();
        popup.setAlwaysOnTop(true);
        popup.initModality(Modality.WINDOW_MODAL);
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setResizable(false);
        popup.initOwner(count1.getScene().getWindow());

        // load fxml scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.ExitView));
        loader.<ExitController>getController().initData(this);
        Parent root = loader.load();

        // set fxml scene
        Scene scene = new Scene(root, 365, 179);
        popup.setScene(scene);
        popup.show();
    }

    public void takeBlock(ActionEvent actionEvent) {
        gridPane.setGridLinesVisible(true);
        destroyMode = false;
        usualMode = false;
        buildMode = true;

        Button block = (Button) actionEvent.getSource();
        this.block = (block == brick) ? Resources.brickWall
                : (block == leaf) ? Resources.leaf
                : (block == solid) ? Resources.solid
                : (block == water) ? Resources.water
                : (block == ice) ? Resources.ice : null;
    }

    public void destroy(ActionEvent actionEvent) {
        buildMode = false;
        usualMode = false;
        destroyMode = true;
        gridPane.setGridLinesVisible(true);
        changeCount1.setText("-");
        changeCount2.setText("-");
        changeCount3.setText("-");
        changeCount4.setText("-");
        changeCount5.setText("-");
    }

    public void usual(ActionEvent actionEvent) {
        buildMode = false;
        usualMode = true;
        destroyMode = false;
        block = null;
        gridPane.setGridLinesVisible(false);
        changeCount1.setText("+");
        changeCount2.setText("+");
        changeCount3.setText("+");
        changeCount4.setText("+");
        changeCount5.setText("+");
    }

    public void changeCountTanks(ActionEvent actionEvent) {
        Button countButton = (Button) actionEvent.getSource();
        int countId = Integer.parseInt(String.valueOf(countButton.getId().charAt(11)));
        Label count = counts[countId - 1];
        if (destroyMode) {
            reduceTank(count);
        } else {
            addTank(count);
        }
    }

    private void addTank(Label label) {
        int count = 0;
        setCountTanks();

        for (int i : tankCount) {
            count += i;
        }

        if (count < 20) {
            label.setText(String.valueOf(Integer.parseInt(label.getText()) + 1));
        }
    }

    private void reduceTank(Label count) {
        if (Integer.parseInt(count.getText()) > 0) {
            count.setText(String.valueOf(Integer.parseInt(count.getText()) - 1));
        }
    }

    private void setCountTanks() {
        tankCount[0] = Integer.parseInt(count1.getText());
        tankCount[1] = Integer.parseInt(count2.getText());
        tankCount[2] = Integer.parseInt(count3.getText());
        tankCount[3] = Integer.parseInt(count4.getText());
        tankCount[4] = Integer.parseInt(count5.getText());
    }

    public void start(ActionEvent actionEvent) {
        setCountTanks();
        game.startGame(gridPane, tankCount);
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();
    }
}
