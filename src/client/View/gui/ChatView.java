package client.View.gui;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.ChatAction;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Created by Leonardo Arcari on 11/06/2016.
 */
public class ChatView extends GridPane {
    private TextArea textArea;
    private TextField textField;

    public ChatView() {
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textField = new TextField();
        textField.setOnAction(event -> {
            CachedData.getInstance().getController().sendInfo(new ChatAction(
                    (Player) CachedData.getInstance().getMe(),
                    textField.getText().trim().replace("\n", "")
            ));
            textField.clear();
        });

        GridPane.setConstraints(textArea, 0, 0, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
        GridPane.setConstraints(textField, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER, new Insets(0, 5, 5, 5));
        getChildren().addAll(textArea, textField);
    }

    public void append(String message) {
        textArea.appendText("\n" + message);
    }
}
