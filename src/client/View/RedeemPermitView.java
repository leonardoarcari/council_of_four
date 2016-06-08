package client.View;

import core.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo Arcari on 07/06/2016.
 */
public class RedeemPermitView extends ScrollPane {
    private List<PermitCardView> permitCardViews;
    private Effect dropShadow;
    private HBox box;

    public RedeemPermitView(Player player) {
        box = new HBox(20);
        permitCardViews = new ArrayList<>();
        dropShadow = setShadowEffect();

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

    private DropShadow setShadowEffect() {
        Glow glow = new Glow(0.8);
        DropShadow borderglow = new DropShadow();
        borderglow.setColor(Color.WHITE);
        borderglow.setWidth(70);
        borderglow.setHeight(70);
        borderglow.setInput(glow);
        borderglow.setBlurType(BlurType.GAUSSIAN);
        return borderglow;
    }
}
