package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.BuildEmpoPCAction;
import core.gamemodel.PermitCard;
import core.gamemodel.RegionType;
import core.gamemodel.TownName;
import core.gamemodel.modelinterface.PlayerInterface;
import core.gamemodel.modelinterface.TownInterface;

import java.util.*;

/**
 * The class is the state where the player falls when he wants to build an emporium
 * with a permit card. It consists by all the sequences bound to the specific
 * action, such as the selection of the town or the choice of the permit card
 * wanted. These "flows" are represented by the internal states of the class: the three
 * static final attributes BEGIN_STATE, CHOOSE_TOWN and CHOOSE_PERMIT.
 */
public class BuildEmporiumState implements CLIState{
    // Internal states of the action
    private static final int BEGIN_STATE = 0;
    private static final int CHOOSE_TOWN = 1;
    private static final int CHOOSE_PERMIT = 2;

    private int internalState;
    private boolean validState;

    // Reference to the context
    private CLI cli;

    // Class attributes
    private PlayerInterface me;
    private Map<TownName, List<PermitCard>> permitsPerTown;
    private Map<Integer, TownInterface> townOptions;
    private TownInterface townChoice;

    /**
     * The constructor sets the beginning state and validates it
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public BuildEmporiumState(CLI cli) {
        internalState = BEGIN_STATE;
        validState = false;
        permitsPerTown = new HashMap<>(15);
        townOptions = new HashMap<>();
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        if (internalState == BEGIN_STATE) printTowns();
        else if (internalState == CHOOSE_PERMIT) printPermits();
    }

    /**
     * @param input is the choice of the player
     * @see CLIState
     * @throws IllegalArgumentException
     */
    @Override
    public void readInput(String input) throws IllegalArgumentException {
        if (internalState == CHOOSE_TOWN) storeTownChoice(input);
        else if (internalState == CHOOSE_PERMIT) {
            sendBuildAction(input);
            resetState();
        }
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        resetState();
    }

    private void printTowns() {
        me = CachedData.getInstance().getMe();
        if (!validState) buildMaps();
        townOptions.keySet().iterator().forEachRemaining(integer ->
            System.out.println(integer + ") " + townOptions.get(integer).getTownName().toString())
        );
        internalState = CHOOSE_TOWN;
    }

    private void buildMaps() {
        permitsPerTown.clear();
        townOptions.clear();
        me.permitCardIterator().forEachRemaining(card ->
            card.getCityPermits().iterator().forEachRemaining(townName -> {
                if (!haveAlreadyBuilt(me, townName) && hasEnoughServants(me, townName)) {
                    permitsPerTown.putIfAbsent(townName, new ArrayList<>());
                    permitsPerTown.get(townName).add(card);
                }
            })
        );

        int option = 1;
        for (TownName name : permitsPerTown.keySet()) {
            if (permitsPerTown.get(name) != null && permitsPerTown.get(name).size() > 0) { // Meaningful list
                townOptions.put(option++, CachedData.getInstance().getTown(name));
            }
        }
        validState = true;
    }

    private boolean haveAlreadyBuilt(PlayerInterface player, TownName townName) {
        TownInterface town = CachedData.getInstance().getTown(townName);
        return town.hasEmporium((Player) player);
    }

    private boolean hasEnoughServants(PlayerInterface player, TownName townName) {
        TownInterface town = CachedData.getInstance().getTown(townName);
        return player.getServantsNumber() >= town.getEmporiumsNumber();
    }

    private void storeTownChoice(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice < 0 || choice > townOptions.size()) throw new IllegalArgumentException();
        if (choice == 0) {
            cli.setCurrentState(cli.getMainActionState());
            resetState();
        }
        else townChoice = townOptions.get(choice);
    }

    private void printPermits() {
        List<PermitCard> permitCards = permitsPerTown.getOrDefault(townChoice, null);
        if (permitCards != null) {
            for (int i = 0; i < permitCards.size(); i++) {
                System.out.println((i+1) + ") " + permitCards.get(i).toString());
            }
        }
        internalState = CHOOSE_PERMIT;
    }

    private void sendBuildAction(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice < 0 || choice > townOptions.size()) throw new IllegalArgumentException();
        if (choice == 0) {
            internalState = BEGIN_STATE;
        } else {
            List<PermitCard> permitCards = permitsPerTown.getOrDefault(townChoice, null);
            if (permitCards != null) {
                PermitCard card = permitCards.get(choice-1);
                CachedData.getInstance().getController().sendInfo(
                        new BuildEmpoPCAction(
                                (Player) CachedData.getInstance().getMe(),
                                getRegionType(townChoice.getTownName()),
                                townChoice.getTownName(),
                                card
                        )
                );
            }
            System.out.println("Action sent");
        }
    }

    private RegionType getRegionType(TownName name) {
        if (name.ordinal() <= 4) return RegionType.SEA;
        if (name.ordinal() <= 9) return RegionType.HILLS;
        return RegionType.MOUNTAINS;
    }

    private void resetState() {
        internalState = BEGIN_STATE;
        validState = false;
        me = null;
        permitsPerTown.clear();
        townOptions.clear();
        townChoice = null;
    }
}
