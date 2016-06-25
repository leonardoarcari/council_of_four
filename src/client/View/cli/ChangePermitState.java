package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.ChangePermitsAction;
import core.gamemodel.RegionType;
import core.gamemodel.modelinterface.RegionInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * The class is the state where the player falls when he wants to change the permit
 * cards of a region. There could have been only one internal state, the selection
 * of the region, but it may happens that there are no available cards to change.
 * To avoid that, a new internal state has been introduced, the NO_OPTIONS state,
 * that informs the player of the unavailability of the cards.
 */
public class ChangePermitState implements CLIState {
    // Internal states of the action
    public static final int OPTIONS_AVAILABLE = 0;
    public static final int NO_OPTIONS = 1;

    private int internalState;
    private boolean alreadyFilled;

    // Reference to the context
    private CLI cli;

    // Class attributes
    private Map<Integer, RegionType> regionOptions;

    /**
     * The constructor sets the beginning state
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public ChangePermitState(CLI cli) {
        internalState = NO_OPTIONS;
        regionOptions = new HashMap<>();
        alreadyFilled = false;
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        if(!alreadyFilled) fillRegionOptions();
        if(CachedData.getInstance().getMe().getServantsNumber() >= 1)
            internalState = OPTIONS_AVAILABLE;
        else
            internalState = NO_OPTIONS;

        if(internalState == OPTIONS_AVAILABLE) printOptions();
        else printNoOptions();
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
        if(internalState == OPTIONS_AVAILABLE) chooseAvailable(choice);
        else chooseNoAvailable(choice);
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        internalState = NO_OPTIONS;
    }

    private void fillRegionOptions() {
        int i = 1;
        i = checkRegion(CachedData.getInstance().getSeaRegion(), i);
        i = checkRegion(CachedData.getInstance().getHillsRegion(), i);
        checkRegion(CachedData.getInstance().getMountainsRegion(), i);
    }

    private int checkRegion(RegionInterface region, int index) {
        if (region.getLeftPermitCard() != null && region.getRightPermitCard() != null) {
            regionOptions.put(index, region.getRegionType());
            index++;
        }
        return index;
    }

    private void printOptions() {
        if(regionOptions.keySet().size() == 0) {
            System.out.println("Can't change any set of permits!");
        }
        else {
            System.out.println("Select region: ");
            for (int i = 0; i < regionOptions.keySet().size(); i++) {
                System.out.println((i + 1) + ") " + regionOptions.get(i).name() + " region");
            }
        }
        System.out.println("0) Go back");
    }

    private void printNoOptions() {
        System.out.println("Not enough servants (1 required)");
        System.out.println("0) Go back");
    }

    private void chooseAvailable(int choice) throws IllegalArgumentException {
        if(!regionOptions.keySet().contains(choice) && choice != 0) throw new IllegalArgumentException();

        if(choice != 0) {
            Action action = new ChangePermitsAction((Player)CachedData.getInstance().getMe(), regionOptions.get(choice));
            CachedData.getInstance().getController().sendInfo(action);
            internalState = NO_OPTIONS;
        }
        cli.setCurrentState(cli.getMainState());
    }

    private void chooseNoAvailable(int choice) throws IllegalArgumentException {
        if(choice != 0) throw new IllegalArgumentException();
        else {
            cli.setCurrentState(cli.getMainState());
        }
    }
}