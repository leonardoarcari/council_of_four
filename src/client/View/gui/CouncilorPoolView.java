package client.View.gui;

import client.CachedData;
import core.gamemodel.CouncilColor;
import core.gamemodel.Councilor;
import javafx.beans.binding.Bindings;
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
public class CouncilorPoolView implements HasMainAction, HasFastAction{
    private ScrollPane councilorPoolNode;
    private TilePane flowNode;
    private ObservableList<CouncilorView> pool;
    private Image blackCouncilor;
    private Image whiteCouncilor;
    private Image cyanCouncilor;
    private Image orangeCouncilor;
    private Image pinkCouncilor;
    private Image purpleCouncilor;

    private BooleanProperty mainAction;
    private BooleanProperty fastAction;

    public CouncilorPoolView() {
        pool = FXCollections.observableArrayList();
        mainAction = null;
        fastAction = null;
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
                for(CouncilorView item : c.getAddedSubList()) {
                    Image councImage = selectFromColor(item.getCouncilor().getCouncilorColor());
                    item.setViewImage(councImage);
                    item.setOnMouseClicked(event -> {
                        CachedData cachedData = CachedData.getInstance();
                        if(cachedData.getSelectedCouncilor() == null) {
                            item.setEffect(new DropShadow(20, Color.DARKRED));
                            cachedData.setSelectedCouncilor(item.getCouncilor());
                            cachedData.setIsCouncilorSelected(true);
                        } else if(item.getCouncilor().equals(cachedData.getSelectedCouncilor())) {
                            item.setEffect(null);
                            cachedData.setSelectedCouncilor(null);
                            cachedData.setIsCouncilorSelected(false);
                        }
                    });
                    flowNode.getChildren().add(item);
                }
                for(CouncilorView item : c.getRemoved()) {
                    flowNode.getChildren().remove(item);
                }
            }
        });
    }

    @Override
    public void setDisableBindingMainAction(BooleanProperty mainActionAvailable) {
        mainAction = mainActionAvailable;
        councilorPoolNode.disableProperty().bind(Bindings.and(
                mainAction.not(),
                (fastAction == null) ? new SimpleBooleanProperty(false) : fastAction.not()
        ));
    }

    @Override
    public void setDisableBindingFastAction(BooleanProperty fastActionAvailable) {
        fastAction = fastActionAvailable;
        councilorPoolNode.disableProperty().bind(Bindings.and(
                (mainAction == null) ? new SimpleBooleanProperty(false) : mainAction.not(),
                fastAction.not()
        ));
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
}
