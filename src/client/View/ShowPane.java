package client.View;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.spreadsheet.Grid;

/**
 * Created by Leonardo Arcari on 07/06/2016.
 */
public class ShowPane {
    private GridPane pane;
    private Parent previous;
    private Scene scene;
    private Text title;
    private Node content;

    public ShowPane(Scene scene, Parent previous) {
        this.scene = scene;
        this.previous = previous;


        pane = new GridPane();
        RowConstraints rc0 = new RowConstraints();
        rc0.setPercentHeight(30);
        rc0.setValignment(VPos.CENTER);
        rc0.setVgrow(Priority.ALWAYS);
        rc0.setFillHeight(true);
        RowConstraints rc1 = new RowConstraints();
        rc1.setPercentHeight(70);
        rc1.setValignment(VPos.CENTER);
        rc1.setVgrow(Priority.ALWAYS);
        rc1.setFillHeight(true);
        pane.getRowConstraints().addAll(rc0, rc1);
        ColumnConstraints cc0 = new ColumnConstraints();
        cc0.setHalignment(HPos.CENTER);
        cc0.setFillWidth(true);
        cc0.setHgrow(Priority.ALWAYS);
        pane.getColumnConstraints().add(cc0);

        title = new Text("Make a choice");
        title.setFont(Font.font(title.getFont().getFamily(), FontWeight.BOLD, 30));
        pane.add(title, 0, 0);

        pane.setGridLinesVisible(true);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setContent(Node content) {
        this.content = content;
        pane.add(this.content, 0, 1);
    }

    public void show() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.25), this.previous);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.25), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeOut.setOnFinished(event -> scene.setRoot(pane));

        SequentialTransition crossFade = new SequentialTransition(fadeOut, fadeIn);
        crossFade.play();
    }

    public void hide() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.25), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.25), previous);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeOut.setOnFinished(event -> scene.setRoot(previous));

        SequentialTransition crossFade = new SequentialTransition(fadeOut, fadeIn);
        crossFade.play();
    }
}
