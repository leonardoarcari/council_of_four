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
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

/**
 * Created by Leonardo Arcari on 05/06/2016.
 */
public class PlayerView {

    private ObjectProperty<Player> playerProperty;
    private VBox playerNode;
    private PopOver popOver;

    public PlayerView() {
        playerProperty = new SimpleObjectProperty<>(null);
        setUpPlayerNode();
    }

    private void setUpPlayerNode() {
        playerNode = new VBox(10);
        playerNode.setAlignment(Pos.TOP_CENTER);
        playerNode.setPadding(new Insets(30, 0, 0, 0));

        // Popover
        popOver = new PopOver();
        popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);

        // User info
        GridPane userInfoPane = buildUserInfoPane();

        Circle playerColor = new Circle(20);
        Text username = new Text();
        setUserNameText(username);
        Text nickname = new Text();
        setNickNameText(nickname);

        userInfoPane.add(playerColor, 0, 0, 1, 2);
        userInfoPane.add(username, 1, 0);
        userInfoPane.add(nickname, 1, 1);

        // Player's hand
        GridPane handPane = buildPlayerHand();

        Effect dropShadow = setShadowEffect();

        HBox servantsBox = new HBox(10);
        servantsBox.setAlignment(Pos.CENTER);
        Circle servantsCircle = new Circle(25, Color.BLACK);
        Text servantsNo = new Text();
        setNumberText(servantsNo);
        servantsBox.getChildren().addAll(servantsCircle, servantsNo);

        HBox politicsBox = new HBox(10);
        politicsBox.setAlignment(Pos.CENTER);
        Circle politicsCircle = new Circle(25, Color.BLUE);
        politicsCircle.setOnMouseEntered(event -> politicsCircle.setEffect(dropShadow));
        politicsCircle.setOnMouseExited(event -> politicsCircle.setEffect(null));
        politicsCircle.setOnMouseClicked(event -> popOver.show(politicsCircle, 10));
        Text politicsNo = new Text();
        setNumberText(politicsNo);
        politicsBox.getChildren().addAll(politicsCircle, politicsNo);

        HBox permitBox = new HBox(10);
        permitBox.setAlignment(Pos.CENTER);
        Circle permitCircle = new Circle(25, Color.DARKGREEN);
        permitCircle.setOnMouseEntered(event -> permitCircle.setEffect(dropShadow));
        permitCircle.setOnMouseExited(event -> permitCircle.setEffect(null));
        permitCircle.setOnMouseClicked(event -> popOver.show(permitCircle, 10));
        Text permitsNo = new Text();
        setNumberText(permitsNo);
        permitBox.getChildren().addAll(permitCircle, permitsNo);

        HBox royalBox = new HBox(10);
        royalBox.setAlignment(Pos.CENTER);
        Circle royalCircle = new Circle(25, Color.RED);
        royalCircle.setOnMouseEntered(event -> royalCircle.setEffect(dropShadow));
        royalCircle.setOnMouseExited(event -> royalCircle.setEffect(null));
        royalCircle.setOnMouseClicked(event -> popOver.show(royalCircle, 10));
        Text royalNo = new Text();
        setNumberText(royalNo);
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

    private void setNumberText (Text text) {
        text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, 30));
    }

    private void setUserNameText (Text text) {
        text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, text.getFont().getSize()));
    }

    private void setNickNameText (Text text) {
        text.setFont(Font.font(text.getFont().getFamily(), FontPosture.ITALIC, text.getFont().getSize()));
    }

    private Effect setShadowEffect() {
        DropShadow borderglow = new DropShadow();
        borderglow.setColor(Color.YELLOW);
        borderglow.setWidth(70);
        borderglow.setHeight(70);
        borderglow.setBlurType(BlurType.GAUSSIAN);
        return borderglow;
    }

    private GridPane buildUserInfoPane() {
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
        return userInfoPane;
    }

    private GridPane buildPlayerHand() {
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
        return handPane;
    }
}
