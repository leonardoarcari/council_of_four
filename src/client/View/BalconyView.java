package client.View;

import client.CachedData;
import client.ControllerUI;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.BuyPermitCardAction;
import core.gamelogic.actions.CouncilorElectionAction;
import core.gamelogic.actions.FastCouncilorElectionAction;
import core.gamemodel.CouncilColor;
import core.gamemodel.Councilor;
import core.gamemodel.PoliticsCard;
import core.gamemodel.RegionType;
import core.gamemodel.modelinterface.BalconyInterface;
import core.gamemodel.modelinterface.PlayerInterface;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matteo on 05/06/16.
 */
public class BalconyView extends ObjectImageView {
    private ObservableList<Councilor> councilors;
    private RegionType balconyRegion;
    private List<Image> councilorImages;
    private PopOver popOver;
    private List<PoliticsCard> politicsCards;
    private Button satisfyCouncil;
    private Action currentAction;

    public BalconyView(Image image, RegionType balconyRegion, double leftX, double topY, double width) {
        super(image, leftX, topY, width);
        this.balconyRegion = balconyRegion;
        councilorImages = new ArrayList<>();
        councilors = FXCollections.observableArrayList();
        buildPopOver();
        setUpCouncilors();
    }

    private void setCouncilors(List<Councilor> councilors) {
        this.councilors.clear();
        this.councilors.addAll(councilors);
    }

    public void setBalcony(BalconyInterface balcony) {
        List<Councilor> councilors = new ArrayList<>();
        Iterator<Councilor> councilorIterator = balcony.councilorsIterator();
        while(councilorIterator.hasNext()) {
            councilors.add(councilorIterator.next());
        }
        setCouncilors(councilors);
    }

    private void setUpCouncilors() {
        councilors.addListener((ListChangeListener<Councilor>) c -> {
            ClassLoader loader = this.getClass().getClassLoader();
            while(c.next()) {
                if(c.wasAdded()) {
                    councilorImages.clear();
                    c.getList().forEach(o -> councilorImages.add(loadCouncilorImage(o, loader)));
                    this.setImage(drawBalcony(councilorImages, loader));
                }
            }
        });
    }

    private static Image drawBalcony(List<Image> councilorImages, ClassLoader loader) {
        Canvas free = new Canvas(255, 140);
        GraphicsContext gc = free.getGraphicsContext2D();
        Image backImage = new Image(loader.getResourceAsStream("backbalcony.png"));
        gc.drawImage(backImage,0,0,255,140);

        int index = 0;
        for(Image image : councilorImages) {
            gc.drawImage(image,15+index*56.25,5,56.25,140);
            index++;
        }

        Image balcImage = new Image(loader.getResourceAsStream("balcony.png"));
        gc.drawImage(balcImage,0,0,255,140);

        WritableImage balc = new WritableImage(255,140);
        free.snapshot(null,balc);
        BufferedImage bi = SwingFXUtils.fromFXImage(balc, null);
        return SwingFXUtils.toFXImage(bi, null);
    }

    private Image loadCouncilorImage(Councilor councilor, ClassLoader loader) {
        String counColor = councilor.getCouncilorColor().toString();
        counColor = counColor + "_councilor.png";
        counColor = counColor.toLowerCase();
        return new Image(loader.getResourceAsStream(counColor));
    }

