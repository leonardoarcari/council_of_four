package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.PermitNoPayAction;
import core.gamemodel.PermitCard;
import core.gamemodel.Region;
import core.gamemodel.modelinterface.RegionInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the bonus action gained from the nobility path
 * that allows the player to pick a permit card for free. If no card is
 * available, a simple information message is displayed and the player
 * is invited to press a key to continue.
 */
public class PickPermitState implements CLIState {
    // Reference to the context
    private CLI cli;

    // Attributes of the class
    private Map<PermitCard, RegionInterface> permitsMap;
    private Map<Integer, PermitCard> indexMap;
    private int currentIndex;
    private boolean validate;

    /**
     * The constructor validates the class, meaning that the permit cards are already
     * set, and if there is more than one invocation on the show method, the map
     * containing the cards won't be built again.
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public PickPermitState(CLI cli) {
        this.cli = cli;
        permitsMap = new HashMap<>();
        indexMap = new HashMap<>();
        validate = false;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        if(!validate) {
            permitsMap.clear();
            indexMap.clear();
            currentIndex = 1;
            fillMaps();
        }

        if(currentIndex == 1) {
            System.out.println("No permits available to buy -- press return to continue");
            cli.setCurrentState(cli.getMainState());
            cli.setValidation(false);
        } else printPermits();
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

        if(indexMap.keySet().contains(choice)) {
            PermitCard permitCard = indexMap.get(choice);
            RegionInterface region = permitsMap.get(permitCard);
            Action action = new PermitNoPayAction((Player)CachedData.getInstance().getMe(), region.getRegionType(),
                    (region.getLeftPermitCard().equals(permitCard))? Region.PermitPos.LEFT: Region.PermitPos.RIGHT);
            CachedData.getInstance().getController().sendInfo(action);
            validate = false;
            cli.setCurrentState(cli.getMainState());
        } else throw new IllegalArgumentException();
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        validate = false;
    }

    private void fillMaps() {
       fillMapsBy(CachedData.getInstance().getSeaRegion());
       fillMapsBy(CachedData.getInstance().getHillsRegion());
        fillMapsBy(CachedData.getInstance().getMountainsRegion());
        validate = true;
    }

    private void fillMapsBy(RegionInterface currentRegion) {
        if(currentRegion.getLeftPermitCard() != null) {
            permitsMap.put(currentRegion.getLeftPermitCard(),currentRegion);
            indexMap.put(currentIndex++, currentRegion.getLeftPermitCard());
        }
        if(currentRegion.getRightPermitCard() != null) {
            permitsMap.put(currentRegion.getRightPermitCard(),currentRegion);
            indexMap.put(currentIndex++, currentRegion.getRightPermitCard());
        }
    }

    private void printPermits() {
        System.out.println("Select permit card to obtain: ");
        for(int index : indexMap.keySet()) {
            System.out.println(index + ") " + indexMap.get(index).toString());
        }
    }
}
