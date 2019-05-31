package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sample.GrayAITank;
import sample.MainTank;
import sample.Tank;

import java.util.ArrayList;

public class ControllerGame {
    @FXML
    public Pane paneOfGame;
    public Pane root;
    public ImageView blackBackground;
    public ImageView grayBackground;
    public GridPane gridPane;
    public GridPane gridPaneLeafs;
    public MenuBar menuBar;
    public ImageView base;
    private ImageView gameOver;
    private ImageView youWin;
    public MainTank goldTank = new MainTank(this);
    private Tank grayTank = new Tank(this);
    public boolean isGame = true;
    public boolean isWin = false;
    public boolean isLaunch = true;
    public int countAlTanksLeft = 1;
    public ArrayList<ImageView> iceArea = new ArrayList<>();
    private ArrayList<ImageView> ALSs = new ArrayList<>();
    public ArrayList<ImageView> dontMoveObjects = new ArrayList<>();
    public ArrayList<Tank> tanks = new ArrayList<>();
    public static Image grayTankImage = new Image("view/pictures/grayTank.png");
    public static Image goldTankImage = new Image("view/pictures/goldTank.png");
    public static Image brickWallImage = new Image("view/pictures/brickWall.png");
    private static Image brickWall2Image = new Image("view/pictures/brickWall2.png");
    private static Image brickWall3Image = new Image("view/pictures/brickWall3.png");
    public static Image solidImage = new Image("view/pictures/solidWall.png");
    public static Image leafImage = new Image("view/pictures/leafWall.png");
    public static Image waterImage = new Image("view/pictures/waterWall.png");
    public static Image iceImage = new Image("view/pictures/iceWall.png");
    public static Image baseImage = new Image("view/pictures/base.png");

    public static Image getBrickWall2Image() {
        return brickWall2Image;
    }

    public static Image getBrickWall3Image() {
        return brickWall3Image;
    }

    public void addChild(ImageView child) {
        paneOfGame.getChildren().add(child);
    }