    private void buildPopOver() {
        popOver = new PopOver();
        popOver.setId("popover");
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        VBox balconyBox = new VBox(5);
        balconyBox.setPadding(new Insets(5));

        Label normalAction = new Label("-- Main Actions --");
        normalAction.setAlignment(Pos.CENTER);
        normalAction.setMaxWidth(Double.MAX_VALUE);

        Button electCouncilor = new Button("Elect Councilor");
        electCouncilor.setDisable(true);
        electCouncilor.setMaxWidth(Double.MAX_VALUE);
        setStyle(electCouncilor);
        electCouncilor.disableProperty().bind(CachedData.getInstance().isCouncilorSelectedProperty().not());
        electCouncilor.setOnMouseClicked(event -> {
            CachedData cachedData = CachedData.getInstance();
            currentAction = new CouncilorElectionAction((Player)cachedData.getMe(), cachedData.getSelectedCouncilor(), balconyRegion);
            cachedData.getController().sendInfo(currentAction);
            CachedData.getInstance().setIsCouncilorSelected(false);
            CachedData.getInstance().setSelectedCouncilor(null);
        });

        satisfyCouncil = new Button("Satisfy Council");
        satisfyCouncil.setDisable(true);
        satisfyCouncil.setMaxWidth(Double.MAX_VALUE);
        setStyle(satisfyCouncil);
        satisfyCouncil.setOnMouseClicked(event -> {
            CachedData cachedData = CachedData.getInstance();
            currentAction = new BuyPermitCardAction((Player)cachedData.getMe(), politicsCards, RegionType.SEA, null);
            cachedData.getController().sendInfo(currentAction);
        });

        Label fastAction = new Label("-- Fast Action --");
        fastAction.setAlignment(Pos.CENTER);
        fastAction.setMaxWidth(Double.MAX_VALUE);

        Button fastElection = new Button("Elect Councilor");
        fastElection.setDisable(true);
        fastElection.setMaxWidth(Double.MAX_VALUE);
        setStyle(fastElection);
        fastElection.disableProperty().bind(CachedData.getInstance().isCouncilorSelectedProperty().not());
        fastElection.setOnMouseClicked(event -> {
            CachedData cachedData = CachedData.getInstance();
            currentAction = new FastCouncilorElectionAction((Player)cachedData.getMe(), balconyRegion, cachedData.getSelectedCouncilor());
            cachedData.getController().sendInfo(currentAction);
            cachedData.setIsCouncilorSelected(false);
            cachedData.setSelectedCouncilor(null);
        });

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setPadding(new Insets(5,5,5,5));
        balconyBox.getChildren().addAll(normalAction, electCouncilor,satisfyCouncil,separator,fastAction, fastElection);
        setOnMouseClicked(event-> {
            popOver.setContentNode(balconyBox);
            popOver.setHeight(balconyBox.getHeight());
            popOver.show(this, 10);
        });
    }

    public void setSelectedPolitics(List<PoliticsCard> politicsCards) {
        if(politicsCards.size() == 0) {
            this.politicsCards.clear();
            satisfyCouncil.setDisable(true);
            satisfyCouncil.setText("Satisfy Council - choose valid cards!");
        } else {
            this.politicsCards = politicsCards;
            checkForSatisfaction();
        }
    }

    private void checkForSatisfaction() {
        List<CouncilColor> myColors = new ArrayList<>();
        for(Councilor councilor : councilors) {
            CouncilColor color = councilor.getCouncilorColor();
            myColors.add(color);
        }
        List<CouncilColor> politicsColors = new ArrayList<>();
        for(PoliticsCard politicsCard : politicsCards) {
            politicsColors.add(politicsCard.getCardColor());
        }

        politicsColors.forEach(councilColor -> {
                if (!myColors.remove(councilColor)) {
                    satisfyCouncil.setDisable(true);
                    satisfyCouncil.setText("Satisfy Council - choose valid cards!");
                    return;
                } else {
                    if(satisfyCouncil.isDisabled()) {
                        satisfyCouncil.setDisable(false);
                        satisfyCouncil.setText("Satisfy Council");
                    }
                }
            }
        );
    }

    private void setStyle(Button button) {
        button.setStyle("-fx-background-radius: 30;\n" +
                        "-fx-background-insets: 0,1,1;\n" +
                        "-fx-text-fill: black;\n" +
                        "-fx-effect: dropshadow( three-pass-box , rgba(0,200,255,0.3) , 3, 0.0 , 0 , 1 );");
    }
}
