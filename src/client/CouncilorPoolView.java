package client;

import core.gamemodel.CouncilColor;
import core.gamemodel.Councilor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Created by Matteo on 05/06/16.
 */
public class CouncilorPoolView {
    private ScrollPane councilorPoolNode;
    private TilePane flowNode;
    private ObservableList<CouncilorView> pool;
    private Councilor selectedCouncilor;
    private Image blackCouncilor;
    private Image whiteCouncilor;
    private Image cyanCouncilor;
    private Image orangeCouncilor;
    private Image pinkCouncilor;
    private Image purpleCouncilor;
    private BooleanProperty councilorPickedProperty;

    public CouncilorPoolView() {
        pool = FXCollections.observableArrayList();
        selectedCouncilor = null;
        councilorPickedProperty = new SimpleBooleanProperty(false);
        loadCouncilorImages();
        setUpPool();
    }

    public void setPool(List<Councilor> councilors) {
        pool.clear();
        for(Councilor councilor : councilors) {
            pool.add(new CouncilorView(councilor));
        }
    }

    public Node getFlowNode() {
        return councilorPoolNode;
    }

    private void setUpPool() {
        councilorPoolNode = new ScrollPane();
        flowNode = new TilePane();
        flowNode.setHgap(10);
        flowNode.setVgap(10);
        councilorPoolNode.setContent(flowNode);
        councilorPoolNode.prefWidthProperty().bind(flowNode.widthProperty());
        councilorPoolNode.widthProperty().addListener((observable, oldValue, newValue) -> {
            flowNode.setPrefColumns(newValue.intValue()/60);
            flowNode.setPadding(new Insets(5,newValue.intValue()/14,5,newValue.intValue()/14));
        });
        pool.addListener((ListChangeListener<CouncilorView>) c ->{
            while(c.next()) {
                if(c.wasAdded() || c.wasRemoved() || c.wasPermutated()) {
                    flowNode.getChildren().clear();
                    c.getList().forEach(o ->{
                        Image councImage = selectFromColor(o.getCouncilor().getCouncilorColor());
                        o.setViewImage(councImage);
                        o.setOnMouseClicked(event -> {
                            if(selectedCouncilor == null) {
                                o.setEffect(new DropShadow(20, Color.DARKRED));
                                selectedCouncilor = o.getCouncilor();
                                councilorPickedProperty.set(true);
                            } else if(o.getCouncilor().equals(selectedCouncilor)) {
                                o.setEffect(null);
                                selectedCouncilor = null;
                                councilorPickedProperty.set(false);
                            }
                        });
                        flowNode.getChildren().add(o);
                    });
                }
            }
        });
    }

    private Image selectFromColor(CouncilColor color) {
        if(color.equals(CouncilColor.BLACK)) return blackCouncilor;
        else if(color.equals(CouncilColor.WHITE)) return whiteCouncilor;
        else if(color.equals(CouncilColor.CYAN)) return cyanCouncilor;
        else if(color.equals(CouncilColor.ORANGE)) return orangeCouncilor;
        else if(color.equals(CouncilColor.PURPLE)) return purpleCouncilor;
        else return pinkCouncilor;
    }

    private void loadCouncilorImages () {
        ClassLoader loader = this.getClass().getClassLoader();
        blackCouncilor = new Image(loader.getResourceAsStream("black_councilor.png"));
        whiteCouncilor = new Image(loader.getResourceAsStream("white_councilor.png"));
        cyanCouncilor = new Image(loader.getResourceAsStream("cyan_councilor.png"));
        orangeCouncilor = new Image(loader.getResourceAsStream("orange_councilor.png"));
        purpleCouncilor = new Image(loader.getResourceAsStream("purple_councilor.png"));
        pinkCouncilor = new Image(loader.getResourceAsStream("pink_councilor.png"));
    }

    public boolean getCouncilorPickedProperty() {
        return councilorPickedProperty.get();
    }

    public BooleanProperty councilorPickedPropertyProperty() {
        return councilorPickedProperty;
    }
}
