package client.View.gui;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.BuyPermitCardAction;
import core.gamemodel.PoliticsCard;
import core.gamemodel.Region;
import core.gamemodel.RegionType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 10/06/16.
 */
public class SelectRegionPermitView extends ScrollPane implements HasMainAction{
    private List<PermitCardView> permitCardViews;
    private Effect dropShadow;
    private HBox box;
    private Region region;
    private BooleanProperty turnEnded;

    public SelectRegionPermitView(RegionType regionType, List<PoliticsCard> politicsSelected) {
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

        region = getRegionFrom(regionType);
        if(region.getRightPermitCard() == null && region.getLeftPermitCard() == null)
            ShowPane.getInstance().hide();

        Image card = new Image(this.getClass().getClassLoader().getResourceAsStream("permitCard.png"));
        PermitCardView permitCardViewL = new PermitCardView(card, 0, 0, 0);
        PermitCardView permitCardViewR = new PermitCardView(card, 0, 0, 0);
        if(region.getLeftPermitCard() != null) {
            permitCardViewL.setPermitCard(region.getLeftPermitCard());
            adjustPermitCard(permitCardViewL);
            permitCardViewL.setOnMouseClicked(event -> {
                Action currentAction = new BuyPermitCardAction(
                        (Player) CachedData.getInstance().getMe(),politicsSelected, regionType, Region.PermitPos.LEFT);
                CachedData.getInstance().getController().sendInfo(currentAction);
                ShowPane.getInstance().hide();
            });
        }
        if(region.getRightPermitCard() != null) {
            permitCardViewR.setPermitCard(region.getRightPermitCard());
            adjustPermitCard(permitCardViewR);
            permitCardViewR.setOnMouseClicked(event -> {
                Action currentAction = new BuyPermitCardAction(
                        (Player) CachedData.getInstance().getMe(),politicsSelected, regionType, Region.PermitPos.RIGHT);
                CachedData.getInstance().getController().sendInfo(currentAction);
                ShowPane.getInstance().hide();
            });
        }

        box.setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        box.getChildren().addAll(permitCardViews);

        setContent(box);
        setFitToWidth(true);
        setFitToHeight(true);
    }

    private void adjustPermitCard(PermitCardView view) {
        view.setOnMouseEntered(event -> view.setEffect(dropShadow));
        view.setOnMouseExited(event -> view.setEffect(null));
        view.setFitHeight(250);
        permitCardViews.add(view);
    }

    private Region getRegionFrom(RegionType type) {
        if(type.equals(RegionType.SEA)) return CachedData.getInstance().getSeaRegion();
        else if(type.equals(RegionType.HILLS)) return CachedData.getInstance().getHillsRegion();
        else return CachedData.getInstance().getMountainsRegion();
    }

    @Override
    public void setDisableBindingMainAction(BooleanProperty mainActionAvailable) {
        turnEnded.bind(mainActionAvailable.not());
    }
}
