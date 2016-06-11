package client.View;

import client.CachedData;
import client.ControllerUI;
import core.Player;
import core.connection.GameBoardInterface;
import core.gamelogic.actions.ChatAction;
import core.gamelogic.actions.EndTurnAction;
import core.gamelogic.actions.PlayerInfoAction;
import core.gamelogic.actions.SelectAgainPermitAction;
import core.gamemodel.*;
import core.gamemodel.Region;
import core.gamemodel.modelinterface.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.PopOver;

import java.util.*;

/**
 * Created by Leonardo Arcari on 31/05/2016.
 */
public class GUI extends Application {
    private ClassLoader classLoader;
    private ControllerUI controller;
    private Stage primaryStage;
    private Scene scene;

    private BorderPane loginPane;
    private TextField username;
    private TextField nickname;
    private GridPane gridPane;
    private ImageView gameboardIV;
    private AnchorPane boardAnchor;
    private TabPane tabPane;

    private MasterDetailPane choicePane;

    private PlayerView playerView;
    private BalconyView seaBalcony;
    private BalconyView hillsBalcony;
    private BalconyView mountainsBalcony;
    private BalconyView boardBalcony;
    private FastActionsView fastActionsView;

    private WealthPathView wealthPath;
    private NobilityPathView nobilityPath;
    private List<ObjectImageView> boardObjects;
    private Map<TownName, TownView> townsView;
    private Map<TownName, ObjectImageView> townBonusView;

    private PopOver townPopOver;
    private Effect borderGlow;

    private TreeView<String> actionChoice;
    private TreeItem<String> choiceItem;
    private TreeItem<String> fastSelector;
    private TreeItem<String> servantsPoolSelector;
    private TreeItem<String> councilorPoolSelector;

    private CouncilorPoolView councilorPool;
    private BuySellableView buySellableView;

    private PermitCardView seaLeftCard;
    private PermitCardView seaRightCard;
    private PermitCardView hillsLeftCard;
    private PermitCardView hillsRightCard;
    private PermitCardView mountainsLeftCard;
    private PermitCardView mountainsRightCard;

    private ObjectImageView seaBonusCard;
    private ObjectImageView hillsBonusCard;
    private ObjectImageView mountainsBonusCard;
    private ObjectImageView ironBonusCard;
    private ObjectImageView bronzeBonusCard;
    private ObjectImageView silverBonusCard;
    private ObjectImageView goldBonusCard;
    private ObjectImageView royalTopCard;

    private Image fifthRoyal;
    private Image fourthRoyal;
    private Image thirdRoyal;
    private Image secondRoyal;
    private Image firstRoyal;

    private Button endTurn;
    private ChatView chatView;

    private BooleanProperty mainActionAvailable;
    private BooleanProperty fastActionAvailable;

