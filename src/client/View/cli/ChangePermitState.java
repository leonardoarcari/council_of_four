package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.ChangePermitsAction;
import core.gamemodel.Region;
import core.gamemodel.RegionType;
import core.gamemodel.modelinterface.RegionInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matteo on 15/06/16.
 */
public class ChangePermitState implements CLIState {
    public static final int OPTIONS_AVAILABLE = 0;
    public static final int NO_OPTIONS = 1;
    private int currentState;

    private CLI cli;

    private Map<Integer, RegionType> regionOptions;

    public ChangePermitState(CLI cli) {
        currentState = NO_OPTIONS;
        regionOptions = new HashMap<>();
        fillRegionOptions();
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        if(CachedData.getInstance().getMe().getServantsNumber() >= 1)
            currentState = OPTIONS_AVAILABLE;
        else
            currentState = NO_OPTIONS;

        if(currentState == OPTIONS_AVAILABLE) printOptions();
        else printNoOptions();
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if(currentState == OPTIONS_AVAILABLE) chooseAvailable(choice);
        else chooseNoAvailable(choice);
    }

    @Override
    public void invalidateState() {
        currentState = NO_OPTIONS;
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
            currentState = NO_OPTIONS;
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