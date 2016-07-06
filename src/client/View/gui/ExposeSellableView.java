package client.View.gui;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.ExposeSellablesAction;
import core.gamemodel.OnSaleItem;
import core.gamemodel.PermitCard;
import core.gamemodel.PoliticsCard;
import core.gamemodel.modelinterface.SellableItem;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A <code>ExposeSellableView</code> lets the player pick which items to sell, to choose its price and expose it in the
 * showcase.
 */
public class ExposeSellableView extends GridPane {
    private HBox cardsBox;
    private Map<SellableItem, OnSaleItem> onSaleItems;

    /**
     * Initializes a <code>ExposeSellableView</code>
     */
    public ExposeSellableView() {
        onSaleItems = new HashMap<>();

        cardsBox = new HBox(20);
        cardsBox.setAlignment(Pos.CENTER);
        cardsBox.setPadding(new Insets(0, 30, 0, 30));


        ScrollPane scrollPane = new ScrollPane(cardsBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        Button exposeButton = new Button("Expose Items");
        HBox buttonBox = new HBox(exposeButton);
        buttonBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(scrollPane, 0, 0, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
        GridPane.setConstraints(buttonBox, 0, 1, 1, 1, HPos.CENTER, VPos.BOTTOM);
        GridPane.setMargin(buttonBox, new Insets(20, 0, 20, 0));
        getChildren().addAll(scrollPane, buttonBox);

        exposeButton.setOnMouseClicked(event -> {
            CachedData.getInstance().getController().stopTimer();
            CachedData.getInstance().getController().sendInfo(new ExposeSellablesAction(
                    (Player) CachedData.getInstance().getMe(),
                    new ArrayList<>(onSaleItems.values())
            ));
            setDisable(true);
            clearSellableItem();
        });
    }

    /**
     * Adds a SellableItem to the list of potential items to expose
     * @param item SellableItem to show
     */
    public void addSellableItem(SellableItem item) {
        cardsBox.getChildren().add(buildSellableNode(item));
    }

    /**
     * Clears the list of potential <code>SellableItem</code>s to expose
     */
    public void clearSellableItem() {
        cardsBox.getChildren().clear();
    }

    private VBox buildSellableNode(SellableItem item) {
        VBox node = new VBox(10);
        node.setAlignment(Pos.CENTER);

        ImageView iv = new ImageView();
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setFitHeight(250);
        iv.setImage(retrieveImage(item));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        Spinner<Integer> spinner = new Spinner<>(valueFactory);
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            onSaleItems.put(item, new OnSaleItem(
                    item,
                    newValue,
                    (Player) CachedData.getInstance().getMe()
            ));
        });

        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                onSaleItems.put(item, new OnSaleItem(
                        item,
                        spinner.getValue(),
                        (Player) CachedData.getInstance().getMe()
                ));
            } else {
                onSaleItems.remove(item);
            }
        });
        spinner.disableProperty().bind(checkBox.selectedProperty().not());

        HBox setPriceBox = new HBox(5);
        setPriceBox.setAlignment(Pos.CENTER);
        setPriceBox.getChildren().addAll(checkBox, spinner);

        node.getChildren().addAll(iv, setPriceBox);
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
