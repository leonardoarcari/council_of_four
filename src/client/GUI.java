package client;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo Arcari on 31/05/2016.
 */
public class GUI extends Application {
    private GridPane gridPane;
    private ImageView gameboardIV;
    private AnchorPane boardAnchor;

    private List<ObjectImageView> boardObjects;

    private final static int GAMEBOARD_HEIGHT = 700;
    private final static float GAMEBOARD_WIDTH = GAMEBOARD_HEIGHT * 72f/61;

    @Override
    public void init() throws Exception {
        super.init();
        boardObjects = new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        gridPane = new GridPane();

        ColumnConstraints boardColumn = new ColumnConstraints();
        boardColumn.setFillWidth(true);
        ColumnConstraints chatColumn = new ColumnConstraints();
        chatColumn.setHgrow(Priority.ALWAYS);
        chatColumn.setHalignment(HPos.CENTER);

        gridPane.getColumnConstraints().addAll(boardColumn, chatColumn);

        RowConstraints chatRow = new RowConstraints();
        chatRow.setPercentHeight(66);
        RowConstraints handRow = new RowConstraints();
        handRow.setPercentHeight(34);

        gridPane.getRowConstraints().addAll(chatRow, handRow);

        boardAnchor = new AnchorPane();

        // Load GameBoard imageview
        Image gameBoardImage = new Image(new FileInputStream("src/client/gameboard.png"));
        gameboardIV = new ImageView(gameBoardImage);
        gameboardIV.setSmooth(true);
        gameboardIV.setCache(true);
        gameboardIV.setPreserveRatio(true);
        gameboardIV.fitHeightProperty().bind(primaryStage.heightProperty());

        // Town IVs
        Image gImage = new Image(new FileInputStream("src/client/g.png"));
        boardObjects.add(new ObjectImageView(gImage, 0.3948916963480114, 0.2457627118644068, 0.10195385614803205));

        // Add Nodes to anchorPane
        boardAnchor.getChildren().add(gameboardIV);
        boardAnchor.getChildren().addAll(boardObjects);

        // Chat column Nodes
        Button dummyChat = new Button("I'm a dummy chat button");
        Button dummyHand = new Button("I'm a dummy hand button");

        // Add Nodes to gridPane
        GridPane.setConstraints(boardAnchor, 0, 0, 1, 2);
        GridPane.setConstraints(dummyChat, 1, 0, 1, 1);
        GridPane.setConstraints(dummyHand, 1, 1, 1, 1);
        gridPane.getChildren().addAll(boardAnchor, dummyChat, dummyHand);

        // Scene & Stage setup
        Scene scene = new Scene(gridPane, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set listeners
        boardObjects.forEach(objectImageView -> {
            setImageViewListener(objectImageView);
            setObjectConstraints(objectImageView);
        });

        // Debug
        gridPane.setGridLinesVisible(true);
        gameboardIV.setOnMouseClicked(event -> System.out.println("X scaled: " +
                event.getX()/gameboardIV.getBoundsInParent().getWidth() + " Y scaled " +
                event.getY()/ gameboardIV.getBoundsInParent().getHeight())
        );
    }

    private void setImageViewListener(ObjectImageView iv) {
        gameboardIV.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            iv.setFitWidth(iv.getWidth() * newValue.getWidth());
        });

        boardAnchor.heightProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setTopAnchor(iv, iv.getTopY() * gameboardIV.getBoundsInParent().getHeight());
            AnchorPane.setLeftAnchor(iv, iv.getLeftX() * gameboardIV.getBoundsInParent().getWidth());
        });
    }

    private void setObjectConstraints(ObjectImageView iv) {
        iv.setFitWidth(iv.getWidth() * gameboardIV.getBoundsInParent().getWidth());
        AnchorPane.setTopAnchor(iv, iv.getTopY() * gameboardIV.getBoundsInParent().getHeight());
        AnchorPane.setLeftAnchor(iv, iv.getLeftX() * gameboardIV.getBoundsInParent().getWidth());
    }
}
