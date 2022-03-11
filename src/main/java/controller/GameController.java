package controller;

import entity.GrayAITank;
import entity.MainTank;
import entity.Tank;
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
import config.Resource;

import java.net.URL;
import java.util.ArrayList;

public class GameController {
    private final Tank grayTank = new Tank(this);
    private final ArrayList<ImageView> ALSs = new ArrayList<>();

    public ArrayList<ImageView> staticObjects = new ArrayList<>();
    public ArrayList<ImageView> iceArea = new ArrayList<>();
    public ArrayList<Tank> tanks = new ArrayList<>();
    public MainTank goldTank = new MainTank(this);

    public boolean isLaunched = true;
    public boolean isRunning = true;
    public boolean hasWinner = false;
    public int leftAITanks = 1;

    public ImageView baseView;
    private ImageView gameOverView;
    private ImageView winnerView;

    @FXML
    public Pane paneOfGame;
    public Pane root;
    public ImageView blackBackground;
    public ImageView grayBackground;
    public GridPane gridPane;
    public GridPane gridPaneLeafs;
    public MenuBar menuBar;

    public void addChild(ImageView child) {
        paneOfGame.getChildren().add(child);
    }

    public void creator(ActionEvent actionEvent) throws Exception {
        // create popup stage
        Stage popup = new Stage();
        popup.setAlwaysOnTop(true);
        popup.initModality(Modality.WINDOW_MODAL);
        popup.setResizable(false);
        popup.initOwner(paneOfGame.getScene().getWindow());
        popup.initStyle(StageStyle.UNDECORATED);

        // load fxml scene
        URL location = getClass().getResource(Resource.CreatorView);
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        loader.<CreatorController>getController().initData(this);

        // show fxml scene
        Scene scene = new Scene(root, 850, 775);
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
        staticObjects.add(grayTank.getImageView());
        tanks.add(grayTank);

        // put the following objects to front
        grayTank.getImageView().toFront();
        gridPaneLeafs.toFront();

        if (gameOverView != null) {
            gameOverView.toFront();
        } else if (winnerView != null) {
            winnerView.toFront();
        }
    }

    private void setImageview(ImageView imageView, double x, double y, double w, double h) {
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
    }

    public void checkWin() {
        if (!isLaunched) {
            if (tanks.size() == 1 && tanks.get(0) == goldTank && ALSs.size() == 0) {
                hasWinner = true;
                winnerView = new ImageView(new Image(Resource.win));
                paneOfGame.getChildren().add(winnerView);
                winnerView.toFront();
                setImageview(winnerView, 80, 135, 472.5, 350);
            }
        } else {
            // TODO: Start new game.
        }
    }

    public void gameOver() {
        if (!hasWinner) {
            isRunning = false;
            gameOverView = new ImageView(new Image(Resource.gameOver));
            paneOfGame.getChildren().add(gameOverView);
            gameOverView.toFront();
            gameOverView.setX(80);
            gameOverView.setY(150);
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
        ImageView ALS = new ImageView(new Image(Resource.als));
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
        timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(1),
                ev -> Platform.runLater(() -> {
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
        isLaunched = false;
        int count = 0;
        for (int i : countTanks) {
            count += i;
        }

        leftAITanks = count;
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
                Image image = newImageView.getImage();

                GridPane.setConstraints(newImageView, GridPane.getColumnIndex(imageView),
                        GridPane.getRowIndex(imageView));

                // handle non-leaf block
                if (image != Resource.leaf) {
                    this.gridPane.getChildren().add(newImageView);
                } else {
                    gridPaneLeafs.getChildren().add(newImageView);
                }

                // handle base block
                if (image == Resource.base) {
                    baseView = newImageView;
                }

                // handle ice block
                if (image == Resource.ice) {
                    ImageView smallBlock = new ImageView();
                    paneOfGame.getChildren().add(smallBlock);
                    setImageview(smallBlock, GridPane.getColumnIndex(newImageView) * 50 + 12.5,
                            GridPane.getRowIndex(newImageView) * 50 + 12.5, 25, 25);
                    iceArea.add(smallBlock);
                }

                // add to static objects if not a leaf or ice
                if (image != null && image != Resource.leaf && image != Resource.ice) {
                    staticObjects.add(newImageView);
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

        // Initialize player tank
        goldTank.setCoordinates(153, 603, 0, 43);
        goldTank.setImage(Resource.goldTank);
        paneOfGame.getChildren().add(goldTank.getImageView());
        staticObjects.add(goldTank.getImageView());
        tanks.add(goldTank);

        // initialize AI tanks
        grayTank.setCoordinates(200, 425, 0, 43);
        grayTank.setImage(Resource.grayTank);
        paneOfGame.getChildren().add(grayTank.getImageView());
        staticObjects.add(grayTank.getImageView());
        tanks.add(grayTank);
    }
}
