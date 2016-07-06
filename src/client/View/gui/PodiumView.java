package client.View.gui;

import client.CachedData;
import core.Player;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

/**
 * A <code>PodiumView</code> displays the podium at the end of the game. The podium shows the best players of the game
 * and their achievements in this match. Thus, victory points, position on nobility path and on wealth path are
 * represented.
 */
public class PodiumView extends ScrollPane {
    private Map<Integer, List<Player>> podium;
    private VBox podiumShow;

    /**
     * Initializes <code>PodiumView</code> with the input podium
     * @param podium Map representing the podium. Keys are the positions on the podium and values are lists of players
     * at <code>'key'</code> position.
     */
    public PodiumView(Map<Integer, List<Player>> podium) {
        this.podium = podium;
        podiumShow = new VBox(5);
        buildPodiumShow();
        ShowPane.getInstance().setTitle("Results");
    }

    private void buildPodiumShow() {
        podiumShow.setPadding(new Insets(10));
        podiumShow.setAlignment(Pos.CENTER);

        for(int index : podium.keySet()) {
            List<Player> players = podium.get(index);
            for(Player player : players) {
                buildPlayerInfoGrid(player, index);
            }
        }

        podiumShow.setPadding(new Insets(0,250f,10f,250f));
        setContent(podiumShow);
    }

