package client.View;

import client.CachedData;
import client.ControllerUI;
import core.Player;
import core.connection.GameBoardInterface;
import core.gamelogic.actions.*;
import core.gamemodel.*;
import core.gamemodel.Region;
import core.gamemodel.modelinterface.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
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
    private VictoryPathView victoryPath;
    private List<ObjectImageView> boardObjects;
    private Map<TownName, TownView> townsView;
    private Map<TownName, ObjectImageView> townBonusView;

    private PopOver townPopOver;

    private TreeView<String> actionChoice;
    private TreeItem<String> councilorPoolSelector;

    private CouncilorPoolView councilorPool;
    private BuySellableView buySellableView;
    private ExposeSellableView exposureView;

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
    private Label timer;
    private StringProperty timerProperty;
    private ChatView chatView;

    private BooleanProperty mainActionAvailable;
    private BooleanProperty fastActionAvailable;
    private BooleanProperty myTurn;

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
        myTurn = new SimpleBooleanProperty(false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
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
        buildVictory();

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
        TreeItem<String> choiceItem = new TreeItem<>("Make a choice");
        TreeItem<String> fastSelector = new TreeItem<>("Select Fast action");
        councilorPoolSelector = new TreeItem<>("See councilor pool");
        choiceItem.getChildren().addAll(fastSelector, councilorPoolSelector);
        actionChoice = new TreeView<>(choiceItem);

        fastActionsView = new FastActionsView();
        councilorPool = new CouncilorPoolView();

        actionChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(councilorPoolSelector)) {
                choicePane.setMasterNode(councilorPool.getFlowNode());
            } else {
                choicePane.setMasterNode(fastActionsView.getBoxNode());
            }
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

    private void buildVictory() {
        victoryPath = new VictoryPathView();
        pathPlayersListener(victoryPath);
    }

    private void pathPlayersListener(PathViewInterface path) {
        path.addListener(c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    boardAnchor.getChildren().removeAll(c.getRemoved());
                } if (c.wasAdded()) {
                    for (ObjectImageView view : c.getList()) {
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
        townsView = TownsWithBonusView.getInstance().getTownsView();
        townsView.values().forEach(townView -> setObjectGlow(townView, TownsWithBonusView.setShadowEffect()));
    }

    private void buildTownBonusViews() {
        townBonusView = TownsWithBonusView.getInstance().getTownBonusView();
        boardObjects.addAll(townBonusView.values());

    }

    private void setObjectGlow(TownView iv, Effect effect) {
        iv.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> iv.setEffect(effect));
        iv.addEventHandler(MouseEvent.MOUSE_EXITED, event -> iv.setEffect(null));
        iv.setOnMouseClicked(event -> {
            ObjectImageView imageView = (ObjectImageView) event.getSource();
            if (AnchorPane.getLeftAnchor(imageView) < boardAnchor.widthProperty().getValue() * 1/4) {
                iv.getTownPopOver().setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
            } else if (AnchorPane.getLeftAnchor(imageView) < boardAnchor.widthProperty().getValue() * 3/4) {
                iv.getTownPopOver().setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
            } else {
                iv.getTownPopOver().setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
            }
            if (imageView.getClass().equals(TownView.class)) {
                VBox vbox = (VBox) ((TownView)imageView).getTownPopOver().getContentNode();
                if(vbox.getChildren().size() == 3) vbox.getChildren().remove(2);
                ((TownView)imageView).getTownPopOver().show(imageView,10);
            }
            else iv.getTownPopOver().show(iv, 60);
        });
    }

    private void loadRoyalImages() {
        fifthRoyal = ImagesMaps.getInstance().getRoyal("fifth");
        fourthRoyal = ImagesMaps.getInstance().getRoyal("fourth");
        thirdRoyal = ImagesMaps.getInstance().getRoyal("third");
        secondRoyal = ImagesMaps.getInstance().getRoyal("second");
        firstRoyal = ImagesMaps.getInstance().getRoyal("first");
    }

    private void buildBonusCards() {
        seaBonusCard = new ObjectImageView(ImagesMaps.getInstance().getRegionBonus(RegionType.SEA),
                0.2522803334480774,0.5274207650273224,0.0634);
        hillsBonusCard = new ObjectImageView(ImagesMaps.getInstance().getRegionBonus(RegionType.HILLS),
                0.5490688645010539,0.5274207650273224,0.0634);
        mountainsBonusCard = new ObjectImageView(ImagesMaps.getInstance().getRegionBonus(RegionType.MOUNTAINS),
                0.8742918224146726,0.5274207650273224,0.0634);
        ironBonusCard = new ObjectImageView(ImagesMaps.getInstance().getTownTypeBonus(TownType.IRON),
                0.7444741305370091,0.8555858310626703,0.0634);
        bronzeBonusCard = new ObjectImageView(ImagesMaps.getInstance().getTownTypeBonus(TownType.BRONZE),
                0.7906430688648857,0.8478260869565217,0.0634);
        silverBonusCard = new ObjectImageView(ImagesMaps.getInstance().getTownTypeBonus(TownType.SILVER),
                0.8414289010255498,0.8410326086956522,0.0634);
        goldBonusCard = new ObjectImageView(ImagesMaps.getInstance().getTownTypeBonus(TownType.GOLD),
                0.8875978393534263,0.8355978260869565,0.0634);
        royalTopCard = new ObjectImageView(fifthRoyal, 0.8772098282296541,0.7547683923705722,0.0634);

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
        endTurn.disableProperty().bind(myTurn.not());
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
        endTurn.setOnMouseClicked(event -> controller.sendInfo(new EndTurnAction((Player) CachedData.getInstance().getMe())));
        timerProperty = new SimpleStringProperty("N / A");
        timer = new Label("");
        timer.setTextAlignment(TextAlignment.CENTER);
        timer.setStyle("color: #B4886B;\n" +
                "-fx-border-width: 3px;\n" +
                "-fx-border-color: black;\n" +
                "-fx-font-weight: normal;\n" +
                "-fx-border-insets: 3px");
        timer.textProperty().bind(timerProperty);

        HBox buttonBox = new HBox(10, endTurn, timer);
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
            fastActionsView.updateEnoughCoinProperty(wealthPath.getPlayerPosition((Player)CachedData.getInstance().getMe())>=3);
            CachedData.getInstance().setWealthPath(wealthPath);
        });
    }

    public void updateVictoryPath(VictoryPathInterface victory) {
        Platform.runLater(() -> victoryPath.updateVictoryPath(victory));
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
            exposureView = exposeSellableView;
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
        Platform.runLater(() -> {
            fastActionsView.updateEnoughServantsProperty(player.getServantsNumber()>=1,player.getServantsNumber()>=3);
            playerView.setPlayer(player);
            townsView.values().forEach(element -> element.setServantsAvailable(player.getServantsNumber()));
        });
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
            String senderString = sender.equals(CachedData.getInstance().getMe()) ? "Me" : sender.getNickname();
            String chatMessage = senderString + ": " + action.getMessage();
            chatView.append(chatMessage);
        });
    }

    public void yourTurn() {
        Platform.runLater(() -> {
            mainActionAvailable.setValue(true);
            fastActionAvailable.setValue(true);
            myTurn.setValue(true);
        });
    }

    public void endTurn() {
        Platform.runLater(() -> {
            mainActionAvailable.setValue(false);
            fastActionAvailable.setValue(false);
            myTurn.setValue(false);
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

    public void setTimer(String text) {
        Platform.runLater(() ->
            timerProperty.setValue(text)
        );
    }

    public void forceExposureEnd() {
        exposureView.setDisable(true);
    }

    public void forceBuyingEnd() {
        buySellableView.setDisable(true);
    }
}
