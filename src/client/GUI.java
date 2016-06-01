package client;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
        final Glow glow = new Glow(1.0);
        final DropShadow borderglow = new DropShadow();
        borderglow.setColor(Color.WHITE);
        borderglow.setWidth(70);
        borderglow.setHeight(70);
        glow.setInput(borderglow);

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
        Image gameBoardImage = new Image(new FileInputStream("src/client/gameboard_scaled.png"));
        gameboardIV = new ImageView(gameBoardImage);
        gameboardIV.setSmooth(true);
        gameboardIV.setCache(true);
        gameboardIV.setPreserveRatio(true);
        gameboardIV.fitHeightProperty().bind(primaryStage.heightProperty());

        // Town IVs
        Image aImage = new Image(new FileInputStream("src/client/a.png"));
        Image bImage = new Image(new FileInputStream("src/client/b.png"));
        Image cImage = new Image(new FileInputStream("src/client/c.png"));
        Image dImage = new Image(new FileInputStream("src/client/d.png"));
        Image eImage = new Image(new FileInputStream("src/client/e.png"));
        Image fImage = new Image(new FileInputStream("src/client/f.png"));
        Image gImage = new Image(new FileInputStream("src/client/g.png"));
        Image hImage = new Image(new FileInputStream("src/client/h.png"));
        Image iImage = new Image(new FileInputStream("src/client/i.png"));
        Image mImage = new Image(new FileInputStream("src/client/m.png"));
        Image nImage = new Image(new FileInputStream("src/client/n.png"));
        Image oImage = new Image(new FileInputStream("src/client/o.png"));
        boardObjects.add(new ObjectImageView(aImage, 0.07257407407407407, 0.059109289617486336, 0.10459153122197));
        boardObjects.add(new ObjectImageView(bImage, 0.061042592592592594, 0.24180327868852458, 0.114425925925926));
        boardObjects.add(new ObjectImageView(cImage, 0.2155925925925926, 0.11958469945355191, 0.114583333333333));
        boardObjects.add(new ObjectImageView(dImage, 0.2084852504710498, 0.2786885245901639, 0.105321313772738));
        boardObjects.add(new ObjectImageView(eImage, 0.11026557621929187, 0.40846994535519127, 0.102691805475035));
        boardObjects.add(new ObjectImageView(fImage, 0.3811597344042335, 0.0868013698630137, 0.104449317367015));
        boardObjects.add(new ObjectImageView(gImage, 0.3948916963480114, 0.2467627118644068, 0.10285385614803205));
        boardObjects.add(new ObjectImageView(hImage, 0.40973005099866394, 0.3864406779661017, 0.09912460333496036));
        boardObjects.add(new ObjectImageView(iImage, 0.5466258390659746, 0.08813559322033898, 0.0967313203267906));
        boardObjects.add(new ObjectImageView(mImage, 0.6729745030117486, 0.416462482946794, 0.120203003974608));
        boardObjects.add(new ObjectImageView(nImage, 0.82539565232543, 0.16800354706684858, 0.113268215283765));
        boardObjects.add(new ObjectImageView(oImage, 0.829096739437645, 0.3542896174863388, 0.106006559623886));
        boardObjects.forEach(objectImageView -> setObjectGlow(objectImageView, glow));

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

    private void setObjectGlow(ObjectImageView iv, Effect effect) {
        iv.setOnMouseEntered(event -> iv.setEffect(effect));
        iv.setOnMouseExited(event -> iv.setEffect(null));
    }
}
