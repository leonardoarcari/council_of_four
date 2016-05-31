package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;

/**
 * Created by Leonardo Arcari on 31/05/2016.
 */
public class GUI extends Application {
    private GridPane gridPane;
    private final static int GAMEBOARD_HEIGHT = 700;
    private final static float GAMEBOARD_WIDTH = GAMEBOARD_HEIGHT * 72f/61;
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(GAMEBOARD_WIDTH + " /// " + GAMEBOARD_HEIGHT);
        gridPane = new GridPane();
        ColumnConstraints boardColumn = new ColumnConstraints();
        boardColumn.setPercentWidth(80);
        boardColumn.setFillWidth(true);
        ColumnConstraints chatColumn = new ColumnConstraints();
        chatColumn.setPercentWidth(20);
        gridPane.getColumnConstraints().addAll(boardColumn, chatColumn);
        RowConstraints chatRow = new RowConstraints();
        chatRow.setPercentHeight(66);
        RowConstraints handRow = new RowConstraints();
        handRow.setPercentHeight(34);
        gridPane.getRowConstraints().addAll(chatRow, handRow);

        AnchorPane boardAnchor = new AnchorPane();

        // Load GameBoard imageview
        Image gameBoardImage = new Image(new FileInputStream("src/client/gameboard.png"));
        ImageView gameboardIV = new ImageView(gameBoardImage);
        //gameboardIV.setPreserveRatio(true);
        gameboardIV.setSmooth(true);
        gameboardIV.setCache(true);
        gameboardIV.fitHeightProperty().bind(primaryStage.heightProperty());
        gameboardIV.fitWidthProperty().bind(primaryStage.widthProperty().multiply(0.8));


        // Add Nodes to anchorPane
        boardAnchor.getChildren().add(gameboardIV);

        // Chat column Nodes
        Button dummyChat = new Button("I'm a dummy chat button");
        Button dummyHand = new Button("I'm a dummy hand button");

        // Add Nodes to gridPane
        GridPane.setConstraints(boardAnchor, 0, 0, 1, 2);
        GridPane.setConstraints(dummyChat, 1, 0, 1, 1);
        GridPane.setConstraints(dummyHand, 1, 1, 1, 1);
        gridPane.getChildren().addAll(boardAnchor, dummyChat, dummyHand);

        // Scene & Stage setup
        Scene scene = new Scene(gridPane, 1.25*GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Debug
        gridPane.setGridLinesVisible(true);
        gameboardIV.setOnMouseClicked(event -> System.out.println("X scaled: " +
                event.getX()/gameboardIV.getBoundsInParent().getWidth() + " Y scaled " +
                event.getY()/ gameboardIV.getBoundsInParent().getHeight())
        );
    }
}
