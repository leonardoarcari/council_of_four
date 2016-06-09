package client;


import core.gamemodel.Councilor;
import core.gamemodel.PoliticsCard;
import core.gamemodel.RegionType;
import core.gamemodel.TownName;
import core.gamemodel.modelinterface.BalconyInterface;
import core.gamemodel.modelinterface.PlayerInterface;
import core.gamemodel.modelinterface.TownInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

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
    private List<Councilor> councilorPool;
    private Councilor selectedCouncilor;
    private BooleanProperty isCouncilorSelected;
    private List<PoliticsCard> selectedPolitics;

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
        selectedCouncilor = null;
        isCouncilorSelected = new SimpleBooleanProperty(false);
    }

    public ControllerUI getController() {
        return controller;
    }

    public void setController(ControllerUI controller) {
        this.controller = controller;
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

    public void addSelectedPoliticsCard(PoliticsCard card) {
        selectedPolitics.add(card);
    }

    public List<PoliticsCard> pullSelectedPoliticsCard() {
        List<PoliticsCard> toReturn = new ArrayList<>(selectedPolitics);
        selectedPolitics.clear();
        return toReturn;
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
