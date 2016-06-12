package client;

import core.Player;
import core.gamemodel.*;
import core.gamemodel.modelinterface.*;
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
    private ObservableList<PermitCard> myPermitCards;
    private Map<TownName, TownInterface> towns;
    private Map<RegionType, BalconyInterface> balconies;
    private WealthPathInterface wealthPath;
    private List<Councilor> councilorPool;
    private Councilor selectedCouncilor;
    private BooleanProperty isCouncilorSelected;
    private ObservableList<PoliticsCard> playerPoliticsCards;
    private Region seaRegion;
    private Region hillsRegion;
    private Region mountainsRegion;
    private ShowcaseInterface showcase;

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
        myPermitCards.clear();
        List<PermitCard> permitCardsTemp = new ArrayList<>();
        Iterator<PermitCard> permitIterator = this.me.permitCardIterator();
        while(permitIterator.hasNext()) {
            permitCardsTemp.add(permitIterator.next());
        }
        myPermitCards.addAll(permitCardsTemp);
        myPermitCards.forEach(permitCard -> System.out.println("Is burned : "+ permitCard.isBurned()));
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

    public Region getSeaRegion() {
        return seaRegion;
    }

    public Region getHillsRegion() {
        return hillsRegion;
    }

    public Region getMountainsRegion() {
        return mountainsRegion;
    }

    public void setSeaRegion(Region seaRegion) {
        this.seaRegion = seaRegion;
    }

    public void setHillsRegion(Region hillsRegion) {
        this.hillsRegion = hillsRegion;
    }

    public void setMountainsRegion(Region mountainsRegion) {
        this.mountainsRegion = mountainsRegion;
    }

    public ShowcaseInterface getShowcase() {
        return showcase;
    }

    public void setShowcase(ShowcaseInterface showcase) {
        this.showcase = showcase;
    }
}
