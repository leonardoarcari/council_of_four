package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.PickTownBonusAction;
import core.gamemodel.TownName;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.TownInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matteo on 16/06/16.
 */
public class PickTownBonusState implements CLIState {
    private Map<TownName, Bonus> townBonusMap;
    private Map<Integer, TownName> namesMap;

    private CLI cli;

    public PickTownBonusState(CLI cli) {
        townBonusMap = new HashMap<>();
        namesMap = new HashMap<>();
        Map<TownName, TownInterface> temp = CachedData.getInstance().getTowns();
        int index = 1;
        for(TownName name : temp.keySet()) {
            townBonusMap.put(name, temp.get(name).getTownBonus());
            namesMap.put(index, name);
        }
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        System.out.println("Choose town bonus to pick");
        int index = 1;
        for(TownName name : townBonusMap.keySet()) {
            System.out.println(index +") " + name + ": " + townBonusMap.get(name).toString());
            index++;
        }
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        if(namesMap.keySet().contains(choice)) {
            Action action = new PickTownBonusAction((Player) CachedData.getInstance().getMe(), namesMap.get(choice));
            CachedData.getInstance().getController().sendInfo(action);
            //TODO: change context
        } else throw new IllegalArgumentException();
    }
}
