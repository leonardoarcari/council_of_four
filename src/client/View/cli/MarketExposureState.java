package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.ExposeSellablesAction;
import core.gamemodel.OnSaleItem;
import core.gamemodel.PoliticsCard;
import core.gamemodel.Servant;
import core.gamemodel.modelinterface.SellableItem;

import java.util.*;

/**
 * This class is the exposure phase of the market. A player can see the items he can sell
 * and decide a price for each of them (from 1 to 20, 0 act as a deselection). The player
 * can also decide to expose nothing, so in the main menu of this state (SELECTION_STATE)
 * he would simply press the expose option, without selecting any item.
 */
public class MarketExposureState implements CLIState {
    // Internal states of the class
    private static final int SELECTION_STATE = 0;
    private static final int SHOWING_STATE = 1;
    private static final int SELECT_PRICE = 2;

    // Reference to the context
    private CLI cli;

    // Attributes of the class
    private Map<Integer, SellableItem> sellableItemMap;
    private Map<Integer, OnSaleItem> onSaleItemMap;
    private boolean validate;
    private int currentState;
    private int selectedItemIndex;

    /**
     * The constructor sets the internal state and validates it
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public MarketExposureState(CLI cli) {
        currentState = SELECTION_STATE;
        sellableItemMap = new HashMap<>();
        onSaleItemMap = new HashMap<>();
        validate = false;
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        if(!validate) {
            onSaleItemMap.clear();
            sellableItemMap.clear();
            fillSellableMap();
        }
        if(currentState == SELECTION_STATE) printSelectionMenu();
        else if(currentState == SHOWING_STATE) printItemsMenu();
        else printPriceMenu();
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

        if(currentState == SELECTION_STATE) selectAction(choice);
        else if(currentState == SHOWING_STATE) selectItem(choice);
        else selectPrice(choice);
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        validate = false;
        currentState = SELECTION_STATE;
    }

    private void fillSellableMap() {
        int index = 1;

        int servantNumber = CachedData.getInstance().getMe().getServantsNumber();
        for (int i = 0; i < servantNumber; i++) {
            sellableItemMap.put(i+index, new Servant());
        }
        index = index + servantNumber;

        int politicsNumber = CachedData.getInstance().getPlayerPoliticsCards().size();
        for(int i = 0; i < politicsNumber; i++) {
            sellableItemMap.put(i+index, CachedData.getInstance().getPlayerPoliticsCards().get(i));
        }
        index = index + politicsNumber;

        int permitsNumber = CachedData.getInstance().getMyPermitCards().size();
        for(int i = 0; i < permitsNumber; i++) {
            sellableItemMap.put(i+index, CachedData.getInstance().getMyPermitCards().get(i));
        }

        validate = true;
    }

    private void printSelectionMenu() {
        System.out.println("What to do?");
        System.out.println("1) View and select objects you can sell");
        System.out.println("2) Expose selected objects (you can expose no object)");
    }

    private void printItemsMenu() {
        System.out.println("Select item number to change its selling price");
        for(int index : sellableItemMap.keySet()) {
            if(sellableItemMap.get(index).getClass().equals(Servant.class)) {
                System.out.println(index+") Servant");
            } else if(sellableItemMap.get(index).getClass().equals(PoliticsCard.class)) {
                System.out.println(index + ") " + sellableItemMap.get(index).toString() + " politics card");
            } else System.out.println(index + ") Permit card\n" + sellableItemMap.get(index).toString());
        }
        System.out.println("0) Go back");
    }

    private void printPriceMenu() {
        System.out.println("Insert price for the selected item (1 : 20)");
        System.out.println("Insert 0 to deselect the item");
    }

    private void selectAction(int choice) throws IllegalArgumentException {
        if(choice != 1 && choice != 2) throw new IllegalArgumentException();

        if(choice == 1) {
            currentState = SHOWING_STATE;
            selectedItemIndex = 0;
        } else {
            List<OnSaleItem> onSaleItemList = new ArrayList<>();
            onSaleItemMap.values().forEach(onSaleItemList::add);
            cli.getController().stopTimer();
            ExposeSellablesAction action = new ExposeSellablesAction((Player)CachedData.getInstance().getMe(), new ArrayList<>(onSaleItemList));
            CachedData.getInstance().getController().sendInfo(action);
            invalidateState(); //Phase ended, next time sellable items map has to be re-calculated
            cli.setCurrentState(cli.getWaitingState());
        }
    }

    private void selectItem(int choice) throws IllegalArgumentException {
        if(choice == 0) {
            currentState = SELECTION_STATE;
            selectedItemIndex = 0;
        }else if(sellableItemMap.keySet().contains(choice)) {
            currentState = SELECT_PRICE;
            selectedItemIndex = choice;
        } else throw new IllegalArgumentException();
    }

    private void selectPrice(int choice) throws IllegalArgumentException {
        if (choice < 0 || choice > 20) throw new IllegalArgumentException();

        //Choice 0 removes an item from the onSale item list, but only if it's already in it!
        if (choice == 0) {
            if (onSaleItemMap.containsKey(selectedItemIndex))
                onSaleItemMap.remove(selectedItemIndex);
        } else {
            onSaleItemMap.put(selectedItemIndex, new OnSaleItem(
                    sellableItemMap.get(selectedItemIndex), choice,
                    (Player) CachedData.getInstance().getMe())
            );
        }
        currentState = SHOWING_STATE;
    }
}
