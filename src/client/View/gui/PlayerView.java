package client.View.gui;

import core.gamemodel.*;
import core.gamemodel.modelinterface.PlayerInterface;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import java.util.*;

/**
 * A <code>PlayerView</code> shows the state of model's {@link core.Player Player} representing this client.
 * This displays player's username, nickname, color, permitcards, politicscards, servants, royalcards and other bonus
 * cards.
 */
public class PlayerView {
    private VBox playerNode;
    private PopOver popOver;
    private ScrollPane permitScroll;
    private HBox permitBox;
    private ScrollPane royalScroll;
    private HBox royalBox;
    private ScrollPane politicsScroll;
    private HBox politicsBox;
    private ScrollPane otherPane;
    private HBox otherCards;

    private PlayerInterface playerInterface;

    private Text username;
    private Text nickname;
    private BoxInfo servants;
    private BoxInfo politics;
    private BoxInfo permits;
    private BoxInfo royals;
    private Circle playerColor;

    private Map<String,Image> cardImages;

    /**
     * Initializes a PlayerView
     */
    public PlayerView() {
        permitScroll = new ScrollPane();
        permitScroll.setPrefViewportWidth(200);
        permitBox = new HBox(5);
        permitBox.setPadding(new Insets(10.0));
        permitScroll.setFitToHeight(true);

        royalScroll = new ScrollPane();
        royalScroll.setPrefViewportWidth(200);
        royalBox = new HBox(5);
        royalBox.setPadding(new Insets(10.0));
        royalScroll.setFitToHeight(true);

        politicsScroll = new ScrollPane();
        politicsScroll.setPrefViewportWidth(200);
        politicsBox = new HBox(5);
        politicsBox.setPadding(new Insets(10.0));
        politicsScroll.setContent(politicsBox);
        politicsScroll.setFitToHeight(true);
        politicsScroll.prefViewportHeightProperty().bind(politicsBox.heightProperty());

        cardImages = new HashMap<>();
        loadImages();
        setUpPlayerNode();
    }

    private void loadImages() {
        Image fifthRoyal = ImagesMaps.getInstance().getRoyal("fifth");
        Image fourthRoyal = ImagesMaps.getInstance().getRoyal("fourth");
        Image thirdRoyal = ImagesMaps.getInstance().getRoyal("third");
        Image secondRoyal = ImagesMaps.getInstance().getRoyal("second");
        Image firstRoyal = ImagesMaps.getInstance().getRoyal("first");
        Image whitePolitics = ImagesMaps.getInstance().getPolitics("WHITE");
        Image blackPolitics = ImagesMaps.getInstance().getPolitics("BLACK");
        Image orangePolitics = ImagesMaps.getInstance().getPolitics("ORANGE");
        Image pinkPolitics = ImagesMaps.getInstance().getPolitics("PINK");
        Image cyanPolitics = ImagesMaps.getInstance().getPolitics("CYAN");
        Image purplePolitics = ImagesMaps.getInstance().getPolitics("PURPLE");
        Image rainbowPolitics = ImagesMaps.getInstance().getPolitics("RAINBOW");
        cardImages.put("3",fifthRoyal);
        cardImages.put("7",fourthRoyal);
        cardImages.put("12",thirdRoyal);
        cardImages.put("18",secondRoyal);
        cardImages.put("25",firstRoyal);
        cardImages.put("WHITE",whitePolitics);
        cardImages.put("BLACK",blackPolitics);
        cardImages.put("ORANGE",orangePolitics);
        cardImages.put("PINK",pinkPolitics);
        cardImages.put("CYAN",cyanPolitics);
        cardImages.put("PURPLE",purplePolitics);
        cardImages.put("RAINBOW",rainbowPolitics);
    }

    private void setUpPlayerNode() {
        ClassLoader loader = this.getClass().getClassLoader();
        playerNode = new VBox(10);
        playerNode.setAlignment(Pos.TOP_CENTER);
        playerNode.setPadding(new Insets(20, 0, 0, 0));
        // Popover
        popOver = new PopOver();
        popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);

