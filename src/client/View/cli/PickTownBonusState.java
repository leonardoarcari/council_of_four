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
 * This class represents a bonus action gained from the nobility path
 * that allows the player to pick a bonus from one of the towns of
 * the game.
 */
public class PickTownBonusState implements CLIState {
    // Reference to the context
    private CLI cli;

    // Attributes of the class
    private Map<TownName, Bonus> townBonusMap;
    private Map<Integer, TownName> namesMap;

    /**
     * The constructor builds the maps containing the town and their bonus references.
     * Being in the constructor, this work is done only one time because the towns
     * doesn't change during the whole game.
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
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

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        System.out.println("Choose town bonus to pick");
        int index = 1;
        for(TownName name : townBonusMap.keySet()) {
            System.out.println(index +") " + name + ": " + townBonusMap.get(name).toString());
            index++;
        }
    }

    /**
     * @param input is the choice of the player
     * @see CLIState
     * @throws IllegalArgumentException
     */
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
            cli.setCurrentState(cli.getMainState());
        } else throw new IllegalArgumentException();
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        //Do nothing
    }
}
