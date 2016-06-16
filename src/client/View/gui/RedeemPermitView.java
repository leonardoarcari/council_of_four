package client.View.gui;

import core.gamemodel.modelinterface.PlayerInterface;
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
 * Created by Leonardo Arcari on 07/06/2016.
 */
public class RedeemPermitView extends ScrollPane implements HasMainAction{
    private List<PermitCardView> permitCardViews;
    private Effect dropShadow;
    private HBox box;
    private BooleanProperty turnEnded;

    public RedeemPermitView(PlayerInterface player) {
        box = new HBox(20);
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
        player.permitCardIterator().forEachRemaining(permitCard -> {
            PermitCardView permitCardView = new PermitCardView(card, 0, 0, 0);
            permitCardView.setPermitCard(permitCard);
            permitCardView.setOnMouseEntered(event -> permitCardView.setEffect(dropShadow));
            permitCardView.setOnMouseExited(event -> permitCardView.setEffect(null));
            permitCardView.setFitHeight(250);
            permitCardViews.add(permitCardView);
        });

        box.setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        box.getChildren().addAll(permitCardViews);

        setContent(box);
        setFitToWidth(true);
        setFitToHeight(true);
    }

    public void addClickListener(EventHandler<? super MouseEvent> value) {
        permitCardViews.forEach(permitCardView -> permitCardView.setOnMouseClicked(value));
    }

    @Override
    public void setDisableBindingMainAction(BooleanProperty mainActionAvailable) {
        turnEnded.bind(mainActionAvailable.not());
    }
}