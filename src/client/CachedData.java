package client;

import core.gamemodel.Councilor;
import core.gamemodel.PoliticsCard;
import core.gamemodel.RegionType;
import core.gamemodel.TownName;
import core.gamemodel.modelinterface.BalconyInterface;
import core.gamemodel.modelinterface.PlayerInterface;
import core.gamemodel.modelinterface.TownInterface;
import core.gamemodel.modelinterface.WealthPathInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * Created by leonardoarcari on 09/06/16.
 */
public class CachedData {
    private volatile static CachedData instance = null;

    private ControllerUI controller;
    private PlayerInterface me;
    private Map<TownName, TownInterface> towns;
    private Map<RegionType, BalconyInterface> balconies;
    private WealthPathInterface wealthPath;
    private List<Councilor> councilorPool;
    private Councilor selectedCouncilor;
    private BooleanProperty isCouncilorSelected;
    private ObservableList<PoliticsCard> playerPoliticsCards;

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

    public WealthPathInterface getWealthPath() {
        return wealthPath;
    }

    public PlayerInterface getMe() {
        return me;
    }

    public void setMe(PlayerInterface me) {
        this.me = me;
    }

    public void putTown(TownName townName, TownInterface town) {
        towns.put(townName, town);
    }

    public TownInterface getTown(TownName townName) {
        return towns.get(townName);
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

    public ObservableList<PoliticsCard> getPlayerPoliticsCards() {
        return playerPoliticsCards;
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
}
