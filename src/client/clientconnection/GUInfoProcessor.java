package client.clientconnection;

import client.View.GUI;
import core.Player;
import core.connection.GameBoardInterface;
import core.connection.InfoProcessor;
import core.gamelogic.actions.SyncAction;
import core.gamemodel.WealthPath;
import core.gamemodel.modelinterface.*;


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
            gui.getTownView(town.getTownName()).update(town);
            gui.populateTownBonus(town);
        } else if (info instanceof BalconyInterface) {
            gui.updateBalcony((BalconyInterface) info);
        } else if (info instanceof WealthPathInterface) {
            gui.updateWealthPath((WealthPath) info);
        } else if (info instanceof RegionInterface) {
            gui.updatePermitCard((RegionInterface) info);
            gui.updateRegionBonus((RegionInterface) info);
        } else if (info instanceof NobilityPathInterface) {
            gui.updateNobilityPath((NobilityPathInterface) info);
        } else if (info instanceof GameBoardInterface) {
            gui.updateGameBoardData((GameBoardInterface) info);
        } else if (info instanceof PlayerInterface) {
            gui.updatePlayer((PlayerInterface) info);
        } else if (info instanceof SyncAction) {
            SyncAction action = (SyncAction) info;
            if (info.equals(SyncAction.GAME_START)) {
                gui.startGame();
            }
        }
    }
}