    public void creator(ActionEvent actionEvent) throws Exception {
        Stage popup = new Stage();
        popup.setAlwaysOnTop(true);
        popup.initModality(Modality.WINDOW_MODAL);
        popup.setResizable(false);
        popup.initOwner(paneOfGame.getScene().getWindow());
        popup.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/creator.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 850, 775);
        loader.<ControllerCreator>getController().initData(this);
        popup.setScene(scene);
        popup.show();
    }

    public void onAction(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.SPACE)) {
            goldTank.tankFire();
        }
    }

    private void createTank(double x, double y, int rotate, double size) {
        GrayAITank grayTank = new GrayAITank(this);
        paneOfGame.getChildren().add(grayTank.getImageView());
        grayTank.setCoordinates(x, y, rotate, size);
        dontMoveObjects.add(grayTank.getImageView());
        tanks.add(grayTank);
        grayTank.getImageView().toFront();
        gridPaneLeafs.toFront();
        if (gameOver != null) {
            gameOver.toFront();
        }
        if (youWin != null) {
            youWin.toFront();
        }
    }

    private void setImageview(ImageView imageView, double x, double y, double w, double h) {
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
    }

    public void checkWin() {
        if (!isLaunch) {
            if (tanks.size() == 1 && tanks.get(0) == goldTank && ALSs.size() == 0) {
                isWin = true;
                youWin = new ImageView(new Image("view/pictures/youWin.png"));
                paneOfGame.getChildren().add(youWin);
                youWin.toFront();
                setImageview(youWin, 80, 135, 472.5, 350);
            }
        } else {
            //startGame();
        }
    }

    public void gameOver() {
        if (!isWin) {
            isGame = false;
            gameOver = new ImageView(new Image("view/pictures/gameOver.png"));
            paneOfGame.getChildren().add(gameOver);
            gameOver.toFront();
            gameOver.setX(80);
            gameOver.setY(150);
        }
    }

    public void setALS(int count) {
        for (int i = ALSs.size(); i > 0; i--) {
            root.getChildren().remove(ALSs.get(i - 1));
            ALSs.remove(i - 1);
        }
        int y = 70;
        for (int i = 0; i < count / 2; i++) {
            createALS(720, y);
            createALS(760, y);
            y += 30;
        }
        if (count % 2 != 0) {
            createALS(720, y);
        }
    }

    private void createALS(double x, double y) {
        ImageView ALS = new ImageView(new Image("view/pictures/ALS.png"));
        root.getChildren().add(ALS);
        ALS.toFront();
        ALS.setFitWidth(29.5);
        ALS.setFitHeight(26.5);
        ALS.setX(x);
        ALS.setY(y);
        ALSs.add(ALS);
    }

    private void beginInvasion(int[] countTanks) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(1), ev -> Platform.runLater(()
                -> {
            int random = (int) (Math.random() * 5);
            if (random == 4 && countTanks[0] != 0) {
                createTank(-50, 153, 90, 43);
                countTanks[0] -= 1;
            }
            random = (int) (Math.random() * 5);
            if (random == 4 && countTanks[1] != 0) {
                createTank(103, -50, 180, 43);
                countTanks[1] -= 1;
            }
            random = (int) (Math.random() * 5);
            if (random == 4 && countTanks[2] != 0) {
                createTank(303, -50, 180, 43);
                countTanks[2] -= 1;
            }
            random = (int) (Math.random() * 5);
            if (random == 4 && countTanks[3] != 0) {
                createTank(503, -50, 180, 43);
                countTanks[3] -= 1;
            }
            random = (int) (Math.random() * 5);
            if (random == 4 && countTanks[4] != 0) {
                createTank(657, 153, 270, 43);
                countTanks[4] -= 1;
            }
        })));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    void startGame(GridPane gridPane, int[] countTanks) {
        isLaunch = false;
        int count = 0;
        for (int i : countTanks) {
            count += i;
        }
        countAlTanksLeft = count;
        setALS(count);
        beginInvasion(countTanks);
        grayTank.removeTank();
        goldTank.setCoordinates(153, 603, 0, 43);
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                ImageView newImageView = new ImageView(imageView.getImage());
                newImageView.setFitHeight(imageView.getFitHeight());
                newImageView.setFitWidth(imageView.getFitHeight());
                GridPane.setConstraints(newImageView, GridPane.getColumnIndex(imageView), GridPane.getRowIndex(imageView));
                if (newImageView.getImage() != leafImage) {
                    this.gridPane.getChildren().add(newImageView);
                } else {
                    gridPaneLeafs.getChildren().add(newImageView);
                }
                if (newImageView.getImage() == baseImage) {
                    base = newImageView;
                }
                if (newImageView.getImage() == iceImage) {
                    ImageView smallBlock = new ImageView();
                    paneOfGame.getChildren().add(smallBlock);
                    setImageview(smallBlock, GridPane.getColumnIndex(newImageView) * 50 + 12.5, GridPane.getRowIndex(newImageView) * 50 + 12.5, 25, 25);
                    iceArea.add(smallBlock);
                }
                if (newImageView.getImage() != null && newImageView.getImage() != leafImage && newImageView.getImage() != iceImage) {
                    dontMoveObjects.add(newImageView);
                }
            }
        }
        gridPane.toBack();
        gridPaneLeafs.toFront();
    }

    @FXML
    private void initialize() {
        grayBackground.toFront();
        menuBar.toFront();
        setALS(1);

        goldTank.setCoordinates(153, 603, 0, 43);
        goldTank.setImage(goldTankImage);
        paneOfGame.getChildren().add(goldTank.getImageView());
        tanks.add(goldTank);
        dontMoveObjects.add(goldTank.getImageView());

        grayTank.setCoordinates(200, 425, 0, 43);
        grayTank.setImage(grayTankImage);
        paneOfGame.getChildren().add(grayTank.getImageView());
        tanks.add(grayTank);
        dontMoveObjects.add(grayTank.getImageView());
    }
}
