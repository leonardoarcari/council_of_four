package client.View;

import core.gamemodel.PoliticsCard;
import core.gamemodel.RegionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Arcari on 08/06/2016.
 */
public class SelectPoliticsView extends BorderPane {
    private HBox cardsBox;
    private ScrollPane scrollPane;
    private Text selectedNo;
    private ObservableList<PoliticsCard> selectedCards;
    private RegionType type;

    public SelectPoliticsView(RegionType type) {
        this.type = type;

        selectedCards = FXCollections.observableArrayList();

        selectedNo = new Text(String.valueOf(selectedCards.size()));
        selectedNo.setFont(Font.font(selectedNo.getFont().getFamily(), FontWeight.BOLD, 20));

        cardsBox = new HBox(20);
        cardsBox.setAlignment(Pos.CENTER);
        cardsBox.setPadding(new Insets(0, 30, 0, 30));

        scrollPane = new ScrollPane(cardsBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        setCenter(scrollPane);
        setBottom(selectedNo);
        BorderPane.setAlignment(scrollPane, Pos.CENTER);
        BorderPane.setAlignment(selectedNo, Pos.CENTER);
    }

    public void updatePoliticsCards(Iterator<PoliticsCard> politicsCardIterator) {
        cardsBox.getChildren().clear();
        Button back = new Button("Back");
        back.setOnMouseClicked(event -> ShowPane.getInstance().hide());
        cardsBox.getChildren().add(back);
        while(politicsCardIterator.hasNext()) {
            cardsBox.getChildren().add(buildCardNode(politicsCardIterator.next()));
        }
        Button forward = new Button("Forward");
        forward.setOnMouseClicked(event -> {
            if(!type.equals(RegionType.KINGBOARD)) {
                SelectRegionPermitView regionPermitView = new SelectRegionPermitView(type, selectedCards);
                ShowPane.getInstance().setContent(regionPermitView);
                ShowPane.getInstance().show();
            } else {
                TownsWithBonusView.getInstance().changeTownListener(selectedCards);
                ShowPane.getInstance().hide();
            }
        });
        cardsBox.getChildren().add(forward);
    }

    private VBox buildCardNode(PoliticsCard card) {
        VBox node = new VBox(10);
        node.setAlignment(Pos.CENTER);

        ImageView iv = new ImageView(ImagesMaps.getInstance().getPolitics(card.getCardColor().name()));
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setFitHeight(250);

        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                selectedCards.add(card);
            } else {
                selectedCards.remove(card);
            }
            selectedNo.setText(String.valueOf(selectedCards.size()));
        });

        node.getChildren().addAll(iv, checkBox);
        return node;
    }

    public ObservableList<PoliticsCard> getSelectedCards() {
        return selectedCards;
    }

    public void addClickListener(EventHandler<? super MouseEvent> value) {
        cardsBox.getChildren().get(0).setOnMouseClicked(value);
    }
}
