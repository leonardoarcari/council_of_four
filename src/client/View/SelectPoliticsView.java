package client.View;

import core.gamemodel.PoliticsCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo Arcari on 08/06/2016.
 */
public class SelectPoliticsView extends BorderPane {
    private HBox cardsBox;
    private ScrollPane scrollPane;
    private Text selectedNo;
    private Map<String, Image> politicsImages;
    private List<PoliticsCard> selectedCards;

    public SelectPoliticsView(List<PoliticsCard> politicsCards) {
        politicsImages = new HashMap<>();
        selectedCards = new ArrayList<>();
        ClassLoader loader = this.getClass().getClassLoader();
        politicsImages.put("CYAN", new Image(loader.getResourceAsStream("bluePolitics.png")));
        politicsImages.put("PINK", new Image(loader.getResourceAsStream("pinkPolitics.png")));
        politicsImages.put("PURPLE", new Image(loader.getResourceAsStream("purplePolitics.png")));
        politicsImages.put("ORANGE", new Image(loader.getResourceAsStream("orangePolitics.png")));
        politicsImages.put("BLACK", new Image(loader.getResourceAsStream("blackPolitics.png")));
        politicsImages.put("WHITE", new Image(loader.getResourceAsStream("whitePolitics.png")));
        politicsImages.put("RAINBOW", new Image(loader.getResourceAsStream("rainbowPolitics.png")));

        selectedNo = new Text(String.valueOf(selectedCards.size()));
        selectedNo.setFont(Font.font(selectedNo.getFont().getFamily(), FontWeight.BOLD, 20));

        cardsBox = new HBox(20);
        cardsBox.setAlignment(Pos.CENTER);
        cardsBox.setPadding(new Insets(0, 30, 0, 30));
        politicsCards.forEach(card -> cardsBox.getChildren().add(buildCardNode(card)));

        scrollPane = new ScrollPane(cardsBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        setCenter(scrollPane);
        setBottom(selectedNo);
        BorderPane.setAlignment(scrollPane, Pos.CENTER);
        BorderPane.setAlignment(selectedNo, Pos.CENTER);
    }

    private VBox buildCardNode(PoliticsCard card) {
        VBox node = new VBox(10);
        node.setAlignment(Pos.CENTER);

        ImageView iv = new ImageView(politicsImages.get(card.getCardColor().name()));
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setFitHeight(250);

        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) selectedCards.add(card);
            else selectedCards.remove(card);
            selectedNo.setText(String.valueOf(selectedCards.size()));
        });

        node.getChildren().addAll(iv, checkBox);
        return node;
    }
}
