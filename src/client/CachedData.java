package client;

import core.gamemodel.*;
import core.gamemodel.modelinterface.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by leonardoarcari on 09/06/16.
 */
public class CachedData {
    private volatile static CachedData instance = null;

    private ControllerUI controller;
    private PlayerInterface me;
    private ObservableList<PermitCard> myPermitCards;
    private Map<TownName, TownInterface> towns;
    private Map<RegionType, BalconyInterface> balconies;
    private WealthPathInterface wealthPath;
    private NobilityPathInterface nobilityPath;
    private VictoryPathInterface victoryPath;
    private List<Councilor> councilorPool;
    private Councilor selectedCouncilor;
    private BooleanProperty isCouncilorSelected;
    private ObservableList<PoliticsCard> playerPoliticsCards;
    private RegionInterface seaRegion;
    private RegionInterface hillsRegion;
    private RegionInterface mountainsRegion;
    private ShowcaseInterface showcase;

    private BooleanProperty mainActionAvailable;
    private BooleanProperty fastActionAvailable;
    private BooleanProperty myTurn;

    private Font customFont;

    public static CachedData getInstance() {
        if (instance == null) {
            synchronized (CachedData.class) {
                if (instance == null) {
                    instance = new CachedData();
                }
            }
        }
        return instance;
    }

    private CachedData() {
        me = null;
        towns = new HashMap<>(15);
        balconies = new HashMap<>(4);
        councilorPool = new ArrayList<>();
        wealthPath = null;
        selectedCouncilor = null;
        isCouncilorSelected = new SimpleBooleanProperty(false);
        playerPoliticsCards = FXCollections.observableArrayList();
        myPermitCards = FXCollections.observableArrayList();
        seaRegion = null;
        hillsRegion = null;
        mountainsRegion = null;
        mainActionAvailable = new SimpleBooleanProperty(false);
        fastActionAvailable = new SimpleBooleanProperty(false);
        myTurn = new SimpleBooleanProperty(false);

        customFont = Font.loadFont(this.getClass().getClassLoader().getResourceAsStream("KnightsQuest.ttf"), 22f);
    }

    public ControllerUI getController() {
        return controller;
    }

    public void setController(ControllerUI controller) {
        this.controller = controller;
    }

    public void setWealthPath(WealthPathInterface wealthPath) {
        this.wealthPath = wealthPath;
    }

    public void setNobilityPath(NobilityPathInterface nobilityPath) {
        this.nobilityPath = nobilityPath;
    }

    public void setVictoryPath(VictoryPathInterface victoryPath) {
        this.victoryPath = victoryPath;
    }

    public WealthPathInterface getWealthPath() {
        return wealthPath;
    }

    public NobilityPathInterface getNobilityPath() {
        return nobilityPath;
    }

    public VictoryPathInterface getVictoryPath() {
        return victoryPath;
    }

    public PlayerInterface getMe() {
        return me;
    }

    public void setMe(PlayerInterface me) {
        this.me = me;
        myPermitCards.clear();
        playerPoliticsCards.clear();
        List<PermitCard> permitCardsTemp = new ArrayList<>();
        Iterator<PermitCard> permitIterator = this.me.permitCardIterator();
        while(permitIterator.hasNext()) {
            permitCardsTemp.add(permitIterator.next());
        }
        myPermitCards.addAll(permitCardsTemp);

        List<PoliticsCard> tempPolitics = new ArrayList<>();
        this.me.politicsCardIterator().forEachRemaining(tempPolitics::add);
        playerPoliticsCards.addAll(tempPolitics);
    }

    public void putTown(TownName townName, TownInterface town) {
        towns.put(townName, town);
    }

    public TownInterface getTown(TownName townName) {
        return towns.get(townName);
    }

    public Map<TownName, TownInterface> getTowns() {
        return towns;
    }

    public void putBalcony(RegionType region, BalconyInterface balcony) {
        balconies.put(region, balcony);
    }

    public BalconyInterface getBalcony(RegionType region) {
        return balconies.get(region);
    }

    public Iterator<Councilor> getCouncilorPool() {
        return councilorPool.iterator();
    }

    public void setCouncilorPool(List<Councilor> councilorPool) {
        this.councilorPool = councilorPool;
    }

    public void listenToPolitics(ListChangeListener<? super PoliticsCard> listener) {
        playerPoliticsCards.addListener(listener);
    }

    public void listenToPermits(ListChangeListener<? super PermitCard> listener) {
        myPermitCards.addListener(listener);
    }

    public ObservableList<PoliticsCard> getPlayerPoliticsCards() {
        return playerPoliticsCards;
    }

    public ObservableList<PermitCard> getMyPermitCards() {
        return myPermitCards;
    }

    public Councilor getSelectedCouncilor() {
        return selectedCouncilor;
    }

    public BooleanProperty isCouncilorSelectedProperty() {
        return isCouncilorSelected;
    }

    public void setIsCouncilorSelected(boolean isCouncilorSelected) {
        this.isCouncilorSelected.set(isCouncilorSelected);
    }

    public void setSelectedCouncilor(Councilor selectedCouncilor) {
        this.selectedCouncilor = selectedCouncilor;
    }

    public RegionInterface getSeaRegion() {
        return seaRegion;
    }

    public RegionInterface getHillsRegion() {
        return hillsRegion;
    }

    public RegionInterface getMountainsRegion() {
        return mountainsRegion;
    }

    public void setSeaRegion(RegionInterface seaRegion) {
        this.seaRegion = seaRegion;
    }

    public void setHillsRegion(RegionInterface hillsRegion) {
        this.hillsRegion = hillsRegion;
    }

    public void setMountainsRegion(RegionInterface mountainsRegion) {
        this.mountainsRegion = mountainsRegion;
    }

    public ShowcaseInterface getShowcase() {
        return showcase;
    }

    public void setShowcase(ShowcaseInterface showcase) {
        this.showcase = showcase;
    }

    public boolean getMainActionAvailable() {
        return mainActionAvailable.get();
    }

    public BooleanProperty mainActionAvailableProperty() {
        return mainActionAvailable;
    }

    public void setMainActionAvailable(boolean mainActionAvailable) {
        this.mainActionAvailable.set(mainActionAvailable);
    }

    public boolean getFastActionAvailable() {
        return fastActionAvailable.get();
    }

    public BooleanProperty fastActionAvailableProperty() {
        return fastActionAvailable;
    }

    public void setFastActionAvailable(boolean fastActionAvailable) {
        this.fastActionAvailable.set(fastActionAvailable);
    }

    public boolean getMyTurn() {
        return myTurn.get();
    }

    public BooleanProperty myTurnProperty() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn.set(myTurn);
    }

    public Font getCustomFont() {
        return customFont;
    }
}
