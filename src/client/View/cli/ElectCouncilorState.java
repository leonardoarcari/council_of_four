package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.CouncilorElectionAction;
import core.gamelogic.actions.FastCouncilorElectionAction;
import core.gamemodel.Councilor;
import core.gamemodel.RegionType;
import core.gamemodel.modelinterface.BalconyInterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Leonardo Arcari on 14/06/2016.
 */
public class ElectCouncilorState implements CLIState {
    private static final int BEGIN_STATE = 0;
    private static final int CHOOSE_BALCONY = 1;
    private static final int CHOOSE_COUNCILOR = 2;
    private int internalState;
    private Type electionType;
    private Map<Integer, BalconyInterface> balconyMap;
    private Map<Integer, Councilor> councilorPoolMap;
    private BalconyInterface balconyChoice;

    public ElectCouncilorState(Type electionType) {
        internalState = BEGIN_STATE;
        this.electionType = electionType;
        balconyMap = new HashMap<>();
        councilorPoolMap = new HashMap<>();
        balconyChoice = null;
    }

    @Override
    public void showMenu() {
        if (internalState == BEGIN_STATE) printBalconies();
        else if (internalState == CHOOSE_BALCONY) printCouncilors();
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        if (internalState == CHOOSE_BALCONY) storeBalconyChoice(input);
        else if (internalState == CHOOSE_COUNCILOR) {
            sendElectionAction(input);
            resetState();
        }
    }

    private void printBalconies() {
        balconyMap.clear();
        RegionType[] regions = RegionType.values();
        for (int i = 0; i < regions.length; i++) {
            balconyMap.put(i+1, CachedData.getInstance().getBalcony(regions[i]));
        }
        balconyMap.keySet().forEach(integer -> System.out.println(integer + ") " + balconyMap.get(integer)));
        internalState = CHOOSE_BALCONY;
    }

    private void storeBalconyChoice(String choice) throws IllegalArgumentException {
        int inputChoice;
        try {
            inputChoice = Integer.valueOf(choice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (inputChoice < 0 || inputChoice > balconyMap.size()) throw new IllegalArgumentException();
        if (inputChoice == 0) {
            //TODO: Go back to previous state
            resetState();
        } else {
            balconyChoice = balconyMap.get(inputChoice);
        }
    }

    private void printCouncilors() {
        councilorPoolMap.clear();
        int counter = 1;
        Iterator<Councilor> poolIterator = CachedData.getInstance().getCouncilorPool();
        while (poolIterator.hasNext()) {
            councilorPoolMap.put(counter++, poolIterator.next());
        }

        councilorPoolMap.keySet().forEach(integer -> System.out.println(integer + ") " + councilorPoolMap.get(integer)));
        internalState = CHOOSE_COUNCILOR;
    }

    private void sendElectionAction(String choice) {
        int inputChoice;
        try {
            inputChoice = Integer.valueOf(choice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (inputChoice < 0 || inputChoice > balconyMap.size()) throw new IllegalArgumentException();
        if (inputChoice == 0) {
            internalState = BEGIN_STATE;
        } else {
            Councilor councilor = councilorPoolMap.get(inputChoice);
            if (councilor == null || balconyChoice == null) throw new NullPointerException("Balcony or Councilor is null");
            CachedData.getInstance().getController().sendInfo(
                    (electionType.equals(Type.MAIN_ACTION)) ?
                            new CouncilorElectionAction(
                                    (Player) CachedData.getInstance().getMe(),
                                    councilor,
                                    balconyChoice.getRegion()
                            ) :
                            new FastCouncilorElectionAction(
                                    (Player) CachedData.getInstance().getMe(),
                                    balconyChoice.getRegion(),
                                    councilor
                            )
            );
            System.out.println("Action sent");
        }
    }

    private void resetState() {
        internalState = BEGIN_STATE;
        balconyChoice = null;
        balconyMap.clear();
        councilorPoolMap.clear();
    }

    public enum Type {
        MAIN_ACTION, FAST_ACTION
    }
}