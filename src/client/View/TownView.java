package client.View;

import client.CachedData;
import core.Player;
import core.gamemodel.PermitCard;
import core.gamemodel.TownName;
import core.gamemodel.modelinterface.TownInterface;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Arcari on 03/06/2016.
 */
public class TownView extends ObjectImageView {
    private ObservableList<Player> emporiums;
    private VBox emporiumNode;
    private TownInterface town;
    private TownName townName;
    private VBox verticalNode;

    public TownView(TownName townName, double leftX, double topY, double width, Image image) {
        super(image, leftX, topY, width);
        this.townName = townName;
        this.town = null;
        emporiums = FXCollections.observableArrayList();
        setUpEmporiumNode();
        setUpPopOver();
    }

    public void update(TownInterface town) {
        if (this.town == null) this.town = town;
        List<Player> emporiums = new ArrayList<>();
        Iterator<Player> emporiumIterator = town.getPlayersEmporium();
        while (emporiumIterator.hasNext()) {
            emporiums.add(emporiumIterator.next());
        }
        setEmporiums(emporiums);
    }

    private void setEmporiums(List<Player> emporiums) {
        this.emporiums.clear();
        this.emporiums.addAll(emporiums);
    }

    public Node getEmporiumNode() {
        return verticalNode;
    }

    public TownName getTownName() {
        return townName;
    }

    private void setUpEmporiumNode() {
        emporiumNode = new VBox(5);
        emporiumNode.setPadding(new Insets(5));
        Text title = new Text("No Emporiums built");
        title.setFont(Font.font(title.getFont().getFamily(), FontWeight.BOLD, title.getFont().getSize()));
        HBox emporiumsList = new HBox(5);
        emporiums.addListener((ListChangeListener<Player>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    title.setText("Emporiums built");
                    emporiumsList.getChildren().clear();
                    c.getList().forEach(o -> {
                        Circle circle = new Circle(20, o.getColor());
                        emporiumsList.getChildren().add(circle);
                    });
                }
            }
        });
        emporiumNode.setAlignment(Pos.CENTER);
        emporiumNode.getChildren().addAll(title, emporiumsList);
    }

    private void setUpPopOver() {
        verticalNode = new VBox(5);
        verticalNode.setPadding(new Insets(5));

        Button permitActionButton = new Button("Build emporium \nwith permit card");
        permitActionButton.setTextAlignment(TextAlignment.CENTER);
        permitActionButton.setAlignment(Pos.CENTER);

        permitActionButton.setOnMouseClicked(event -> {
            ScrollPane scroll = new ScrollPane();
            HBox box = new HBox(5);
            box.setPadding(new Insets(5));
            Iterator<PermitCard> permitCardIterator = CachedData.getInstance().getMe().permitCardIterator();
            while(permitCardIterator.hasNext()) {
                PermitCard permit = permitCardIterator.next();
                if(permit.getCityPermits().contains(townName)) {
                    PermitCardView permitCardView = new PermitCardView(null,0,0,0);
                    permitCardView.setPermitCard(permit);
                    permitCardView.setFitHeight(50);
                    box.getChildren().add(permitCardView);
                }
            }
            scroll.setContent(box);
        });

        verticalNode.getChildren().addAll(emporiumNode,permitActionButton);
    }
}
