package client.View;

import core.Player;
import core.gamemodel.NobilityPath;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.NobilityPathInterface;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import javax.xml.ws.Holder;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Matteo on 02/06/16.
 */
public class NobilityPathView extends ObjectImageView implements PathViewInterface {
    private double[] posX;
    private boolean alreadyDrawn;
    private final double width = 0.02352073703854243;
    private final double posY = 0.8661202185792349;
    private PopOver popOver;
    private ObservableList<ObjectImageView> players;

    public NobilityPathView(Image image, double leftX, double topY, double width) {
        super(image, leftX, topY, width);
        posX = new double[21];
        alreadyDrawn = false;
        popOver = new PopOver();
        players = FXCollections.observableArrayList();
        initializePosX();
    }

    public void updateNobilityPath(NobilityPathInterface nobilityPath) {
        if(!alreadyDrawn) {
            this.setImage(drawPath(nobilityPath));
            alreadyDrawn = true;
        }
        updatePlayers(nobilityPath);
    }

    private Image drawPath(NobilityPathInterface nobilityPath) {
        Canvas free = new Canvas(1705, 150);
        GraphicsContext gc = free.getGraphicsContext2D();
        gc.drawImage(this.getImage(),0,0,1705,150);
        List<List<Bonus>> listOBonus = nobilityPath.getBonusPath();
        int i = 0;
        for(List<Bonus> list : listOBonus) {
            if(list.size() != 0) {
                drawBonuses(gc, list, i);
            }
            i++;
        }
        WritableImage nobi = new WritableImage(1705,150);
        free.snapshot(null,nobi);
        BufferedImage bi = SwingFXUtils.fromFXImage(nobi, null);
        return SwingFXUtils.toFXImage(bi, null);
    }

    private void drawBonuses(GraphicsContext gc, List<Bonus> list, int index) {
        ClassLoader loader = this.getClass().getClassLoader();
        int ident = (index < 10) ? 23:20;
        int yPos = (list.size() == 1) ? 45:35;
        int i = 0;

        for(Bonus bonus : list) {
            String bonusName = getImageName(bonus);
            bonusName = "BonusImages/" + bonusName;
            Image bonusImage = new Image(loader.getResourceAsStream(bonusName));
            gc.drawImage(bonusImage,ident+81*index,yPos+35*i,50,50);
            i++;
        }
    }

    private String getImageName(Bonus bonus) {
        String bonusname;
        String className = bonus.getClass().getName();
        bonusname = className.substring(className.lastIndexOf(".")+1);
        bonusname = bonusname + "_" + bonus.getValue() + ".png";
        return bonusname.toLowerCase();
    }

    private void updatePlayers(NobilityPathInterface nobilityPath) {
        players.clear();
        List<List<Player>> pathList = nobilityPath.getPlayers();
        List<ObjectImageView> holders = new ArrayList<>();

        for (int i = 0; i < pathList.size(); i++) {
            List<Player> sameSpotPlayers = pathList.get(i);
            if (sameSpotPlayers.size() > 0) {
                Image icon = new Image(this.getClass().getClassLoader().getResourceAsStream("merchant.png"));
                ObjectImageView marker = new ObjectImageView(icon, posX[i], posY, width);
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
                    popOver.show(marker, 0);
                });
                marker.setEffect(new Glow(1.0));
                marker.setOnMouseEntered(event -> marker.setEffect(new DropShadow(10.0,Color.WHITE)));
                marker.setOnMouseExited(event -> marker.setEffect(new Glow(1.0)));
                holders.add(marker);
            }
        }
        players.addAll(holders);
    }

    public void addListener(ListChangeListener<? super ObjectImageView> listener) {
        players.addListener(listener);
    }

    private void initializePosX() {
        posX[0] = 0.06134098494456154;
        posX[1] = 0.09606229717733222;
        posX[2] = 0.12615410111240014;
        posX[3] = 0.1620327904195965;
        posX[4] = 0.19212459435466445;
        posX[5] = 0.22453115243858374;
        posX[6] = 0.25809508759692873;
        posX[7] = 0.28818689153199667;
        posX[8] = 0.32175082669034166;
        posX[9] = 0.35415738477426095;
        posX[10] = 0.3830918116349032;
        posX[11] = 0.4189705009420996;
        posX[12] = 0.4513770590260189;
        posX[13] = 0.4814688629610868;
        posX[14] = 0.5161901751938575;
        posX[15] = 0.547439356203351;
        posX[16] = 0.5798459142872704;
        posX[17] = 0.611095095296764;
        posX[18] = 0.6435016533806833;
        posX[19] = 0.6747508343901769;
        posX[20] = 0.7094721466229477;
    }
}