    @Override
    public void init() throws Exception {
        super.init();
        boardObjects = new ArrayList<>();
        townsView = new HashMap<>();
        townBonusView = new HashMap<>();
        classLoader = this.getClass().getClassLoader();
        controller = new ControllerUI(this);
        CachedData.getInstance().setController(controller);
        mainActionAvailable = new SimpleBooleanProperty(false);
        fastActionAvailable = new SimpleBooleanProperty(false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        borderGlow = setShadowEffect();
        townPopOver = new PopOver();

        buildLoginPane();
        buildGameGUI();

        scene = new Scene(loginPane, 500, 200);

        // Scene & Stage setup
        primaryStage.setScene(scene);
        primaryStage.setTitle("The Council of Four");
        primaryStage.show();
    }

    //Build methods
    private void buildLoginPane() {
        loginPane = new BorderPane();
        loginPane.setPadding(new Insets(30, 30, 30, 30));

        username = new TextField();
        username.setPromptText("Insert username");
        nickname = new TextField();
        nickname.setPromptText("Insert nickname");

        VBox textFieldBox = new VBox(20);
        textFieldBox.setAlignment(Pos.TOP_CENTER);
        textFieldBox.getChildren().addAll(username, nickname);

        Button loginBtn = new Button("Login");
        loginBtn.disableProperty().bind(Bindings.or(username.textProperty().isEmpty(), nickname.textProperty().isEmpty()));
        ChoiceBox<String> cb = new ChoiceBox<>(FXCollections.observableArrayList("Socket", "RMI"));
        cb.getSelectionModel().clearAndSelect(0);

        GridPane bottomPane = new GridPane();
        GridPane.setConstraints(cb, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER);
        GridPane.setConstraints(loginBtn, 1, 0, 1, 1, HPos.RIGHT, VPos.CENTER);
        GridPane.setHgrow(cb, Priority.ALWAYS);
        GridPane.setHgrow(loginBtn, Priority.ALWAYS);
        bottomPane.getChildren().addAll(cb, loginBtn);

        loginPane.setCenter(textFieldBox);
        loginPane.setBottom(bottomPane);

        loginBtn.setOnAction(event -> {
            loginPane.setDisable(true);
            if (cb.getSelectionModel().getSelectedItem().equals("Socket")) {
                controller.socketConnection();
            } else {
                controller.rmiConnection();
            }
        });
    }

    private void buildGameGUI() {
        //Creates the gridPane
        buildMainPane();
        boardAnchor = new AnchorPane();

        //Choice tree (and children) setup
        buildActionTree();
        buildMasterDetailPane();

        //Load GameBoard imageview
        buildGameboard();

        //Paths setup
        buildNobility();
        buildWealthPath();

        //Balconies setup
        buildBalconies();

        //PermitCards setup
        buildPermitsModel();

        //Towns setup
        buildTownViews();
        buildTownBonusViews();

        //Bonus Cards setup
        loadRoyalImages();
        buildBonusCards();

        //Add Nodes to anchorPane
        boardAnchor.getChildren().add(gameboardIV);
        boardAnchor.getChildren().addAll(townsView.values());
        boardAnchor.getChildren().addAll(boardObjects);

        // Side bar nodes
        chatView = new ChatView();
        playerView = new PlayerView();
        buildTabPane();

        // Add Nodes to gridPane
        GridPane.setConstraints(boardAnchor, 0, 0, 1, 2);
        GridPane.setConstraints(tabPane, 1, 0, 1, 1);
        GridPane.setConstraints(chatView, 1, 1, 1, 1);
        gridPane.getChildren().addAll(boardAnchor, tabPane, chatView);

        buySellableView = new BuySellableView();

        // Set listeners
        townsView.values().forEach(townView -> {
            setImageViewListener(townView);
            setObjectConstraints(townView);
        });
        boardObjects.forEach(objectImageView -> {
            setImageViewListener(objectImageView);
            setObjectConstraints(objectImageView);
        });

        bindDisableProperties();

        // Debug
        gridPane.setGridLinesVisible(true);
        gameboardIV.setOnMouseClicked(event -> System.out.println("X scaled: " +
                event.getX()/gameboardIV.getBoundsInParent().getWidth() + " Y scaled " +
                event.getY()/ gameboardIV.getBoundsInParent().getHeight())
        );
    }

    private void buildMainPane() {
        gridPane = new GridPane();
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
    }

    private void buildActionTree() {
        choiceItem = new TreeItem<>("Make a choice");
        fastSelector = new TreeItem<>("Select Fast action");
        servantsPoolSelector = new TreeItem<>("See servants pool");
        councilorPoolSelector = new TreeItem<>("See councilor pool");
        choiceItem.getChildren().addAll(fastSelector, servantsPoolSelector, councilorPoolSelector);
        actionChoice = new TreeView<>(choiceItem);

        fastActionsView = new FastActionsView();
        councilorPool = new CouncilorPoolView();

        actionChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(councilorPoolSelector)) {
                choicePane.setMasterNode(councilorPool.getFlowNode());
            } else if(newValue.equals(fastSelector)) {
                choicePane.setMasterNode(fastActionsView.getBoxNode());
            } else choicePane.setMasterNode(new Label("ciao"));
            choicePane.setDividerPosition(0.3);
        });
    }

    private void buildMasterDetailPane() {
        choicePane = new MasterDetailPane();
        choicePane.setDetailNode(actionChoice);
        choicePane.setDetailSide(Side.TOP);
        choicePane.setDividerPosition(0.1);
        choicePane.setShowDetailNode(true);

        choicePane.dividerPositionProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.doubleValue()>0.5) choicePane.setDividerPosition(0.4);  //Fix divider position
        }));

    }

    private void buildGameboard() {
        Image gameBoardImage = new Image(classLoader.getResourceAsStream("gameboard_scaled.png"));
        gameboardIV = new ImageView(gameBoardImage);
        gameboardIV.setSmooth(true);
        gameboardIV.setCache(true);
        gameboardIV.setPreserveRatio(true);
        gameboardIV.fitHeightProperty().bind(primaryStage.heightProperty());
        gameboardIV.setOnMouseEntered(event -> townPopOver.hide());
    }

    private void buildNobility() {
        Image nobilityImage = new Image(classLoader.getResourceAsStream("nobility.png"));
        nobilityPath = new NobilityPathView(nobilityImage, 0.04968196834915602, 0.8150382513661202, 0.68253772);
        pathPlayersListener(nobilityPath);
        boardObjects.add(nobilityPath);
    }

    private void buildWealthPath() {
        wealthPath = new WealthPathView();
        pathPlayersListener(wealthPath);
    }

    private void pathPlayersListener(PathViewInterface path) {
        path.addListener(c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    boardAnchor.getChildren().removeAll(c.getRemoved());
                } if (c.wasAdded()) {
                    Iterator<? extends ObjectImageView> iterator = c.getList().iterator();
                    while (iterator.hasNext()) {
                        ObjectImageView view = iterator.next();
                        setObjectConstraints(view);
                        setImageViewListener(view);
                        boardAnchor.getChildren().add(view);
                    }
                }
            }
        });
    }

    private void buildBalconies() {
        Image fullBalcony = new Image(classLoader.getResourceAsStream("fullbalcony.png"));
        seaBalcony = new BalconyView(fullBalcony,RegionType.SEA, 0.14323428884034265, 0.6991224489795919, 0.105586124657067);
        hillsBalcony = new BalconyView(fullBalcony,RegionType.HILLS, 0.44091336103122086, 0.6991224489795919, 0.105586124657067);
        mountainsBalcony = new BalconyView(fullBalcony,RegionType.MOUNTAINS, 0.7710212700755381, 0.6991224489795919, 0.105586124657067);
        boardBalcony = new BalconyView(fullBalcony,RegionType.KINGBOARD, 0.631653891146887, 0.7402176870748299, 0.105586124657067);
        boardObjects.addAll(Arrays.asList(seaBalcony, hillsBalcony, mountainsBalcony, boardBalcony));
    }

    private void buildPermitsModel() {
        Image card = new Image(classLoader.getResourceAsStream("permitCard.png"));
        seaLeftCard = new PermitCardView(card,0.1430406026492049,0.6052670299727521,0.068);
        seaRightCard = new PermitCardView(card,0.22105902121530668,0.6052670299727521,0.068);
        hillsLeftCard = new PermitCardView(card,0.44096066535618766,0.6052670299727521,0.068);
        hillsRightCard = new PermitCardView(card,0.5150327981194318,0.6052670299727521,0.068);
        mountainsLeftCard = new PermitCardView(card,0.7696557544930834,0.6052670299727521,0.068);
        mountainsRightCard = new PermitCardView(card,0.8448852643307533,0.6052670299727521,0.068);
        permitZoomListener(seaLeftCard);
        permitZoomListener(seaRightCard);
        permitZoomListener(hillsLeftCard);
        permitZoomListener(hillsRightCard);
        permitZoomListener(mountainsLeftCard);
        permitZoomListener(mountainsRightCard);
        boardObjects.addAll(Arrays.asList(seaLeftCard,seaRightCard,hillsLeftCard,hillsRightCard,mountainsLeftCard,mountainsRightCard));
    }

    private void permitZoomListener(PermitCardView permitCardView) {
        permitCardView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                if (!permitCardView.getMyPopover().isShowing())
                    permitCardView.getMyPopover().show(permitCardView, 20);
                else permitCardView.getMyPopover().hide(new Duration(100));
            }
        });
    }

    private void buildTownViews () {
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

        townsView.values().forEach(townView -> setObjectGlow(townView, borderGlow, townPopOver));
    }

    private void buildTownBonusViews() {
        townBonusView.put(TownName.A,new ObjectImageView(null,0.08028267559927046,0.07499708049113233,0.037));
        townBonusView.put(TownName.B,new ObjectImageView(null,0.07129208879328723,0.24587339699863574,0.037));
        townBonusView.put(TownName.C,new ObjectImageView(null,0.22129004564640675,0.12388308321964529,0.037));
        townBonusView.put(TownName.D,new ObjectImageView(null,0.21219945884042354,0.28812960436562074,0.037));
        townBonusView.put(TownName.E,new ObjectImageView(null,0.11973560962918661,0.41191268758526605,0.037));
        townBonusView.put(TownName.F,new ObjectImageView(null,0.38903657045691414,0.08858390177353342,0.037));
        townBonusView.put(TownName.G,new ObjectImageView(null,0.39681556103261644,0.24956616643929058,0.037));
        townBonusView.put(TownName.H,new ObjectImageView(null,0.41330833087486385,0.3880845839017735,0.037));
        townBonusView.put(TownName.I,new ObjectImageView(null,0.5485367103462978,0.08994815825375171,0.037));
        townBonusView.put(TownName.K,new ObjectImageView(null,0.7146600426962434,0.07976261937244201,0.037));
        townBonusView.put(TownName.L,new ObjectImageView(null,0.6872324841631532,0.2598444747612551,0.037));
        townBonusView.put(TownName.M,new ObjectImageView(null,0.676830301126889,0.4351050477489768,0.037));
        townBonusView.put(TownName.N,new ObjectImageView(null,0.8409536334768346,0.17562482946793997,0.037));
        townBonusView.put(TownName.O,new ObjectImageView(null,0.8455768259373965,0.35770668485675305,0.037));

        boardObjects.addAll(townBonusView.values());

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
                ((TownView)imageView).getTownPopOver().show(iv, 60);
            }
            else popOver.show(iv, 60);
        });
    }

    private void loadRoyalImages() {
        ClassLoader loader = this.getClass().getClassLoader();
        fifthRoyal = new Image(loader.getResourceAsStream("fifthRoyal.png"));
        fourthRoyal = new Image(loader.getResourceAsStream("fourthRoyal.png"));
        thirdRoyal = new Image(loader.getResourceAsStream("thirdRoyal.png"));
        secondRoyal = new Image(loader.getResourceAsStream("secondRoyal.png"));
        firstRoyal = new Image(loader.getResourceAsStream("firstRoyal.png"));
    }

    private void buildBonusCards() {
        ClassLoader loader = this.getClass().getClassLoader();
        seaBonusCard = new ObjectImageView(selectRegionCardImage(RegionType.SEA),
                0.2522803334480774,0.5274207650273224,0.0634);
        hillsBonusCard = new ObjectImageView(selectRegionCardImage(RegionType.HILLS),
                0.5490688645010539,0.5274207650273224,0.0634);
        mountainsBonusCard = new ObjectImageView(selectRegionCardImage(RegionType.MOUNTAINS),
                0.8742918224146726,0.5274207650273224,0.0634);
        ironBonusCard = new ObjectImageView(new Image(loader.getResourceAsStream("ironBonus.png")),
                0.7444741305370091,0.8555858310626703,0.0634);
        bronzeBonusCard = new ObjectImageView(new Image(loader.getResourceAsStream("bronzeBonus.png")),
                0.7906430688648857,0.8478260869565217,0.0634);
        silverBonusCard = new ObjectImageView(new Image(loader.getResourceAsStream("silverBonus.png")),
                0.8414289010255498,0.8410326086956522,0.0634);
        goldBonusCard = new ObjectImageView(new Image(loader.getResourceAsStream("goldBonus.png")),
                0.8875978393534263,0.8355978260869565,0.0634);
        royalTopCard = new ObjectImageView(new Image(loader.getResourceAsStream("fifthRoyal.png")),
                0.8772098282296541,0.7547683923705722,0.0634);

        boardObjects.addAll(Arrays.asList(seaBonusCard,hillsBonusCard,mountainsBonusCard,
                ironBonusCard,bronzeBonusCard,silverBonusCard,goldBonusCard,royalTopCard));
    }

    private void bindDisableProperties() {
        // Main Action
        seaBalcony.setDisableBindingMainAction(mainActionAvailable);
        hillsBalcony.setDisableBindingMainAction(mainActionAvailable);
        mountainsBalcony.setDisableBindingMainAction(mainActionAvailable);
        boardBalcony.setDisableBindingMainAction(mainActionAvailable);
        councilorPool.setDisableBindingMainAction(mainActionAvailable);
        townsView.values().forEach(townView -> townView.setDisableBindingMainAction(mainActionAvailable));

        // Fast Action
        seaBalcony.setDisableBindingFastAction(fastActionAvailable);
        hillsBalcony.setDisableBindingFastAction(fastActionAvailable);
        mountainsBalcony.setDisableBindingFastAction(fastActionAvailable);
        boardBalcony.setDisableBindingFastAction(fastActionAvailable);
        councilorPool.setDisableBindingFastAction(fastActionAvailable);
        fastActionsView.setDisableBindingFastAction(fastActionAvailable);

        // Not my turn
        endTurn.disableProperty().bind(Bindings.and(mainActionAvailable.not(), fastActionAvailable.not()));
    }

    public static Image selectRegionCardImage(RegionType type) {
        ClassLoader loader = GUI.class.getClassLoader();
        if(type.equals(RegionType.SEA)) return new Image(loader.getResourceAsStream("seaBonus.png"));
        else if(type.equals(RegionType.HILLS)) return new Image(loader.getResourceAsStream("hillsBonus.png"));
        else return new Image(loader.getResourceAsStream("mountainsBonus.png"));
    }

    private void buildTabPane() {
        tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        Tab playerHandTab = new Tab("Your Hand");
        playerHandTab.setClosable(false);
        playerHandTab.setContent(buildPlayerHand());
        Tab actionTreeTab = new Tab("Actions and Gameboard");
        actionTreeTab.setClosable(false);
        actionTreeTab.setContent(choicePane);
        tabPane.getTabs().addAll(playerHandTab, actionTreeTab);
    }

    private Pane buildPlayerHand() {
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(true);
        endTurn = new Button("End Turn");
        endTurn.setOnMouseClicked(event -> {
            controller.sendInfo(new EndTurnAction((Player) CachedData.getInstance().getMe()));
        });

        HBox buttonBox = new HBox(endTurn);
        buttonBox.setPadding(new Insets(20, 0, 20, 0));
        buttonBox.setAlignment(Pos.CENTER);
        GridPane.setFillHeight(playerView.getPlayerNode(), true);
        GridPane.setConstraints(playerView.getPlayerNode(), 0, 0, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);

        GridPane.setConstraints(buttonBox, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        pane.getChildren().addAll(playerView.getPlayerNode(), buttonBox);
        return pane;
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

    //Update methods
    public void updateBalcony(BalconyInterface balcony) {
        Platform.runLater(() -> {
            RegionType type = balcony.getRegion();
            BalconyView balconyIV;
            if(type.equals(RegionType.SEA)) {
                balconyIV = seaBalcony;
            } else if(type.equals(RegionType.HILLS)) {
                balconyIV = hillsBalcony;
            } else if(type.equals(RegionType.MOUNTAINS)) {
                balconyIV = mountainsBalcony;
            } else balconyIV = boardBalcony;
            balconyIV.setBalcony(balcony);
        });
    }

    public void updateNobilityPath(NobilityPathInterface nobility) {
        Platform.runLater(() -> nobilityPath.updateNobilityPath(nobility));
    }

    public void updateWealthPath(WealthPathInterface wealthPath) {
        Platform.runLater(() -> {
            this.wealthPath.updateWealthPath(wealthPath);
            CachedData.getInstance().setWealthPath(wealthPath);
        });
    }

    public TownView getTownView(TownName name) {
        return townsView.get(name);
    }

    public void populateTownBonus(TownInterface town) {
        if(town.getTownName().equals(TownName.J)) return;
        if(townBonusView.get(town.getTownName()).getImage() == null) {
            String className = town.getTownBonus().getClass().getName();
            String toLoad;
            toLoad = className.substring(className.lastIndexOf(".") + 1).toLowerCase();
            toLoad = "BonusImages/" + toLoad + "_" + town.getTownBonus().getValue() + ".png";
            townBonusView.get(town.getTownName()).setImage(new Image(classLoader.getResourceAsStream(toLoad)));
        }
    }

    public void updatePermitCard(RegionInterface region) {
        Platform.runLater(() -> {
            RegionType type = region.getRegionType();
            PermitCardView left;
            PermitCardView right;
            if(type.equals(RegionType.SEA)) {
                CachedData.getInstance().setSeaRegion((Region)region);
                left = seaLeftCard;
                right = seaRightCard;
            } else if(type.equals(RegionType.HILLS)) {
                CachedData.getInstance().setHillsRegion((Region)region);
                left = hillsLeftCard;
                right = hillsRightCard;
            } else {
                CachedData.getInstance().setMountainsRegion((Region)region);
                left = mountainsLeftCard;
                right = mountainsRightCard;
            }

            left.setPermitCard(region.getLeftPermitCard());
            right.setPermitCard(region.getRightPermitCard());
        });
    }

    public void updateRegionBonus(RegionInterface regionInterface) {
        Platform.runLater(() -> {
            RegionType type = regionInterface.getRegionType();
            if(type.equals(RegionType.SEA)) {
                if(seaBonusCard.getImage() != null && regionInterface.isRegionCardTaken())
                    seaBonusCard.setImage(null);}
            else if(type.equals(RegionType.HILLS)) {
                if(hillsBonusCard.getImage() != null && regionInterface.isRegionCardTaken())
                    hillsBonusCard.setImage(null);}
            else {
                if(mountainsBonusCard.getImage() != null && regionInterface.isRegionCardTaken())
                    mountainsBonusCard.setImage(null);}
        });

    }

    public void updateGameBoardData(GameBoardInterface gameBoardInterface) {
        Platform.runLater(() -> {
            updateRoyalCards(gameBoardInterface);
            updateCouncilorsPool(gameBoardInterface);
            updateTownTypeCard(gameBoardInterface);
        });
    }

    private void updateRoyalCards(GameBoardInterface gameboard) {
        Platform.runLater(() -> {
            Iterator<RoyalCard> royalCardIterator = gameboard.royalCardIterator();
            if(!royalCardIterator.hasNext()) {
                royalTopCard.setImage(null);
                return;
            }

            RoyalCard royalTop = null;
            while(royalCardIterator.hasNext()) {
                royalTop = royalCardIterator.next();
            }
            int identifier = royalTop.getRoyalBonus().getValue();
            Image currentImage;
            if(identifier == 3) currentImage = fifthRoyal;
            else if(identifier == 7) currentImage = fourthRoyal;
            else if(identifier == 12) currentImage = thirdRoyal;
            else if(identifier == 18) currentImage = secondRoyal;
            else currentImage = firstRoyal;
            royalTopCard.setImage(currentImage);
        });

    }

    private void updateCouncilorsPool(GameBoardInterface gameboard) {
        Platform.runLater(() -> {
            Iterator<Councilor> councilorIterator = gameboard.councilorIterator();
            List<Councilor> councilorList = new Vector<>();
            while(councilorIterator.hasNext()) {
                councilorList.add(councilorIterator.next());
            }
            councilorPool.setPool(councilorList);
        });
    }

    private void updateTownTypeCard(GameBoardInterface gameboard) {
        Platform.runLater(() -> {
            List<TownType> allTypes = new ArrayList<>(Arrays.asList(TownType.values()));
            allTypes.remove(TownType.KING); //I need all the town types except for Javier's one!
            List<TownType> boardTypes = new ArrayList<>();

            Iterator<TownTypeCard> townTypeCardIterator = gameboard.townTypeCardIterator();
            while(townTypeCardIterator.hasNext()) {
                TownType cardType = townTypeCardIterator.next().getTownType();
                boardTypes.add(cardType);
            }
            allTypes.removeAll(boardTypes);

            for(TownType type : allTypes) {
                if(type.equals(TownType.IRON)) {
                    if(ironBonusCard.getImage() != null) ironBonusCard.setImage(null);
                } else if(type.equals(TownType.BRONZE)) {
                    if(bronzeBonusCard.getImage() != null) bronzeBonusCard.setImage(null);
                } else if(type.equals(TownType.SILVER)) {
                    if(silverBonusCard.getImage() != null) silverBonusCard.setImage(null);
                } else if(goldBonusCard.getImage() != null) goldBonusCard.setImage(null);
            }
        });
    }

    public void updateShowCase(ShowcaseInterface showcase) {
        Platform.runLater(() -> {
            CachedData.getInstance().setShowcase(showcase);
            buySellableView.clearOnSaleItem();
            CachedData.getInstance().getShowcase().onSaleItemIterator().forEachRemaining(buySellableView::addOnSaleItem);
        });
    }

    public void showExposeView() {
        Platform.runLater(() -> {
            ExposeSellableView exposeSellableView = new ExposeSellableView();
            CachedData.getInstance().getMe().permitCardIterator().forEachRemaining(exposeSellableView::addSellableItem);
            CachedData.getInstance().getMe().politicsCardIterator().forEachRemaining(exposeSellableView::addSellableItem);
            for (int i = 0; i < CachedData.getInstance().getMe().getServantsNumber(); i++) {
                exposeSellableView.addSellableItem(new Servant());
            }
            exposeSellableView.setDisable(false);
            ShowPane.getInstance().setContent(exposeSellableView);
            ShowPane.getInstance().show();
        });
    }

    public void showBuyItemView() {
        Platform.runLater(() -> {
            ShowPane.getInstance().setContent(buySellableView);
            buySellableView.setDisable(false);
            ShowPane.getInstance().show();
        });
    }

    public void hideMarket() {
        Platform.runLater(() -> ShowPane.getInstance().hide());
    }

    public void updatePlayer(PlayerInterface player) {
        Platform.runLater(() -> playerView.setPlayer(player));
    }

    public void startGame() {
        Platform.runLater(() -> {
            scene = new Scene(gridPane, 1280, 800);
            ShowPane.getInstance().setSceneAndParent(scene,gridPane);
            primaryStage.setScene(scene);
            controller.sendInfo(new PlayerInfoAction((Player) playerView.getPlayer(), username.getText(),
                    nickname.getText()));
        });
    }

    public void appendChatMessage(ChatAction action) {
        Platform.runLater(() -> {
            Player sender = action.getPlayer();
            String senderString = sender.equals((Player)CachedData.getInstance().getMe()) ? "Me" : sender.getNickname();
            String chatMessage = senderString + ": " + action.getMessage();
            chatView.append(chatMessage);
        });
    }

    public void yourTurn() {
        Platform.runLater(() -> {
            mainActionAvailable.setValue(true);
            fastActionAvailable.setValue(true);
        });
    }

    public void setMainActionAvailable(boolean mainActionAvailable) {
        Platform.runLater(() -> this.mainActionAvailable.setValue(mainActionAvailable));
    }

    public void setFastActionAvailable(boolean fastActionAvailable) {
        Platform.runLater(() -> this.fastActionAvailable.setValue(fastActionAvailable));
    }

    public void showRedeemPermitView() {
        Platform.runLater(() -> {
            RedeemPermitView permitView = new RedeemPermitView(CachedData.getInstance().getMe());
            permitView.addClickListener(event -> {
                controller.sendInfo(
                        new SelectAgainPermitAction(
                                (Player) CachedData.getInstance().getMe(),
                                ((PermitCardView) event.getSource()).getPermitCard()
                        )
                );
                ShowPane.getInstance().hide();
            });
            ShowPane.getInstance().setContent(permitView);
            ShowPane.getInstance().show();
        });
    }
}
