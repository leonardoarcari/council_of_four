package client.View;

import core.Player;
import core.gamemodel.modelinterface.VictoryPathInterface;
import core.gamemodel.modelinterface.WealthPathInterface;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 13/06/16.
 */
public class VictoryPathView implements PathViewInterface {
    private double[] posX;
    private double[] posY;
    private final double width = 0.01952073703854243;

    private ObservableList<ObjectImageView> players;
    private PopOver popOver;
    private Effect effect;

    public VictoryPathView() {
        posX = new double[28];
        posY = new double[24];
        players = FXCollections.observableArrayList();
        popOver = new PopOver();
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        effect = new DropShadow(20, Color.WHITE);
        initializePosX();
        initializePosY();
    }

    @Override
    public void addListener(ListChangeListener<? super ObjectImageView> listener) {
        players.addListener(listener);
    }

    public void updateVictoryPath(VictoryPathInterface victoryPath) {
        players.clear();
        List<List<Player>> pathList = victoryPath.getPlayers();
        List<ObjectImageView> holders = new ArrayList<>();
        for (int i = 0; i < pathList.size(); i++) {
            List<Player> sameSpotPlayers = pathList.get(i);
            if (sameSpotPlayers.size() > 0) {
                Image icon = new Image(this.getClass().getClassLoader().getResourceAsStream("merchant.png"));
                double posHor = (i == 0 || i > 76) ?  posX[0] : ((i>26 && i<51) ? posX[27] : ((i<27)?posX[i]:posX[77-i]));
                double posVer = (i < 28) ? posY[0] : ((i>49 && i <78) ? posY[23] : ((i<50)?posY[i]:posY[100-i]));
                ObjectImageView marker = new ObjectImageView(icon, posHor, posVer, width);

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
                marker.setEffect(new Glow(1.0));
                marker.setOnMouseEntered(event -> marker.setEffect(effect));
                marker.setOnMouseExited(event -> marker.setEffect(new Glow(1.0)));
                holders.add(marker);
            }

        }
        players.addAll(holders);
    }

    private void initializePosX() {
        posX[0] = 0.024304918562939478;
        posX[1] = 0.05902623079571016 ;
        posX[2] = 0.09374754302848085 ;
        posX[3] = 0.12731147818682584 ;
        posX[4] = 0.1631901674940222 ;
        posX[5] = 0.19791147972679288 ;
        posX[6] = 0.23379016903398928 ;
        posX[7] = 0.2650393500434829 ;
        posX[8] = 0.30207541642510494 ;
        posX[9] = 0.33679672865787563;
        posX[10] = 0.3703606638162206 ;
        posX[11] = 0.4050819760489913 ;
        posX[12] = 0.4386459112073363 ;
        posX[13] = 0.47336722344010695 ;
        posX[14] = 0.5092459127473034 ;
        posX[15] = 0.5428098479056483 ;
        posX[16] = 0.5763737830639933 ;
        posX[17] = 0.6122524723711897 ;
        posX[18] = 0.6469737846039604 ;
        posX[19] = 0.681695096836731 ;
        posX[20] = 0.715259031995076 ;
        posX[21] = 0.7499803442278468;
        posX[22] = 0.7847016564606174;
        posX[23] = 0.8194229686933882;
        posX[24] = 0.8541442809261588;
        posX[25] = 0.8877082160845038;
        posX[26] = 0.9235869053917002;
        posX[27] = 0.9606229717733222;
    }

    private void initializePosY() {
        posY[0] = 0.0273224043715847;
        posY[1] = 0.06284153005464481;
        posY[2] = 0.1051912568306011;
        posY[3] = 0.14754098360655737;
        posY[4] = 0.18442622950819673;
        posY[5] = 0.22540983606557377;
        posY[6] = 0.2677595628415301;
        posY[7] = 0.3087431693989071;
        posY[8] = 0.34972677595628415;
        posY[9] = 0.3907103825136612;
        posY[10] = 0.42896174863387976;
        posY[11] = 0.47404371584699456;
        posY[12] = 0.5136612021857924;
        posY[13] = 0.5532786885245902;
        posY[14] = 0.5942622950819673;
        posY[15] = 0.6352459016393442;
        posY[16] = 0.6762295081967213;
        posY[17] = 0.7144808743169399;
        posY[18] = 0.7581967213114754;
        posY[19] = 0.796448087431694;
        posY[20] = 0.837431693989071;
        posY[21] = 0.8797814207650273;
        posY[22] = 0.9166666666666666;
        posY[23] = 0.9549180327868853;
    }
}
