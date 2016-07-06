package client.View.gui;

import client.CachedData;
import core.gamemodel.modelinterface.RegionInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * A <code>DrawFreePermitView</code> lets the player choose a permit card from those revealed on the game-board as a
 * consequence of a {@link core.gamemodel.bonus.DrawPermitCard DrawPermitCard bonus}.
 */
public class DrawFreePermitView extends ScrollPane implements HasMainAction{
    private List<PermitCardView> permitCardViews;
    private Effect dropShadow;
    private BooleanProperty turnEnded;

    /**
     * Initializes a DrawFreePermitView
     */
    public DrawFreePermitView() {
        HBox box = new HBox(20);
        permitCardViews = new ArrayList<>();
        dropShadow = TownsWithBonusView.setShadowEffect();
        turnEnded = new SimpleBooleanProperty(false);
        turnEnded.addListener((observable, oldValue, newValue) -> {
            if(newValue != oldValue && newValue) {
                permitCardViews.clear();
                ShowPane.getInstance().hide();
            }
        });

        Image card = new Image(this.getClass().getClassLoader().getResourceAsStream("permitCard.png"));
        fillPermitCardViews(CachedData.getInstance().getSeaRegion(), card);
        fillPermitCardViews(CachedData.getInstance().getHillsRegion(), card);
        fillPermitCardViews(CachedData.getInstance().getMountainsRegion(), card);

        box.setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        if(permitCardViews.isEmpty()) ShowPane.getInstance().hide();
        else box.getChildren().addAll(permitCardViews);

        setContent(box);
        setFitToWidth(true);
        setFitToHeight(true);
    }

    private void fillPermitCardViews(RegionInterface region, Image card) {
        PermitCardView permitCardViewL = new PermitCardView(card, 0, 0, 0);
        if(region.getLeftPermitCard() != null) {
            permitCardViewL.setPermitCard(region.getLeftPermitCard());
            adjustPermitCard(permitCardViewL);
        }

        PermitCardView permitCardViewR = new PermitCardView(card, 0, 0, 0);
        if(region.getRightPermitCard() != null) {
            permitCardViewR.setPermitCard(region.getRightPermitCard());
            adjustPermitCard(permitCardViewR);
        }
    }

    private void adjustPermitCard(PermitCardView view) {
        view.setOnMouseEntered(event -> view.setEffect(dropShadow));
        view.setOnMouseExited(event -> view.setEffect(null));
        view.setFitHeight(250);
        permitCardViews.add(view);
    }

    /**
     * Registers the input EventHandler on clicks on shown <code>PermitCardView</code>s
     * @param value
     */
    public void addClickListener(EventHandler<? super MouseEvent> value) {
        permitCardViews.forEach(permitCardView -> permitCardView.setOnMouseClicked(value));
    }

    @Override
    public void setDisableBindingMainAction(BooleanProperty mainActionAvailable) {
        turnEnded.bind(mainActionAvailable.not());
    }
}