    private void buildPlayerInfoGrid(Player player, int position) {
        GridPane podiumGrid = new GridPane();
        setGridConstraints(podiumGrid);

        //Position label
        Label positionLabel = new Label(position + numberSuffix(position) + " place to: ");
        GridPane.setConstraints(positionLabel,0, 0, 3, 1, HPos.LEFT, VPos.CENTER);
        positionLabel.setFont(CachedData.getInstance().getCustomFont());
        positionLabel.setPadding(new Insets(10,10,10,0));
        if(position == 1) positionLabel.setTextFill(Color.GOLD);
        else positionLabel.setTextFill(Color.SILVER);

        //Color box
        VBox colorBox = new VBox();
        colorBox.setPadding(new Insets(5));
        colorBox.setFillWidth(true);
        colorBox.setAlignment(Pos.CENTER);
        Circle circle = new Circle(30, player.getColor());
        colorBox.getChildren().add(circle);
        GridPane.setConstraints(colorBox,0, 1, 1, 3, HPos.CENTER, VPos.CENTER);
        colorBox.setStyle("-fx-border-width: 2px; -fx-border-color: black");

        //Player info node
        VBox infoBox = new VBox(50);
        infoBox.setPadding(new Insets(5));
        infoBox.setAlignment(Pos.CENTER);
        Text label = new Text("Player nickname: ");
        label.setFont(CachedData.getInstance().getCustomFont());
        label.setFill(player.getColor());
        Text label2 = new Text(player.getNickname());
        label2.setFont(CachedData.getInstance().getCustomFont());
        infoBox.getChildren().addAll(label, label2);
        GridPane.setConstraints(infoBox,1, 1, 1, 3, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        infoBox.setStyle("-fx-border-width: 2px; -fx-border-color: black");

        //Player points nodes
        HBox coinBox = buildCoinBox(player);
        GridPane.setConstraints(coinBox, 2, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        coinBox.setPadding(new Insets(10));
        coinBox.setStyle("-fx-border-width: 2px; -fx-border-color: black");

        HBox nobilityBox = buildNobilityBox(player);
        GridPane.setConstraints(nobilityBox, 2, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        nobilityBox.setPadding(new Insets(10));
        nobilityBox.setStyle("-fx-border-width: 2px; -fx-border-color: black");

        HBox victoryBox = buildVictoryBox(player);
        GridPane.setConstraints(victoryBox, 2, 3, 1, 1, HPos.CENTER, VPos.CENTER);
        victoryBox.setPadding(new Insets(10));
        victoryBox.setStyle("-fx-border-width: 3px; -fx-border-color: black");

        podiumGrid.getChildren().addAll(positionLabel,colorBox,infoBox,coinBox,nobilityBox,victoryBox);

        podiumGrid.setAlignment(Pos.CENTER);
        podiumShow.getChildren().add(podiumGrid);
        setFitToWidth(true);
        setFitToHeight(true);
    }

    private void setGridConstraints(GridPane gridPane) {
        ColumnConstraints colorColumn = new ColumnConstraints();
        colorColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints playerNicknameColumn = new ColumnConstraints();
        playerNicknameColumn.setHgrow(Priority.ALWAYS);
        playerNicknameColumn.setHalignment(HPos.CENTER);
        ColumnConstraints pointsColumn = new ColumnConstraints();
        pointsColumn.setHalignment(HPos.CENTER);

        gridPane.getColumnConstraints().addAll(colorColumn, playerNicknameColumn, pointsColumn);

        RowConstraints positionRow = new RowConstraints();
        positionRow.setValignment(VPos.CENTER);
        RowConstraints coinRow = new RowConstraints();
        coinRow.setValignment(VPos.CENTER);
        coinRow.setFillHeight(true);
        RowConstraints nobilityRow = new RowConstraints();
        nobilityRow.setValignment(VPos.CENTER);
        coinRow.setFillHeight(true);
        RowConstraints victoryRow = new RowConstraints();
        victoryRow.setValignment(VPos.CENTER);
        coinRow.setFillHeight(true);

        gridPane.getRowConstraints().addAll(positionRow, coinRow, nobilityRow, victoryRow);
        // turn layout pixel snapping off on the grid so that grid lines will be an even width.
        gridPane.setSnapToPixel(false);
    }

    private HBox buildCoinBox(Player player) {
        ClassLoader loader = this.getClass().getClassLoader();

        HBox coinBox = new HBox(5);
        coinBox.setAlignment(Pos.CENTER);
        ImageView coinIcon = new ImageView(new Image(loader.getResourceAsStream("PointSymbols/coin.png")));
        coinIcon.setPreserveRatio(true);
        coinIcon.setFitHeight(50);
        Label coinLabel = new Label(String.valueOf(CachedData.getInstance().getWealthPath().getPlayerPosition(player)));
        coinLabel.setFont(CachedData.getInstance().getCustomFont());
        coinBox.getChildren().addAll(coinLabel,coinIcon);

        return coinBox;
    }

    private HBox buildNobilityBox(Player player) {
        ClassLoader loader = this.getClass().getClassLoader();

        HBox nobilityBox = new HBox(5);
        nobilityBox.setAlignment(Pos.CENTER);
        ImageView nobilityIcon = new ImageView(new Image(loader.getResourceAsStream("BonusImages/nobilitypoint_1.png")));
        nobilityIcon.setPreserveRatio(true);
        nobilityIcon.setFitHeight(50);
        Label nobilityLabel = new Label(String.valueOf(CachedData.getInstance().getNobilityPath().getPlayerPosition(player)));
        nobilityLabel.setFont(CachedData.getInstance().getCustomFont());
        nobilityBox.getChildren().addAll(nobilityLabel,nobilityIcon);

        return nobilityBox;
    }

    private HBox buildVictoryBox(Player player) {
        ClassLoader loader = this.getClass().getClassLoader();

        HBox victoryBox = new HBox(5);
        victoryBox.setAlignment(Pos.CENTER);
        ImageView victoryIcon = new ImageView(new Image(loader.getResourceAsStream("PointSymbols/victory.png")));
        victoryIcon.setPreserveRatio(true);
        victoryIcon.setFitHeight(50);
        Label victoryLabel = new Label(String.valueOf(CachedData.getInstance().getVictoryPath().getPlayerPosition(player)));
        victoryLabel.setFont(CachedData.getInstance().getCustomFont());
        victoryBox.getChildren().addAll(victoryLabel,victoryIcon);

        return victoryBox;
    }

    private String numberSuffix(int index) {
        if(index == 1) return "st";
        else if(index == 2) return "nd";
        else if(index == 3) return "rd";
        else return "th";
    }
}