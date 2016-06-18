package client.View.gui;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.BuyObjectsAction;
import core.gamemodel.OnSaleItem;
import core.gamemodel.PermitCard;
import core.gamemodel.PoliticsCard;
import core.gamemodel.modelinterface.SellableItem;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo Arcari on 11/06/2016.
 */
public class BuySellableView extends GridPane {
    private HBox cardsBox;
    private List<OnSaleItem> itemToBuy;
    private IntegerProperty coinsSpentProperty;

    public BuySellableView() {
        itemToBuy = new ArrayList<>();
        coinsSpentProperty = new SimpleIntegerProperty(0);

        cardsBox = new HBox(20);
        cardsBox.setAlignment(Pos.CENTER);
        cardsBox.setPadding(new Insets(0, 30, 0, 30));

        ScrollPane scrollPane = new ScrollPane(cardsBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        Button exposeButton = new Button("Buy Selected Items");
        HBox buttonBox = new HBox(exposeButton);
        buttonBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(scrollPane, 0, 0, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
        GridPane.setConstraints(buttonBox, 0, 1, 1, 1, HPos.CENTER, VPos.BOTTOM);
        GridPane.setMargin(buttonBox, new Insets(20, 0, 20, 0));
        getChildren().addAll(scrollPane, buttonBox);

        exposeButton.setOnMouseClicked(event -> {
            CachedData.getInstance().getController().stopTimer();
            CachedData.getInstance().getController().sendInfo(new BuyObjectsAction(
                    (Player) CachedData.getInstance().getMe(),
                    itemToBuy
                )
            );
            setDisable(true);
        });
    }

    public void addOnSaleItem(OnSaleItem item) {
        cardsBox.getChildren().add(buildOnSaleNode(item));
    }

    public void clearOnSaleItem() {
        cardsBox.getChildren().clear();
    }

    private VBox buildOnSaleNode(OnSaleItem item) {
        VBox node = new VBox(10);
        node.setAlignment(Pos.CENTER);

        ImageView iv = new ImageView();
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setFitHeight(250);
        iv.setImage(retrieveImage(item.getItem()));

        CheckBox checkBox = new CheckBox();
        checkBox.setText(String.valueOf(item.getPrice()));
        // Control availability
        checkBox.setDisable(
                coinsSpentProperty.intValue() + item.getPrice() >
                        CachedData.getInstance().getWealthPath().getPlayerPosition(
                                (Player) CachedData.getInstance().getMe()
                        )
                        && !checkBox.selectedProperty().getValue()
        );

        // Add listeners
        coinsSpentProperty.addListener((observable, oldValue, newValue) -> {
            checkBox.setDisable(
                    newValue.intValue() + item.getPrice() >
                            CachedData.getInstance().getWealthPath().getPlayerPosition(
                                    (Player) CachedData.getInstance().getMe()
                            )
                    && !checkBox.selectedProperty().getValue()
            );
        });
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                itemToBuy.add(item);
                coinsSpentProperty.setValue(coinsSpentProperty.get() + item.getPrice());
            } else {
                itemToBuy.remove(item);
                coinsSpentProperty.setValue(coinsSpentProperty.get() - item.getPrice());
            }
        });

        node.getChildren().addAll(iv, checkBox);
        return node;
    }

    private Image retrieveImage(SellableItem item) {
        if (item instanceof PoliticsCard) {
            PoliticsCard card = (PoliticsCard) item;
            return ImagesMaps.getInstance().getPolitics(card.getCardColor().name());
        } else if (item instanceof PermitCard) {
            return ImagesMaps.getInstance().getPermit((PermitCard) item);
        } return ImagesMaps.getInstance().getServant();
    }
}
