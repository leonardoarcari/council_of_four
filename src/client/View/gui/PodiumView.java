package client.View.gui;

import client.CachedData;
import core.Player;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Map;

/**
 * Created by Matteo on 18/06/16.
 */
public class PodiumView extends ScrollPane {
    private Map<Integer, List<Player>> podium;
    private VBox podiumShow;

    public PodiumView(Map<Integer, List<Player>> podium) {
        this.podium = podium;
        podiumShow = new VBox(5);
        buildPodiumShow();
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

        setContent(podiumShow);
    }

    private void buildPlayerInfoGrid(Player player, int position) {
        GridPane podiumGrid = new GridPane();
        podiumGrid.setPrefHeight(180.0);
        setGridConstraints(podiumGrid);

        //Position label
        Label positionLabel = new Label(position + numberSuffix(position) + " place to: ");
        GridPane.setConstraints(positionLabel,0, 0);
        GridPane.setColumnSpan(positionLabel, 3);

        //Color box
        VBox colorBox = new VBox();
        colorBox.setPadding(new Insets(5));
        colorBox.setFillWidth(true);
        colorBox.setAlignment(Pos.CENTER);
        Circle circle = new Circle(30, player.getColor());
        colorBox.getChildren().add(circle);
        GridPane.setConstraints(colorBox,0,1);
        GridPane.setRowSpan(colorBox,3);

        //Player info node
        VBox infoBox = new VBox();
        infoBox.setPadding(new Insets(5));
        infoBox.setAlignment(Pos.CENTER);
        Label label = new Label(player.getNickname());
        label.setWrapText(true);
        GridPane.setConstraints(infoBox,1,1);
        GridPane.setRowSpan(infoBox,3);

        //Player points nodes
        HBox coinBox = buildCoinBox(player);
        GridPane.setConstraints(coinBox,2,1);

        HBox nobilityBox = buildNobilityBox(player);
        GridPane.setConstraints(nobilityBox,2,2);

        HBox victoryBox = buildVictoryBox(player);
        GridPane.setConstraints(victoryBox,2,3);
    }

    private void setGridConstraints(GridPane gridPane) {
        ColumnConstraints colorColumn = new ColumnConstraints();
        colorColumn.setFillWidth(true);
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
    }

    private HBox buildCoinBox(Player player) {
        ClassLoader loader = this.getClass().getClassLoader();

        HBox coinBox = new HBox(5);
        coinBox.setAlignment(Pos.CENTER);
        ImageView coinIcon = new ImageView(new Image(loader.getResourceAsStream("PointSymbols/coin.png")));
        coinIcon.setPreserveRatio(true);
        coinIcon.setFitHeight(50);
        Label coinLabel = new Label(String.valueOf(CachedData.getInstance().getWealthPath().getPlayerPosition(player)));
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