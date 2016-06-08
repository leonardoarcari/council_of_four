package client.View;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Matteo on 06/06/16.
 */
public class FastActionsView {
    private VBox actionPane;
    private VBox actionNode;
    private VBox regionNode;

    private List<Button> actionButton;
    private Button hireServant;
    private Button changePermit;
    private Button electCouncilor;
    private Button anotherAction;

    private List<Button> regionButton;
    private Button seaRegion;
    private Button hillsRegion;
    private Button mountainsRegion;
    private Button boardRegion;

    public FastActionsView() {
        actionPane = new VBox();
        actionNode = new VBox(20);
        regionNode = new VBox(20);

        hireServant = new Button("HIRE SERVANT");
        changePermit = new Button("CHANGE PERMIT");
        electCouncilor = new Button("ELECT COUNCILOR");
        anotherAction = new Button("MAKE ANOTHER ACTION");
        actionButton = new ArrayList<>(Arrays.asList(hireServant,changePermit,electCouncilor,anotherAction));

        seaRegion = new Button("Sea Balcony");
        hillsRegion = new Button("Hills Balcony");
        mountainsRegion = new Button("Mountains Balcony");
        boardRegion = new Button("King Balcony");
        regionButton = new ArrayList<>(Arrays.asList(seaRegion,hillsRegion,mountainsRegion,boardRegion));

        setUpLayer(actionNode, actionButton);
        setUpLayer(regionNode, regionButton);
        actionPane.getChildren().add(actionNode);
        VBox.setVgrow(actionNode, Priority.ALWAYS);
        VBox.setVgrow(regionNode, Priority.ALWAYS);
        createHandlers();
    }

    private void setUpLayer(VBox node, List<Button> buttons) {
        String style = (buttons.equals(actionButton)) ?
                "   -fx-background-color: \n" +
                        "        linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),\n" +
                        "        linear-gradient(#020b02, #3a3a3a),\n" +
                        "        linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),\n" +
                        "        linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%);\n" +
                        "    -fx-background-insets: 0,1,4,5;\n" +
                        "    -fx-background-radius: 9,8,5,4;\n" +
                        "    -fx-padding: 15 30 15 30;\n" +
                        "    -fx-font-family: \"Helvetica\";\n" +
                        "    -fx-font-size: 18px;\n" +
                        "    -fx-font-weight: bold;\n" +
                        "    -fx-text-fill: #333333;\n" +
                        "    -fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);" : "-fx-font: 18 sans; -fx-base: #c122b9;";

        for(Button button : buttons){
            button.setStyle(style);
            button.setMaxWidth(Double.MAX_VALUE);
        }

        node.widthProperty().addListener((observable, oldValue, newValue) ->
                node.setPadding(new Insets(10,newValue.intValue()/10,10,newValue.intValue()/10)));

        node.heightProperty().addListener((observable, oldValue, newValue) -> {
            for(Button button : buttons) {
                button.setPrefHeight(newValue.doubleValue()/5);
            }
        });

        node.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, null, null)));
        node.getChildren().addAll(buttons);
    }

    private void createHandlers() {
        hireServant.setOnAction(event ->
                //TODO: send new HireServantAction)
                System.out.println()
        );

        changePermit.setOnAction(event -> {
            actionPane.getChildren().clear();
            actionPane.getChildren().add(regionNode);
        });
    }

    public Node getBoxNode() {
        return actionPane;
    }
}
