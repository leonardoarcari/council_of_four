package client.clientconnection;

import client.CachedData;
import client.View.GUI;
import core.connection.GameBoardInterface;
import core.connection.InfoProcessor;
import core.gamelogic.actions.SyncAction;
import core.gamemodel.Councilor;
import core.gamemodel.WealthPath;
import core.gamemodel.modelinterface.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Leonardo Arcari on 03/06/2016.
 */
public class GUInfoProcessor implements InfoProcessor {
    private GUI gui;

    public GUInfoProcessor(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void processInfo(Object info) {
        if (info instanceof TownInterface) {
            TownInterface town = (TownInterface) info;
            CachedData.getInstance().putTown(town.getTownName(), town);
            gui.getTownView(town.getTownName()).update(town);
            gui.populateTownBonus(town);
        } else if (info instanceof BalconyInterface) {
            BalconyInterface balcony = (BalconyInterface) info;
            CachedData.getInstance().putBalcony(balcony.getRegion(), balcony);
            gui.updateBalcony((BalconyInterface) info);
        } else if (info instanceof WealthPathInterface) {
            gui.updateWealthPath((WealthPath) info);
        } else if (info instanceof RegionInterface) {
            gui.updatePermitCard((RegionInterface) info);
            gui.updateRegionBonus((RegionInterface) info);
        } else if (info instanceof NobilityPathInterface) {
            gui.updateNobilityPath((NobilityPathInterface) info);
        } else if (info instanceof GameBoardInterface) {
            GameBoardInterface gameboard = (GameBoardInterface) info;
            List<Councilor> councilorPool = new ArrayList<>();
            gameboard.councilorIterator().forEachRemaining(councilorPool::add);
            CachedData.getInstance().setCouncilorPool(councilorPool);
            gui.updateGameBoardData((GameBoardInterface) info);
        } else if (info instanceof PlayerInterface) {
            CachedData.getInstance().setMe((PlayerInterface) info);
            gui.updatePlayer((PlayerInterface) info);
        } else if (info instanceof SyncAction) {
            SyncAction action = (SyncAction) info;
            if (info.equals(SyncAction.GAME_START)) {
                gui.startGame();
            }
        }
    }
}
