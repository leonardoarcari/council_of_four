package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.SelectAgainPermitAction;
import core.gamemodel.PermitCard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Matteo on 16/06/16.
 */
public class PermitAgainState implements CLIState {
    private Map<Integer, PermitCard> permitCardsMap;
    private int currentIndex;
    private boolean validate;

    public PermitAgainState() {
        permitCardsMap = new HashMap<>();
        validate = false;
        currentIndex = 1;
    }

    @Override
    public void showMenu() {
        if(!validate) {
            permitCardsMap.clear();
            currentIndex = 1;
            fillPermitsMap();
        }

        if(currentIndex == 1) {
            System.out.println("You have no permits!");
            //TODO change state
        } else printPermits();
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        if(permitCardsMap.keySet().contains(choice)) {
            Action action = new SelectAgainPermitAction((Player)CachedData.getInstance().getMe(), permitCardsMap.get(choice));
            CachedData.getInstance().getController().sendInfo(action);
            validate = false;
            //TODO change state
        } else throw new IllegalArgumentException();
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