        // User info
        GridPane userInfoPane = buildUserInfoPane();

        playerColor = new Circle(20);
        username = new Text("N/A");
        setUserNameText(username);
        nickname = new Text("N/A");
        setNickNameText(nickname);

        userInfoPane.add(playerColor, 0, 0, 1, 2);
        userInfoPane.add(username, 1, 0);
        userInfoPane.add(nickname, 1, 1);

        // Player's hand
        GridPane handPane = buildPlayerHand();

        HBox servantsBox = new HBox(10);
        servantsBox.setAlignment(Pos.CENTER);
        servants = new BoxInfo(new Image(loader.getResourceAsStream("BonusImages/hireservant_1.png")),new Text("N/A"));
        servantsBox.getChildren().addAll(servants.getBoxImageView(), servants.getBoxText());

        HBox politicsBox = new HBox(10);
        politicsBox.setAlignment(Pos.CENTER);
        politics = new BoxInfo(new Image(loader.getResourceAsStream("BonusImages/drawpoliticscard_1.png")),new Text("N/A"));
        politics.setPopOverContent(politicsScroll);
        politicsBox.getChildren().addAll(politics.getBoxImageView(), politics.getBoxText());

        HBox permitBox = new HBox(10);
        permitBox.setAlignment(Pos.CENTER);
        permits = new BoxInfo(new Image(loader.getResourceAsStream("permitCardIcon.png")), new Text("N/A"));
        permits.setPopOverContent(permitScroll);
        permitBox.getChildren().addAll(permits.getBoxImageView(), permits.getBoxText());

        HBox royalBox = new HBox(10);
        royalBox.setAlignment(Pos.CENTER);
        royals = new BoxInfo(new Image(loader.getResourceAsStream("royalModel.png")), new Text("N/A"));
        royals.setPopOverContent(royalScroll);
        royalBox.getChildren().addAll(royals.getBoxImageView(), royals.getBoxText());

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
        otherPane = new ScrollPane();
        otherPane.setFitToHeight(true);
        otherCards = new HBox(10);
        otherCards.setAlignment(Pos.CENTER);
        otherCards.setPadding(new Insets(5));

        otherPane.prefViewportWidthProperty().bind(playerNode.widthProperty());

        // Pack VBox
        playerNode.getChildren().addAll(userInfoPane, first, handPane, second, otherPane);

