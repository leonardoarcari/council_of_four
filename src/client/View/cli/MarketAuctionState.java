package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.BuyObjectsAction;
import core.gamelogic.actions.ExposeSellablesAction;
import core.gamemodel.OnSaleItem;
import core.gamemodel.PoliticsCard;
import core.gamemodel.Servant;

import java.util.*;

/**
 * Created by Matteo on 15/06/16.
 */
public class MarketAuctionState implements CLIState {
    private static final int SELECTION_STATE = 0;
    private static final int SHOWING_STATE = 1;
    private static final int BUYING_STATE = 2;
    private int currentState;

    private static final int SELECT_OBJECT = 4;
    private static final int NOT_ENOUGH_MONEY = 5;
    private static final int DESELECT_OBJECT = 6;
    private int microState;

    private CLI cli;

    private Map<Integer, OnSaleItem> onSaleItemMap;
    private Map<Integer, OnSaleItem> buyingItemMap;
    private boolean validate;
    private int selectedItemIndex;
    private int myCoins;

    public MarketAuctionState(CLI cli) {
        currentState = SELECTION_STATE;
        microState = NOT_ENOUGH_MONEY;
        onSaleItemMap = new HashMap<>();
        buyingItemMap = new HashMap<>();
        validate = false;
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        if(!validate) {
            myCoins = CachedData.getInstance().getWealthPath().getPlayerPosition((Player)CachedData.getInstance().getMe());
            onSaleItemMap.clear();
            buyingItemMap.clear();
            fillOnSaleItemMap();
        }

        if(currentState == SELECTION_STATE) printSelectionMenu();
        else if(currentState == SHOWING_STATE) printItemsMenu();
        else chooseItemMenu();
    }

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
        else checkItemSelection(choice);
    }

    @Override
    public void invalidateState() {
        validate = false;
        currentState = SELECTION_STATE;
        Action action = new BuyObjectsAction((Player)CachedData.getInstance().getMe(), new ArrayList<>());
        CachedData.getInstance().getController().sendInfo(action);
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
                        "politics card at " + onSaleItem.getPrice() + " coin(s)");
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
            currentState = SHOWING_STATE;
            selectedItemIndex = 0;
        } else {
            List<OnSaleItem> itemsToBuy = new ArrayList<>();
            buyingItemMap.values().forEach(itemsToBuy::add);
            Action action = new BuyObjectsAction((Player)CachedData.getInstance().getMe(), itemsToBuy);
            CachedData.getInstance().getController().sendInfo(action);
            validate = false; //Phase ended, next time buying items map has to be re-calculated
            cli.setCurrentState(cli.getMainState());
        }
    }

    private void selectItem(int choice) throws IllegalArgumentException {
        if(choice == 0) {
            currentState = SELECTION_STATE;
            selectedItemIndex = 0;
        }

        if(onSaleItemMap.keySet().contains(choice)) {
            currentState = BUYING_STATE;
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
        currentState = SHOWING_STATE;
    }
}
