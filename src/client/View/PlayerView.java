package client.View;

import core.Player;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * Created by Leonardo Arcari on 05/06/2016.
 */
public class PlayerView {

    private ObjectProperty<Player> playerProperty;
    private VBox playerNode;

    public PlayerView() {
        playerProperty = new SimpleObjectProperty<>(null);
        setUpPlayerNode();
    }

    private void setUpPlayerNode() {
        playerNode = new VBox(10);
        playerNode.setAlignment(Pos.CENTER);
        playerNode.setFillWidth(true);

        // User info
        GridPane userInfoPane = new GridPane();

        ColumnConstraints c0 = new ColumnConstraints();
        c0.setPercentWidth(20);
        c0.setHalignment(HPos.LEFT);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(80);
        c1.setFillWidth(true);
        c1.setHgrow(Priority.ALWAYS);
        c1.setHalignment(HPos.CENTER);

        userInfoPane.getColumnConstraints().addAll(c0, c1);

        Circle playerColor = new Circle(20);
        Text username = new Text();
        Text nickname = new Text();

        userInfoPane.add(playerColor, 0, 0, 1, 2);
        userInfoPane.add(username, 1, 0);
        userInfoPane.add(nickname, 1, 1);

        // Player's hand
        TilePane handPane = new TilePane(Orientation.HORIZONTAL);
        handPane.setPrefColumns(2);
        handPane.setTileAlignment(Pos.CENTER);

        HBox servantsBox = new HBox(10);
        servantsBox.setAlignment(Pos.CENTER);
        Circle servantsCircle = new Circle(10, Color.BLACK);
        Text servantsNo = new Text();
        servantsBox.getChildren().addAll(servantsCircle, servantsNo);

        HBox politicsBox = new HBox(10);
        politicsBox.setAlignment(Pos.CENTER);
        Circle politicsCircle = new Circle(10, Color.BLUE);
        Text politicsNo = new Text();
        servantsBox.getChildren().addAll(politicsCircle, politicsNo);

        HBox permitBox = new HBox(10);
        permitBox.setAlignment(Pos.CENTER);
        Circle permitCircle = new Circle(10, Color.DARKGREEN);
        Text permitsNo = new Text();
        servantsBox.getChildren().addAll(permitCircle, permitsNo);

        HBox royalBox = new HBox(10);
        royalBox.setAlignment(Pos.CENTER);
        Circle royalCircle = new Circle(10, Color.RED);
        Text royalNo = new Text();
        servantsBox.getChildren().addAll(royalCircle, royalNo);

        handPane.getChildren().addAll(servantsBox, politicsBox, permitBox, royalBox);

        // Separators
        Separator first = new Separator(Orientation.HORIZONTAL);
        first.setPadding(new Insets(5, 10, 5, 10));
        Separator second = new Separator(Orientation.HORIZONTAL);
        second.setPadding(new Insets(5, 10, 5, 10));

        // Other cards
        ScrollPane otherPane = new ScrollPane();
        HBox otherCards = new HBox(10);
        otherCards.setAlignment(Pos.CENTER);
        otherCards.setPadding(new Insets(5));

        otherPane.prefViewportHeightProperty().bind(otherCards.heightProperty());
        otherPane.prefViewportWidthProperty().bind(playerNode.widthProperty());

        // Pack VBox
        playerNode.getChildren().addAll(userInfoPane, first, handPane, second, otherPane);

        // Set Listener
        playerProperty.addListener((observable, oldValue, newValue) -> {
            playerColor.setFill(newValue.getColor());
            username.setText(newValue.getUsername().isEmpty() ? "N/A" : newValue.getUsername());
            nickname.setText(newValue.getNickname().isEmpty() ? "N/A" : newValue.getNickname());
            servantsNo.setText(String.valueOf(newValue.getServantsNumber()));
            politicsNo.setText(String.valueOf(newValue.getPoliticsCardsNumber()));
            permitsNo.setText(String.valueOf(newValue.getPermitCardsNumber()));
            royalNo.setText(String.valueOf(newValue.getRoyalCardsNumber()));

            //TODO: Add PopOvers and ScrollPane Cards views
        });
    }
}
