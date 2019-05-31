package controller;

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

import java.io.IOException;

public class ControllerCreator {
    public Label count1;
    public Label count2;
    public Label count3;
    public Label count4;
    public Label count5;
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
    public GridPane gridPane;
    private Image block;
    private boolean destoyMode;
    private boolean buildMode;
    private boolean usualMode = true;
    private ControllerGame game;
    private int[] countTanks = new int[5];

    void initData(ControllerGame game) {
        this.game = game;
    }

    @FXML
    private void initialize() {
        for (int i = 0; i < 13; ++i) {
            for (int j = 0; j < 13; ++j) {
                ImageView imageView = new ImageView();
                gridPane.getChildren().add(imageView);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                GridPane.setConstraints(imageView, j, i);
                if (j == 6 && i == 12) {
                    imageView.setImage(ControllerGame.baseImage);
                }
                if (!(j == 3 && i == 12) && !(j == 0 && i == 3) && !(j == 2 && i == 0) && !(j == 6 && i == 0) && !(j == 10 && i == 0) && !(j == 12 && i == 3) && !(j == 6 && i == 12)) {
                    root.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                        if (!usualMode && imageView.getBoundsInParent().contains(e.getSceneX() - 50, e.getSceneY() - 50)) {
                            if (destoyMode) {
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
        Stage popup = new Stage();
        popup.setAlwaysOnTop(true);
        popup.initModality(Modality.WINDOW_MODAL);
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setResizable(false);
        popup.initOwner(count1.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/exit.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 365, 179);
        loader.<ControllerExit>getController().initData(this);
        popup.setScene(scene);
        popup.show();
    }

    public void takeBlock(ActionEvent actionEvent) {
        buildMode = true;
        usualMode = false;
        destoyMode = false;
        gridPane.setGridLinesVisible(true);
        Button block = (Button) actionEvent.getSource();
        if (block == brick) {
            this.block = ControllerGame.brickWallImage;
        }
        if (block == leaf) {
            this.block = ControllerGame.leafImage;
        }
        if (block == solid) {
            this.block = ControllerGame.solidImage;
        }
        if (block == water) {
            this.block = ControllerGame.waterImage;
        }
        if (block == ice) {
            this.block = ControllerGame.iceImage;
        }
    }

    public void destroy(ActionEvent actionEvent) {
        buildMode = false;
        usualMode = false;
        destoyMode = true;
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
        destoyMode = false;
        block = null;
        gridPane.setGridLinesVisible(false);
        changeCount1.setText("+");
        changeCount2.setText("+");
        changeCount3.setText("+");
        changeCount4.setText("+");
        changeCount5.setText("+");
    }

    public void changeCountTanks(ActionEvent actionEvent) {
        int num = Integer.parseInt(String.valueOf(((Button) actionEvent.getSource()).getId().charAt(11)));
        switch (num) {
            case (1):
                if (destoyMode) {
                    reduceTank(count1);
                } else {
                    addTank(count1);
                }
                break;
            case (2):
                if (destoyMode) {
                    reduceTank(count2);
                } else {
                    addTank(count2);
                }
                break;
            case (3):
                if (destoyMode) {
                    reduceTank(count3);
                } else {
                    addTank(count3);
                }
                break;
            case (4):
                if (destoyMode) {
                    reduceTank(count4);
                } else {
                    addTank(count4);
                }
                break;
            case (5):
                if (destoyMode) {
                    reduceTank(count5);
                } else {
                    addTank(count5);
                }
                break;
        }
    }

    private void addTank(Label label) {
        int count = 0;
        setCountTanks();
        for (int i : countTanks) {
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
        countTanks[0] = Integer.parseInt(count1.getText());
        countTanks[1] = Integer.parseInt(count2.getText());
        countTanks[2] = Integer.parseInt(count3.getText());
        countTanks[3] = Integer.parseInt(count4.getText());
        countTanks[4] = Integer.parseInt(count5.getText());
    }

    public void start(ActionEvent actionEvent) {
        setCountTanks();
        game.startGame(gridPane, countTanks);
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();
    }
}
