package client.clientconnection;

import client.View.GUI;
import core.Player;
import core.connection.InfoProcessor;
import core.gamemodel.Town;
import core.gamemodel.TownName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
        if (info.getClass().equals(Town.class)) {
            Town town = (Town) info;
            gui.getTownView(town.getTownName()).update(town);
        }
    }
}