        // Set Listener

    }

    private void setPermitScrollPane(Iterator<PermitCard> permitCardIterator) {
        permitBox.getChildren().clear();
        if(!permitCardIterator.hasNext()) {
            permitBox.getChildren().add(new Button("No Permit Card"));
        } else {
            while(permitCardIterator.hasNext()) {
                PermitCard currentPermit = permitCardIterator.next();
                PermitCardView currentView = new PermitCardView(null,0,0,0);
                currentView.setPermitCard(currentPermit);
                currentView.setFitHeight(80);
                permitBox.getChildren().add(currentView);
            }
        }
        permitScroll.setContent(permitBox);
    }

    private void setRoyalScrollPane(Iterator<RoyalCard> royalCardIterator) {
        royalBox.getChildren().clear();
        if(!royalCardIterator.hasNext()) {
            royalBox.getChildren().add(new Label("No Royal Card"));
        } else
            while(royalCardIterator.hasNext()) {
                RoyalCard currentRoyal = royalCardIterator.next();
                Image myRoyal = cardImages.get(String.valueOf(currentRoyal.getRoyalBonus().getValue()));
                ObjectImageView currentView = new ObjectImageView(myRoyal,0,0,0);
                currentView.setFitHeight(80);
                royalBox.getChildren().add(currentView);
            }
        royalScroll.setContent(royalBox);
    }

    private void setPoliticScrollPane(Iterator<PoliticsCard> politicsCardIterator) {
        politicsBox.getChildren().clear();
        if(!politicsCardIterator.hasNext()) {
            politicsBox.getChildren().add(new Label("No Politics Card"));
        } else {
            List<PoliticsCard> tempList = new Vector<>();
            while (politicsCardIterator.hasNext()) {
                PoliticsCard currentPolitic = politicsCardIterator.next();
                tempList.add(currentPolitic);
                Image myPolitic = cardImages.get(currentPolitic.getCardColor().name());
                ObjectImageView currentView = new ObjectImageView(myPolitic, 0, 0, 0);
                currentView.setFitHeight(80);
                politicsBox.getChildren().add(currentView);
            }
            /*CachedData.getInstance().getPlayerPoliticsCards().clear();
            CachedData.getInstance().getPlayerPoliticsCards().addAll(tempList);*/
        }
    }

    private void setOtherCards(Iterator<RegionCard> regionCardIterator, Iterator<TownTypeCard> townTypeCardIterator, HBox otherCards) {
        otherCards.getChildren().clear();
        while(regionCardIterator.hasNext()) {
            ImageView myView = new ImageView(ImagesMaps.getInstance().getRegionBonus(regionCardIterator.next().getType()));
            myView.setPreserveRatio(true);
            myView.setFitHeight(80);
            otherCards.getChildren().add(myView);
        }
        while(townTypeCardIterator.hasNext()) {
            ImageView myView = new ImageView(ImagesMaps.getInstance().getTownTypeBonus(townTypeCardIterator.next().getTownType()));
            myView.setPreserveRatio(true);
            myView.setFitHeight(80);
            otherCards.getChildren().add(myView);
        }
        otherPane.setContent(otherCards);
    }

    public Node getPlayerNode() {
        return playerNode;
    }

    /**
     * @return A reference to the <code>PlayerInterface</code> instance representing this playing client
     */
    public PlayerInterface getPlayer() {
        return playerInterface;
    }

    /**
     * @param player <code>PlayerInterface</code> to show all details of
     */
    public void setPlayer(PlayerInterface player) {
        if (player != null) {
            playerInterface = player;
            playerColor.setFill(player.getColor());
            username.setText(player.getUsername().isEmpty() ? "N/A" : player.getUsername());
            nickname.setText(player.getNickname().isEmpty() ? "N/A" : player.getNickname());
            servants.getBoxText().setText(String.valueOf(player.getServantsNumber()));
            politics.getBoxText().setText(String.valueOf(player.getPoliticsCardsNumber()));
            permits.getBoxText().setText(String.valueOf(player.getPermitCardsNumber()));
            royals.getBoxText().setText(String.valueOf(player.getRoyalCardsNumber()));
        }

        setPermitScrollPane(player.permitCardIterator());
        setRoyalScrollPane(player.royalCardIterator());
        setPoliticScrollPane(player.politicsCardIterator());
        setOtherCards(player.regionCardIterator(),player.townCardIterator(), otherCards);
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

    private class BoxInfo {
        private ImageView boxImageView;
        private Text boxText;
        private ScrollPane popOverContent;

        BoxInfo(Image image, Text text) {
            boxImageView = new ImageView(image);
            boxText = text;
            popOverContent = new ScrollPane();
            setUp();
        }

        private void setUp(){
            boxImageView.setPreserveRatio(true);
            boxImageView.setFitHeight(50);
            boxImageView.setOnMouseEntered(event -> boxImageView.setEffect(setShadowEffect()));
            boxImageView.setOnMouseExited(event -> boxImageView.setEffect(null));
            setNumberText(boxText);
        }

        public void setPopOverContent(ScrollPane popOverContent) {
            this.popOverContent = popOverContent;

            boxImageView.setOnMouseClicked(event -> {
                popOver.setContentNode(this.popOverContent);
                popOver.show(boxImageView, 10);
            });
        }

        public ImageView getBoxImageView() {
            return boxImageView;
        }

        public Text getBoxText() {
            return boxText;
        }
    }
}
