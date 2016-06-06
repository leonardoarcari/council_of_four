package client.clientconnection;

import client.View.GUI;
import core.connection.InfoProcessor;
import core.gamemodel.WealthPath;
import core.gamemodel.modelinterface.BalconyInterface;
import core.gamemodel.modelinterface.TownInterface;
import core.gamemodel.modelinterface.WealthPathInterface;


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
        } else if (info instanceof BalconyInterface) {
            gui.updateBalcony((BalconyInterface) info);
        } else if (info instanceof WealthPathInterface) {
            gui.updateWealthPath((WealthPath) info);
        }
    }
}
