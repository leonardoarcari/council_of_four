package client.View;

import core.Player;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

        // User info
        GridPane userInfoPane = new GridPane();
        userInfoPane.setPadding(new Insets(0, 20, 0, 20));

        ColumnConstraints c0 = new ColumnConstraints();
        c0.setPercentWidth(20);
        c0.setHalignment(HPos.LEFT);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(80);
        c1.setFillWidth(true);
        c1.setHgrow(Priority.ALWAYS);
        c1.setHalignment(HPos.LEFT);

        userInfoPane.getColumnConstraints().addAll(c0, c1);

        Circle playerColor = new Circle(20);
        Text username = new Text();
        Text nickname = new Text();

        userInfoPane.add(playerColor, 0, 0, 1, 2);
        userInfoPane.add(username, 1, 0);
        userInfoPane.add(nickname, 1, 1);

        // Player's hand
        GridPane handPane = new GridPane();
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        c2.setHalignment(HPos.CENTER);
        c2.setHgrow(Priority.ALWAYS);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(50);
        c3.setHgrow(Priority.ALWAYS);
        c3.setHalignment(HPos.CENTER);

        handPane.getColumnConstraints().addAll(c2, c3);
        handPane.setVgap(20);

        HBox servantsBox = new HBox(10);
        servantsBox.setAlignment(Pos.CENTER);
        Circle servantsCircle = new Circle(25, Color.BLACK);
        Text servantsNo = new Text();
        servantsBox.getChildren().addAll(servantsCircle, servantsNo);

        HBox politicsBox = new HBox(10);
        politicsBox.setAlignment(Pos.CENTER);
        Circle politicsCircle = new Circle(25, Color.BLUE);
        Text politicsNo = new Text();
        politicsBox.getChildren().addAll(politicsCircle, politicsNo);

        HBox permitBox = new HBox(10);
        permitBox.setAlignment(Pos.CENTER);
        Circle permitCircle = new Circle(25, Color.DARKGREEN);
        Text permitsNo = new Text();
        permitBox.getChildren().addAll(permitCircle, permitsNo);

        HBox royalBox = new HBox(10);
        royalBox.setAlignment(Pos.CENTER);
        Circle royalCircle = new Circle(25, Color.RED);
        Text royalNo = new Text();
        royalBox.getChildren().addAll(royalCircle, royalNo);

        handPane.add(servantsBox, 0, 0);
        handPane.add(politicsBox, 1, 0);
        handPane.add(permitBox, 0, 1);
        handPane.add(royalBox, 1, 1);

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
            if (newValue != null) {
                playerColor.setFill(newValue.getColor());
                username.setText(newValue.getUsername().isEmpty() ? "N/A" : newValue.getUsername());
                nickname.setText(newValue.getNickname().isEmpty() ? "N/A" : newValue.getNickname());
                servantsNo.setText(String.valueOf(newValue.getServantsNumber()));
                politicsNo.setText(String.valueOf(newValue.getPoliticsCardsNumber()));
                permitsNo.setText(String.valueOf(newValue.getPermitCardsNumber()));
                royalNo.setText(String.valueOf(newValue.getRoyalCardsNumber()));

                //TODO: Add PopOvers and ScrollPane Cards views
            }
        });
    }

    public Node getPlayerNode() {
        return playerNode;
    }

    public Player getPlayerProperty() {
        return playerProperty.get();
    }

    public ObjectProperty<Player> playerPropertyProperty() {
        return playerProperty;
    }

    public void setPlayerProperty(Player playerProperty) {
        this.playerProperty.set(playerProperty);
    }
}
