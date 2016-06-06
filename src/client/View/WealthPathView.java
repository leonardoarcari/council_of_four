package client.View;

import core.Player;
import core.gamemodel.modelinterface.WealthPathInterface;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Arcari on 06/06/2016.
 */
public class WealthPathView {
    private double[] posX;
    private final double width = 0.01952073703854243;
    private final double posY = 0.9066843150231635;
    private final double m = - 0.0072799470549305 / 0.66721905047932958;
    private ObservableList<Holder> players;
    private PopOver popOver;
    private Effect effect;

    public WealthPathView() {
        posX = new double[21];
        players = FXCollections.observableArrayList();
        popOver = new PopOver();
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        effect = setShadowEffect();
        initializePosX();
    }

    public void addListener(ListChangeListener<? super Holder> listener) {
        players.addListener(listener);
    }

    public void updateWealthPath(WealthPathInterface wealthPath) {
        players.clear();
        List<List<Player>> pathList = wealthPath.getPlayers();
        List<Holder> holders = new ArrayList<>();
        for (int i = 0; i < pathList.size(); i++) {
            List<Player> sameSpotPlayers = pathList.get(i);
            if (sameSpotPlayers.size() > 0) {
                Holder marker = new Holder(width / 2, posX[i]);
                marker.anchorY = posY + m * posX[i];
                if (sameSpotPlayers.size() == 1) {
                    marker.setFill(Color.DEEPSKYBLUE);
                } else if (sameSpotPlayers.size() > 1) {
                    marker.setFill(Color.DARKVIOLET);
                }
                VBox popBox = new VBox(5);
                popBox.setAlignment(Pos.CENTER);
                Text title = new Text("Players");
                HBox hBox = new HBox(5);
                hBox.setAlignment(Pos.CENTER);
                popBox.getChildren().addAll(title, hBox);
                sameSpotPlayers.forEach(player -> {
                    Circle playerCircle = new Circle(10, player.getColor());
                    hBox.getChildren().add(playerCircle);
                });
                marker.setOnMouseClicked(event -> {
                    popOver.setContentNode(popBox);
                    popOver.show(marker, 30);
                });
                marker.setOnMouseEntered(event -> marker.setEffect(effect));
                marker.setOnMouseExited(event -> marker.setEffect(null));
                holders.add(marker);
            }

        }
        players.addAll(holders);
    }

    public double getWidth() {
        return width;
    }

    public double getPosY() {
        return posY;
    }

    private void initializePosX() {
        posX[0] = 0.04212067837046467;
        posX[1] = 0.07323254307592153 ;
        posX[2] = 0.10769491628811989 ;
        posX[3] = 0.1435932217174932 ;
        posX[4] = 0.1775769508572999 ;
        posX[5] = 0.21156067999710662 ;
        posX[6] = 0.24650169728169663 ;
        posX[7] = 0.2819213586386783 ;
        posX[8] = 0.31638373185087665 ;
        posX[9] = 0.3503674609906834;
        posX[10] = 0.3819579697685319 ;
        posX[11] = 0.4173776311255135 ;
        posX[12] = 0.44944678397575366 ;
        posX[13] = 0.4810372927536022 ;
        posX[14] = 0.5145423778210173 ;
        posX[15] = 0.5490047510332157 ;
        posX[16] = 0.5844244123901973 ;
        posX[17] = 0.6188867856023956 ;
        posX[18] = 0.6543064469593773 ;
        posX[19] = 0.6882901760991841 ;
        posX[20] = 0.7213166170942075 ;
    }

    private DropShadow setShadowEffect() {
        Glow glow = new Glow(0.8);
        DropShadow borderglow = new DropShadow();
        borderglow.setColor(Color.WHITE);
        borderglow.setWidth(70);
        borderglow.setHeight(70);
        borderglow.setInput(glow);
        borderglow.setBlurType(BlurType.GAUSSIAN);
        return borderglow;
    }

    public class Holder extends Circle {
        double baseRadius;
        double anchorX;
        double anchorY;

        public Holder(double radius, double anchorX) {
            super(radius);
            this.baseRadius = radius;
            this.anchorX = anchorX;
        }

        public double getBaseRadius() {
            return baseRadius;
        }

        public double getAnchorX() {
            return anchorX;
        }
    }
}
