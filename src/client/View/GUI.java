package client.View;

import client.ControllerUI;
import core.Player;
import core.gamelogic.AbstractBonusFactory;
import core.gamelogic.BonusFactory;
import core.gamelogic.BonusOwner;
import core.gamemodel.*;
import core.gamemodel.bonus.Bonus;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by Leonardo Arcari on 31/05/2016.
 */
public class GUI extends Application {
    private ClassLoader classLoader;
    private GridPane gridPane;
    private ImageView gameboardIV;
    private AnchorPane boardAnchor;
    private ControllerUI controllerUI;

    private List<ObjectImageView> boardObjects;
    private Map<TownName, TownView> townsView;

    private final static int GAMEBOARD_HEIGHT = 700;
    private final static float GAMEBOARD_WIDTH = GAMEBOARD_HEIGHT * 72f/61;

    @Override
    public void init() throws Exception {
        super.init();
        boardObjects = new ArrayList<>();
        ControllerUI controllerUI = new ControllerUI(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        gridPane = new GridPane();
        classLoader = this.getClass().getClassLoader();
        final DropShadow borderglow = setShadowEffect();

        PopOver popOver = new PopOver();
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane();
        //FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL);
        //flowPane.setPadding(new Insets(10));
        FlowPane pane = new FlowPane(Orientation.HORIZONTAL, 5, 5);
        Button merda = new Button("Cliccami se\n hai coraggio");

        //flowPane.getChildren().addAll(merda);
        scrollPane.setContent(merda);
        scrollPane.prefViewportHeightProperty().bind(merda.heightProperty());
        popOver.setContentNode(scrollPane);

        ColumnConstraints boardColumn = new ColumnConstraints();
        boardColumn.setFillWidth(true);
        ColumnConstraints chatColumn = new ColumnConstraints();
        chatColumn.setHgrow(Priority.ALWAYS);
        chatColumn.setHalignment(HPos.CENTER);

        gridPane.getColumnConstraints().addAll(boardColumn, chatColumn);

        RowConstraints chatRow = new RowConstraints();
        chatRow.setPercentHeight(66);
        RowConstraints handRow = new RowConstraints();
        handRow.setPercentHeight(34);

        gridPane.getRowConstraints().addAll(chatRow, handRow);

        boardAnchor = new AnchorPane();

        // Load GameBoard imageview
        ClassLoader classLoader = this.getClass().getClassLoader();
        Image gameBoardImage = new Image(classLoader.getResourceAsStream("gameboard_scaled.png"));
        gameboardIV = new ImageView(gameBoardImage);
        gameboardIV.setSmooth(true);
        gameboardIV.setCache(true);
        gameboardIV.setPreserveRatio(true);
        gameboardIV.fitHeightProperty().bind(primaryStage.heightProperty());
        gameboardIV.setOnMouseEntered(event -> popOver.hide());

        //Nobility setup
        Image nobilityPath = new Image(classLoader.getResourceAsStream("nobility.png"));
        Canvas nobilityCanvas = new Canvas(1705,150);
        nobilityCanvas.getGraphicsContext2D().clearRect(0,0,1705,150);
        nobilityCanvas.getGraphicsContext2D().drawImage(nobilityPath, 0, 0, 1705, 150);
        WritableImage nobi = new WritableImage(1705,150);
        nobilityCanvas.snapshot(null,nobi);
        BufferedImage bi = SwingFXUtils.fromFXImage(nobi, null);
        Image image = SwingFXUtils.toFXImage(bi, null);
        ObjectImageView nobilityIV = new ObjectImageView(image, 0.04968196834915602, 0.8150382513661202, 0.68253772);
        boardObjects.add(nobilityIV);

        Image balcony1 = new Image(classLoader.getResourceAsStream("balcony.png"));
        ObjectImageView seaBalcony = new ObjectImageView(balcony1, 0.14323428884034265, 0.6991224489795919, 0.105586124657067);
        boardObjects.add(seaBalcony);

        CouncilorsBalcony balcony = new CouncilorsBalcony(RegionType.SEA);
        balcony.addCouncilor(new Councilor(CouncilColor.BLACK,0));
        balcony.addCouncilor(new Councilor(CouncilColor.PINK,0));
        balcony.addCouncilor(new Councilor(CouncilColor.PURPLE,0));
        balcony.addCouncilor(new Councilor(CouncilColor.WHITE,0));

        townsView = new HashMap<>();
        setBoardObjects();
        boardObjects.forEach(objectImageView -> setObjectGlow(objectImageView, borderglow, popOver));
        townsView.values().forEach(objectImageView -> setObjectGlow(objectImageView, borderglow, popOver));

        // Add Nodes to anchorPane
        boardAnchor.getChildren().add(gameboardIV);
        boardAnchor.getChildren().addAll(boardObjects);
        boardAnchor.getChildren().addAll(townsView.values());

        // Chat column Nodes
        Button dummyChat = new Button("I'm a dummy chat button");
        Button dummyHand = new Button("I'm a dummy hand button");
        dummyChat.setOnAction(event -> {
            balcony.addCouncilor(new Councilor(CouncilColor.CYAN,0));
            seaBalcony.setImage(BalconyDrawer.drawBalcony(balcony));
        });
        dummyHand.setOnAction(event -> {
            AbstractBonusFactory bonusFactory = BonusFactory.getFactory(BonusOwner.NOBILITY);
            List<List<Bonus>> bonusPath = new ArrayList<>(21);
            bonusPath.add(new ArrayList<>());
            for (int i = 1; i < 21; i++) {
                bonusPath.add(bonusFactory.generateBonuses());
            }
            NobilityPath path = new NobilityPath(bonusPath);
            NobilityDrawer drawer = new NobilityDrawer(path, image);
            nobilityIV.setImage(drawer.drawPath());
        });

        /********************* TESTING PLAYER'S HAND ***************************/
        PlayerView playerView = new PlayerView();
        Player test = new Player(null);
        test.setNickname("Lippy");
        test.setUsername("Leonardo Arcari");
        test.hireServants(Arrays.asList(new Servant(), new Servant(), new Servant()));
        test.addPermitCard(null);
        test.addPoliticsCard(new PoliticsCard(CouncilColor.ORANGE));
        test.addPoliticsCard(new PoliticsCard(CouncilColor.RAINBOW));
        test.addRoyalCard(RoyalCard.FIRST);
        test.addRoyalCard(RoyalCard.SECOND);
        test.addRoyalCard(RoyalCard.THIRD);
        test.addRoyalCard(RoyalCard.FOURTH);
        playerView.setPlayerProperty(test);
        GridPane.setConstraints(playerView.getPlayerNode(), 1, 0, 1, 1);
        /************************** END ****************************************/

        // Add Nodes to gridPane
        GridPane.setConstraints(boardAnchor, 0, 0, 1, 2);
        //GridPane.setConstraints(dummyChat, 1, 0, 1, 1);
        GridPane.setConstraints(dummyHand, 1, 1, 1, 1);
        gridPane.getChildren().addAll(boardAnchor, playerView.getPlayerNode(), dummyHand);

        // Scene & Stage setup
        Scene scene = new Scene(gridPane, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        scrollPane.prefViewportWidthProperty().bind(boardAnchor.widthProperty().multiply(1.5).multiply(boardObjects.get(1).getWidth()));

        // Set listeners
        boardObjects.forEach(objectImageView -> {
            setImageViewListener(objectImageView);
            setObjectConstraints(objectImageView);
        });

        townsView.values().forEach(objectImageView -> {
            setImageViewListener(objectImageView);
            setObjectConstraints(objectImageView);
        });

        // Debug
        gridPane.setGridLinesVisible(true);
        gameboardIV.setOnMouseClicked(event -> System.out.println("X scaled: " +
                event.getX()/gameboardIV.getBoundsInParent().getWidth() + " Y scaled " +
                event.getY()/ gameboardIV.getBoundsInParent().getHeight())
        );
    }

    private void setImageViewListener(ObjectImageView iv) {
        gameboardIV.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            iv.setFitWidth(iv.getWidth() * newValue.getWidth());
        });

        boardAnchor.heightProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setTopAnchor(iv, iv.getTopY() * gameboardIV.getBoundsInParent().getHeight());
            AnchorPane.setLeftAnchor(iv, iv.getLeftX() * gameboardIV.getBoundsInParent().getWidth());
        });
    }

    private void setObjectConstraints(ObjectImageView iv) {
        iv.setFitWidth(iv.getWidth() * gameboardIV.getBoundsInParent().getWidth());
        AnchorPane.setTopAnchor(iv, iv.getTopY() * gameboardIV.getBoundsInParent().getHeight());
        AnchorPane.setLeftAnchor(iv, iv.getLeftX() * gameboardIV.getBoundsInParent().getWidth());
    }

    private void setObjectGlow(ObjectImageView iv, Effect effect, PopOver popOver) {
        iv.setOnMouseEntered(event -> iv.setEffect(effect));
        iv.setOnMouseExited(event -> iv.setEffect(null));
        iv.setOnMouseClicked(event -> {
            ObjectImageView imageView = (ObjectImageView) event.getSource();
            if (AnchorPane.getLeftAnchor(imageView) < boardAnchor.widthProperty().getValue() * 1/4) {
                popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
            } else if (AnchorPane.getLeftAnchor(imageView) < boardAnchor.widthProperty().getValue() * 3/4) {
                popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
            } else {
                popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
            }
            if (imageView.getClass().equals(TownView.class)) {
                popOver.setContentNode(((TownView)imageView).getEmporiumNode());
                popOver.show(iv, 60);
            }
            else popOver.show(iv, 60);
        });
    }

    private void setBoardObjects() {
        // Town IVs
        Image aImage = new Image(classLoader.getResourceAsStream("a.png"));
        Image bImage = new Image(classLoader.getResourceAsStream("b.png"));
        Image cImage = new Image(classLoader.getResourceAsStream("c.png"));
        Image dImage = new Image(classLoader.getResourceAsStream("d.png"));
        Image eImage = new Image(classLoader.getResourceAsStream("e.png"));
        Image fImage = new Image(classLoader.getResourceAsStream("f.png"));
        Image gImage = new Image(classLoader.getResourceAsStream("g.png"));
        Image hImage = new Image(classLoader.getResourceAsStream("h.png"));
        Image iImage = new Image(classLoader.getResourceAsStream("i.png"));
        Image jImage = new Image(classLoader.getResourceAsStream("j.png"));
        Image kImage = new Image(classLoader.getResourceAsStream("k.png"));
        Image lImage = new Image(classLoader.getResourceAsStream("l.png"));
        Image mImage = new Image(classLoader.getResourceAsStream("m.png"));
        Image nImage = new Image(classLoader.getResourceAsStream("n.png"));
        Image oImage = new Image(classLoader.getResourceAsStream("o.png"));
        townsView.put(TownName.A, new TownView(TownName.A, 0.07257407407407407, 0.059109289617486336, 0.10459153122197, aImage));
        townsView.put(TownName.B, new TownView(TownName.B, 0.061042592592592594, 0.24180327868852458, 0.114425925925926, bImage));
        townsView.put(TownName.C, new TownView(TownName.C, 0.2155925925925926, 0.11958469945355191, 0.114583333333333, cImage));
        townsView.put(TownName.D, new TownView(TownName.D, 0.2084852504710498, 0.2786885245901639, 0.105321313772738, dImage));
        townsView.put(TownName.E, new TownView(TownName.E, 0.11026557621929187, 0.40846994535519127, 0.102691805475035, eImage));
        townsView.put(TownName.F, new TownView(TownName.F, 0.3811597344042335, 0.0868013698630137, 0.104449317367015, fImage));
        townsView.put(TownName.G, new TownView(TownName.G, 0.3948916963480114, 0.2467627118644068, 0.10285385614803205, gImage));
        townsView.put(TownName.H, new TownView(TownName.H, 0.40973005099866394, 0.3864406779661017, 0.09912460333496036, hImage));
        townsView.put(TownName.I, new TownView(TownName.I, 0.5466258390659746, 0.08813559322033898, 0.0967313203267906, iImage));
        townsView.put(TownName.J, new TownView(TownName.J, 0.5349027170062496, 0.2830508474576271, 0.0973074585507204, jImage));
        townsView.put(TownName.K, new TownView(TownName.K, 0.7117437356463746, 0.07401129943502825, 0.0999579670574619, kImage));
        townsView.put(TownName.L, new TownView(TownName.L, 0.684200387088455, 0.24884182660489743, 0.1078307727656072, lImage));
        townsView.put(TownName.M, new TownView(TownName.M, 0.6729745030117486, 0.416462482946794, 0.120203003974608, mImage));
        townsView.put(TownName.N, new TownView(TownName.N, 0.82539565232543, 0.16800354706684858, 0.113268215283765, nImage));
        townsView.put(TownName.O, new TownView(TownName.O, 0.829096739437645, 0.3542896174863388, 0.106006559623886, oImage));
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

    /*public void modifyBalcony(CouncilorsBalcony balcony) {
        RegionType type = balcony.getRegion();
        ObjectImageView balconyIV;
        if (type.equals(RegionType.SEA)) {
            balconyIV = seaBalcony;
        } else if (type.equals(RegionType.HILLS)) {
            balconyIV = hillsBalcony;
        } else if (type.equals(RegionType.MOUNTAINS)) {
            balconyIV = mountainsBalcony;
        } else balconyIV = boardBalcony;

        balconyIV.setImage(BalconyDrawer.drawBalcony(balcony));
    }*/

    public TownView getTownView(TownName name) {
        return townsView.get(name);
    }
}
