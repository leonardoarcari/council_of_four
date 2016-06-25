package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.BuyObjectsAction;
import core.gamemodel.OnSaleItem;
import core.gamemodel.PoliticsCard;
import core.gamemodel.Servant;

import java.util.*;

/**
 * This class is the auction phase of the game. The player can choose to view the items
 * in the showcase, with their respective price, or simply pass the phase not buying
 * anything. As a real "market", every time the player selects an object, the price he will
 * pay when buying increases. Thus, the need of three micro states referred to the item
 * selection. The player is informed when he has not enough money to buy a specific object
 * (NOT_ENOUGH_MONEY micro state) and is able to select/deselect every item (SELECT_OBJECT
 * and DESELECT_OBJECT micro states).
 */
public class MarketAuctionState implements CLIState {
    // Internal state of the class
    private static final int SELECTION_STATE = 0;
    private static final int SHOWING_STATE = 1;
    private static final int BUYING_STATE = 2;

    private int internalState;

    // Status of an item when buying it
    private static final int SELECT_OBJECT = 4;
    private static final int NOT_ENOUGH_MONEY = 5;
    private static final int DESELECT_OBJECT = 6;

    private int microState;

    // Reference to the context
    private CLI cli;

    // Attributes of the class
    private Map<Integer, OnSaleItem> onSaleItemMap;
    private Map<Integer, OnSaleItem> buyingItemMap;
    private boolean validate;
    private int selectedItemIndex;
    private int myCoins;

    /**
     * The constructor sets the internal state and the micro state (default NOT_ENOUGH_MONEY)
     * and validates them
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public MarketAuctionState(CLI cli) {
        internalState = SELECTION_STATE;
        microState = NOT_ENOUGH_MONEY;
        onSaleItemMap = new HashMap<>();
        buyingItemMap = new HashMap<>();
        validate = false;
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        if(!validate) {
            myCoins = CachedData.getInstance().getWealthPath().getPlayerPosition((Player)CachedData.getInstance().getMe());
            onSaleItemMap.clear();
            buyingItemMap.clear();
            fillOnSaleItemMap();
        }

        if(internalState == SELECTION_STATE) printSelectionMenu();
        else if(internalState == SHOWING_STATE) printItemsMenu();
        else chooseItemMenu();
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

        if(internalState == SELECTION_STATE) selectAction(choice);
        else if(internalState == SHOWING_STATE) selectItem(choice);
        else checkItemSelection(choice);
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        validate = false;
        internalState = SELECTION_STATE;
        //Action action = new BuyObjectsAction((Player)CachedData.getInstance().getMe(), new ArrayList<>());
        //CachedData.getInstance().getController().sendInfo(action);
    }

    private void fillOnSaleItemMap() {
        Iterator<OnSaleItem> onSaleItemIterator = CachedData.getInstance().getShowcase().onSaleItemIterator();
        int index = 1;
        while(onSaleItemIterator.hasNext()) {
            OnSaleItem onSaleItem = onSaleItemIterator.next();
            //Don't show items who exceed the player's coins
            if(onSaleItem.getPrice() <= myCoins) {
                onSaleItemMap.put(index, onSaleItem);
                index++;
            }
        }
        validate = true;
    }

    private void printSelectionMenu() {
        System.out.println("What to do?");
        System.out.println("1) View and select objects you can buy");
        System.out.println("2) Buy selected objects (you can buy no object)");
    }

    private void printItemsMenu() {
        System.out.println("Select item number to buy or deselect");
        for(int index : onSaleItemMap.keySet()) {
            OnSaleItem onSaleItem = onSaleItemMap.get(index);
            if(onSaleItem.getItem().getClass().equals(Servant.class)) {
                System.out.println(index+") Servant at " + onSaleItem.getPrice() + " coin(s)");
            } else if(onSaleItem.getItem().getClass().equals(PoliticsCard.class)) {
                System.out.println(index + ") " + onSaleItem.getItem().toString() +
                        " politics card at " + onSaleItem.getPrice() + " coin(s)");
            } else System.out.println(index + ") Permit card\n" + onSaleItem.getItem().toString()
                    + "\n\tat " + onSaleItem.getPrice() + " coin(s)");
        }
        System.out.println("0) Go back");
    }

    private void chooseItemMenu() {
        if(buyingItemMap.containsKey(selectedItemIndex)) {
            microState = DESELECT_OBJECT;
            System.out.println("1) Deselect item");
        } else {
            int available = myCoins - onSaleItemMap.get(selectedItemIndex).getPrice();
            if (available < 0) {
                microState = NOT_ENOUGH_MONEY;
                System.out.println("Not enough money!");
            } else {
                microState = SELECT_OBJECT;
                System.out.println("1) Select item");
            }
        }
        System.out.println("0) Go back");
    }

    private void selectAction(int choice) throws IllegalArgumentException{
        if(choice != 1 && choice != 2) throw new IllegalArgumentException();

        if(choice == 1) {
            internalState = SHOWING_STATE;
            selectedItemIndex = 0;
        } else {
            List<OnSaleItem> itemsToBuy = new ArrayList<>();
            buyingItemMap.values().forEach(itemsToBuy::add);
            BuyObjectsAction action = new BuyObjectsAction((Player)CachedData.getInstance().getMe(), new ArrayList<>(itemsToBuy));
            cli.getController().stopTimer();
            CachedData.getInstance().getController().sendInfo(action);
            validate = false; //Phase ended, next time buying items map has to be re-calculated
            cli.setCurrentState(cli.getMainState());
        }
    }

    private void selectItem(int choice) throws IllegalArgumentException {
        if(choice == 0) {
            internalState = SELECTION_STATE;
            selectedItemIndex = 0;
        }else if(onSaleItemMap.keySet().contains(choice)) {
            internalState = BUYING_STATE;
            selectedItemIndex = choice;
        } else throw new IllegalArgumentException();
    }

    private void checkItemSelection(int choice) throws IllegalArgumentException {
        if(microState == NOT_ENOUGH_MONEY && choice != 0) throw new IllegalArgumentException();
        if(choice != 0 && choice != 1) throw new IllegalArgumentException();

        if(choice == 1) {
            if(microState == DESELECT_OBJECT) {
                myCoins = myCoins + onSaleItemMap.get(selectedItemIndex).getPrice();
                buyingItemMap.remove(selectedItemIndex);
            } else {
                myCoins = myCoins - onSaleItemMap.get(selectedItemIndex).getPrice();
                buyingItemMap.put(selectedItemIndex, onSaleItemMap.get(selectedItemIndex));
            }
        }
        internalState = SHOWING_STATE;
    }
}
