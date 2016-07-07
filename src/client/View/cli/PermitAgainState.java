package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.TakePermitBonusAction;
import core.gamemodel.PermitCard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents the bonus action gained from the nobility path
 * that allows the player to pick the bonus of one of his permit cards again.
 * If he has no cards, a simple information message is displayed and the player
 * is invited to press a key to continue.
 */
public class PermitAgainState implements CLIState {
    //Reference to the context
    private CLI cli;

    // Attributes of the class
    private Map<Integer, PermitCard> permitCardsMap;
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
    public PermitAgainState(CLI cli) {
        permitCardsMap = new HashMap<>();
        validate = false;
        currentIndex = 1;
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        if(!validate) {
            permitCardsMap.clear();
            currentIndex = 1;
            fillPermitsMap();
        }

        if(currentIndex == 1) {
            System.out.println("You have no permits! -- Press return to continue");
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

        if (permitCardsMap.keySet().contains(choice)) {
            Action action = new TakePermitBonusAction((Player)CachedData.getInstance().getMe(), permitCardsMap.get(choice));
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

    private void fillPermitsMap() {
        Iterator<PermitCard> permitCardIterator = CachedData.getInstance().getMe().permitCardIterator();
        while(permitCardIterator.hasNext()) {
            PermitCard permitCard = permitCardIterator.next();
            permitCardsMap.put(currentIndex++, permitCard);
        }
        validate = true;
    }

    private void printPermits() {
        System.out.println("Select permit card to obtain: ");
        for(int index : permitCardsMap.keySet()) {
            System.out.println(index + ") " + permitCardsMap.get(index).toString());
        }
    }
}
