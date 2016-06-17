package client.View.cli;

import client.CachedData;
import client.View.ViewAlgorithms;
import core.Player;
import core.gamelogic.actions.BuyPermitCardAction;
import core.gamemodel.*;
import core.gamemodel.modelinterface.BalconyInterface;
import core.gamemodel.modelinterface.RegionInterface;

import java.util.*;

/**
 * Created by Leonardo Arcari on 15/06/2016.
 */
public class BuyPermitCardState implements CLIState {
    private static final int BASE_STATE = 0;
    private static final int CHOOSE_BALCONY = 1;
    private static final int CHOOSE_POLITICS = 2;
    private static final int CHOOSE_PERMIT = 3;

    private int internalState;
    private BalconyInterface chosenBalcony;
    private RegionInterface chosenRegion;

    private CLI cli;

    private Map<BalconyInterface, List<PoliticsCard>> validPoltics;
    private Map<Integer, BalconyInterface> balconyMap;
    private List<PoliticsCard> chosenPolitics;

    public BuyPermitCardState(CLI cli) {
        validPoltics = new HashMap<>();
        balconyMap = new HashMap<>(4);
        internalState = BASE_STATE;
        chosenBalcony = null;
        chosenRegion = null;
        chosenPolitics = new ArrayList<>();
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        if (internalState == BASE_STATE) printBalconies();
        else if (internalState == CHOOSE_BALCONY) printPolitics();
        else if (internalState == CHOOSE_POLITICS) printPermit();
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        if (internalState == CHOOSE_BALCONY) storeChosenBalcony(input);
        else if (internalState == CHOOSE_POLITICS) satisfyAction(input);
        else if (internalState == CHOOSE_PERMIT) {
            sendBuyPermitAction(input);
            resetState();
        }
    }

    @Override
    public void invalidateState() {
        resetState();
    }

    private void printBalconies() {
        balconyMap.clear();
        RegionType[] regions = {RegionType.SEA, RegionType.HILLS, RegionType.MOUNTAINS};
        int counter = 1;
        validPoltics.clear();
        for (RegionType region : regions) {
            BalconyInterface balcony = CachedData.getInstance().getBalcony(region);
            if (checkBalconyForSatisfaction(balcony)) {
                balconyMap.put(counter, balcony);
                System.out.println(counter + ") " + balcony);
                counter++;
            }
        }
        System.out.println("0) Back");
        internalState = CHOOSE_BALCONY;
    }

    private boolean checkBalconyForSatisfaction(BalconyInterface balcony) {
        List<Councilor> councilors = new ArrayList<>();
        List<PoliticsCard> validCards = new ArrayList<>();
        balcony.councilorsIterator().forEachRemaining(councilors::add);
        boolean result = ViewAlgorithms.checkForSatisfaction(councilors, validCards);
        if (result) {
            validPoltics.put(balcony, validCards);
        }
        return result;
    }

    private void storeChosenBalcony(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice < 0 || choice > balconyMap.size()) throw new IllegalArgumentException();
        if (choice == 0) {
            cli.setCurrentState(cli.getMainActionState());
            resetState();
        } else {
            chosenBalcony = balconyMap.get(choice);
        }
    }

    private void printPolitics() {
        List<PoliticsCard> validCards = validPoltics.getOrDefault(chosenBalcony, null);
        if (validCards != null) {
            System.out.println("Choose up to 4 of the following Politics Cards by typing their indexes separated by commas. Do not repeat indexes.");
            int index = 1;
            for (PoliticsCard card : validCards) {
                System.out.println(index++ + ") " + card);
            }
        }
        internalState = CHOOSE_POLITICS;
    }

    private void satisfyAction(String input) throws IllegalArgumentException{
        // Catch back action
        if (input.trim().equals("0")) {
            internalState = BASE_STATE;
        } else { // Validate input
            String toParse = input.trim().replaceAll(" ", "");
            StringTokenizer tokenizer = new StringTokenizer(toParse, ",");
            List<PoliticsCard> validCards = validPoltics.get(chosenBalcony);
            List<Integer> indexes = new ArrayList<>();
            chosenPolitics.clear();
            while (tokenizer.hasMoreTokens()) {
                int index;
                try {
                    index = Integer.valueOf(tokenizer.nextToken());
                    if (indexes.contains(index)) throw new IllegalArgumentException();
                    chosenPolitics.add(validCards.get(index-1));
                    indexes.add(index);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException();
                }
            }
            // Check if valid selection
            int money = CachedData.getInstance().getWealthPath().getPlayerPosition(
                    (Player) CachedData.getInstance().getMe()
            );
            if (ViewAlgorithms.coinForSatisfaction(chosenPolitics) > money) {
                chosenPolitics.clear();
                internalState = CHOOSE_BALCONY;
                System.out.println("You don't have enough coins to do this action with the selected Politics Cards");
            }
        }
    }

    private void printPermit() {
        if (chosenBalcony.getRegion().equals(RegionType.SEA)) {
            chosenRegion = CachedData.getInstance().getSeaRegion();
        } else if (chosenBalcony.getRegion().equals(RegionType.HILLS)) {
            chosenRegion = CachedData.getInstance().getHillsRegion();
        } else {
            chosenRegion = CachedData.getInstance().getMountainsRegion();
        }

        System.out.println("1) " + chosenRegion.getLeftPermitCard().toString());
        System.out.println("2) " + chosenRegion.getRightPermitCard().toString());
        System.out.println("0) Back");
        internalState = CHOOSE_PERMIT;
    }

    private void sendBuyPermitAction(String input) {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice < 0 || choice > 2) throw new IllegalArgumentException();
        if (choice == 0) {
            internalState = CHOOSE_BALCONY;
        } else {
            Region.PermitPos chosenPermit;
            if (choice == 1) chosenPermit = Region.PermitPos.LEFT;
            else chosenPermit = Region.PermitPos.RIGHT;
            List<PoliticsCard> myPolitics = new ArrayList<>(chosenPolitics);
            CachedData.getInstance().getController().sendInfo(
                    new BuyPermitCardAction(
                            (Player) CachedData.getInstance().getMe(),
                            myPolitics,
                            chosenRegion.getRegionType(),
                            chosenPermit
                    )
            );
        }
    }

    private void resetState() {
        internalState = BASE_STATE;
        chosenBalcony = null;
        chosenRegion = null;
        validPoltics.clear();
        balconyMap.clear();
        chosenPolitics.clear();
    }
}
