package client.View;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.BuildEmpoKingAction;
import core.gamelogic.actions.BuildEmpoPCAction;
import core.gamemodel.PermitCard;
import core.gamemodel.RegionType;
import core.gamemodel.TownName;
import core.gamemodel.modelinterface.TownInterface;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.PopOver;
import org.controlsfx.tools.Platform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Arcari on 03/06/2016.
 */
public class TownView extends ObjectImageView implements HasMainAction {
    private ObservableList<Player> emporiums;
    private VBox emporiumNode;
    private TownInterface town;
    private TownName townName;
    private RegionType regionType;
    private VBox verticalNode;
    private ScrollPane scroll;

    private PopOver townPopOver;
    private Button permitActionButton;

    private ObservableList<PermitCard> availablePermits;
    private BooleanProperty isEmporiumBuildable;

    public TownView(TownName townName, RegionType regionType, double leftX, double topY, double width, Image image) {
        super(image, leftX, topY, width);
        this.townName = townName;
        this.regionType = regionType;
        this.town = null;
        isEmporiumBuildable = new SimpleBooleanProperty(false);
        scroll = new ScrollPane();
        emporiums = FXCollections.observableArrayList();
        availablePermits = FXCollections.observableArrayList();

        CachedData.getInstance().listenToPermits(c -> {
            availablePermits.clear();
            isEmporiumBuildable.set(ViewAlgorithms.checkAvailablePermits(townName,availablePermits));
            if(isEmporiumBuildable.getValue()) {
                createPermitScrollPane();
            }
        });

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

    public TownName getTownName() {
        return townName;
    }

    private void setUpEmporiumNode() {
        emporiumNode = new VBox(5);
        emporiumNode.setPadding(new Insets(5));
        Text title = new Text(townName.toString() + " -- No Emporiums built");
        title.setFont(Font.font(title.getFont().getFamily(), FontWeight.BOLD, title.getFont().getSize()));
        HBox emporiumsList = new HBox(5);
        emporiums.addListener((ListChangeListener<Player>) c -> {
            javafx.application.Platform.runLater(()->{
            while (c.next()) {
                if (c.wasAdded()) {
                    title.setText(townName.toString() + " -- Emporiums built");
                    emporiumsList.getChildren().clear();
                    c.getList().forEach(o -> {
                        Circle circle = new Circle(20, o.getColor());
                        emporiumsList.getChildren().add(circle);
                    });
                }
            } });
        });
        emporiumNode.setAlignment(Pos.CENTER);
        emporiumNode.getChildren().addAll(title, emporiumsList);
    }

    private void setUpPopOver() {
        townPopOver = new PopOver();
        verticalNode = new VBox(5);
        verticalNode.setPadding(new Insets(5));

        permitActionButton = new Button("Build emporium \nwith permit card");
        permitActionButton.setTextAlignment(TextAlignment.CENTER);
        permitActionButton.setAlignment(Pos.CENTER);
        permitActionButton.setMaxWidth(Double.MAX_VALUE);

        permitActionButton.setOnMouseClicked(event -> {
            if(verticalNode.getChildren().size() < 3)
                verticalNode.getChildren().add(scroll);
        });

        verticalNode.getChildren().addAll(emporiumNode,permitActionButton);
        townPopOver.setContentNode(verticalNode);
    }

    private void createPermitScrollPane() {
        javafx.application.Platform.runLater(()-> {
        HBox box = new HBox(5);
        box.setPadding(new Insets(5));
        Iterator<PermitCard> permitCardIterator = availablePermits.iterator();
        while(permitCardIterator.hasNext()) {
            PermitCard permit = permitCardIterator.next();
            if(permit.getCityPermits().contains(townName)) {
                PermitCardView permitCardView = new PermitCardView(null,0,0,0);
                permitCardView.setPermitCard(permit);
                permitCardView.setFitHeight(60);
                permitCardView.setOnMouseEntered(event -> permitCardView.setEffect(new Glow(1.0)));
                permitCardView.setOnMouseExited(event -> permitCardView.setEffect(null));
                permitCardView.setOnMouseClicked(event -> {
                    BuildEmpoPCAction action = new BuildEmpoPCAction((Player)CachedData.getInstance().getMe(),regionType,townName,permitCardView.getPermitCard());
                    CachedData.getInstance().getController().sendInfo(action);
                    scroll.setContent(null);
                    townPopOver.hide();
                });
                box.getChildren().add(permitCardView);
            }
        }
        scroll.setContent(box); });
    }

    @Override
    public void setDisableBindingMainAction(BooleanProperty mainActionAvailable) {
        permitActionButton.disableProperty().bind(Bindings.or(isEmporiumBuildable.not(), mainActionAvailable.not()));
    }

    public PopOver getTownPopOver() {
        return townPopOver;
    }
}
