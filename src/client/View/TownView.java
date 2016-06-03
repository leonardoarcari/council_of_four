package client.View;

import core.Player;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Created by Leonardo Arcari on 03/06/2016.
 */
public class TownView extends ObjectImageView {
    private ObservableList<Player> emporiums;
    private VBox emporiumNode;

    public TownView(Image image, double leftX, double topY, double width) {
        super(image, leftX, topY, width);
        emporiums = FXCollections.observableArrayList();
        emporiumNode = new VBox(5);
        emporiumNode.setPadding(new Insets(5));
        Text title = new Text("Emporiums built");
        title.setFont(Font.font(title.getFont().getFamily(), FontWeight.BOLD, title.getFont().getSize()));
        HBox emporiumsList = new HBox(5);
        emporiums.addListener((ListChangeListener<Player>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
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

    public void setEmporiums(List<Player> emporiums) {
        this.emporiums.clear();
        this.emporiums.addAll(emporiums);
    }

    public Node getEmporiumNode() {
        return emporiumNode;
    }
}
