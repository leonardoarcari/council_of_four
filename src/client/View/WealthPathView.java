package client.View;

import core.Player;
import core.gamemodel.modelinterface.WealthPathInterface;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * Created by Leonardo Arcari on 06/06/2016.
 */
public class WealthPathView {
    private double[] posX;
    private final double width = 0.02776135619871535;
    private ObservableList<Circle> players;

    private WealthPathView() {
        posX = new double[21];
        players = FXCollections.observableArrayList();
        initializePosX();
    }

    public void addListener(ListChangeListener<? super Node> listener) {
        players.addListener(listener);
    }

    public void updateWealthPath(WealthPathInterface wealthPath) {
        players.clear();
        List<List<Player>> pathList = wealthPath.getPlayers();
        for (int i = 0; i < pathList.size(); i++) {
            List<Player> sameSpotPlayers = pathList.get(i);
            if (sameSpotPlayers.size() == 1) {
                Circle marker = new Circle(width / 2);

            }
        }
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
}
