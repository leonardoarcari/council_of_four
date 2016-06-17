package client.View.cli;

import client.CachedData;
import client.View.ViewAlgorithms;
import core.Player;
import core.gamelogic.GraphsAlgorithms;
import core.gamelogic.actions.BuildEmpoKingAction;
import core.gamemodel.*;
import core.gamemodel.modelinterface.BalconyInterface;
import core.gamemodel.modelinterface.TownInterface;

import java.util.*;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public class BuildEmpoWithKingState implements CLIState {
    private static final int BASE_STATE = 0;
    private static final int CHOOSE_POLITICS = 1;
    private static final int CHOOSE_TOWN = 2;

    private int internalState;
    private BalconyInterface kingsBalcony;
    private List<PoliticsCard> validPolitics;
    private List<PoliticsCard> chosenPolitics;

    private CLI cli;

    private int startingMoney;
    private int moneyForPolitics;
    private int moneyForBuilding;
    private TownInterface withKing;
    private Map<Integer, TownInterface> townsSelection;
    private Map<TownName, Integer> reachableTowns;

    public BuildEmpoWithKingState(CLI cli) {
        internalState = BASE_STATE;
        validPolitics = new ArrayList<>();
        chosenPolitics = new ArrayList<>();
        townsSelection = new HashMap<>();
        reachableTowns = new HashMap<>();
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        if (internalState == BASE_STATE) printPolitics();
        else if (internalState == CHOOSE_POLITICS) printTowns();
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        if (internalState == CHOOSE_POLITICS) satisfyAction(input);
        else if (internalState == CHOOSE_TOWN) {
            sendBuildAction(input);
            resetState();
        }
    }

    @Override
    public void invalidateState() {
        resetState();
    }

    private void printPolitics() {
        kingsBalcony = CachedData.getInstance().getBalcony(RegionType.KINGBOARD);
        buildPoliticsList();
        System.out.println("To satisfy: " + kingsBalcony);
        System.out.println("Choose up to 4 of the following Politics Cards by typing their indexes separated by commas. Do not repeat indexes.");
        for (int i = 0; i < validPolitics.size(); i++) {
            System.out.println((i+1) + ") " + validPolitics.get(i));
        }
        System.out.println("0) Back");
        internalState = CHOOSE_POLITICS;
    }

    private void buildPoliticsList() {
        List<Councilor> councilors = new ArrayList<>();
        List<PoliticsCard> validCards = new ArrayList<>();
        kingsBalcony.councilorsIterator().forEachRemaining(councilors::add);
        ViewAlgorithms.checkForSatisfaction(councilors, validCards);
        validPolitics = validCards;
    }

    private void satisfyAction(String input) throws IllegalArgumentException{
        // Catch back action
        if (input.trim().equals("0")) {
            cli.setCurrentState(cli.getMainActionState());
        } else { // Validate input
            String toParse = input.trim().replaceAll(" ", "");
            StringTokenizer tokenizer = new StringTokenizer(toParse, ",");
            List<Integer> indexes = new ArrayList<>();
            chosenPolitics.clear();
            while (tokenizer.hasMoreTokens()) {
                int index;
                try {
                    index = Integer.valueOf(tokenizer.nextToken());
                    if (indexes.contains(index)) throw new IllegalArgumentException();
                    chosenPolitics.add(validPolitics.get(index-1));
                    indexes.add(index);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException();
                }
            }
            // Check if valid selection
            startingMoney = CachedData.getInstance().getWealthPath().getPlayerPosition(
                    (Player) CachedData.getInstance().getMe()
            );
            moneyForPolitics = ViewAlgorithms.coinForSatisfaction(chosenPolitics);
            if (moneyForPolitics > startingMoney) {
                chosenPolitics.clear();
                internalState = BASE_STATE;
                System.out.println("You don't have enough coins to do this action with the selected Politics Cards");
            } else {
                startingMoney -= moneyForPolitics;
            }
        }
    }

    private void printTowns() {
        buildReachableTowns();
        int index = 1;
        townsSelection.clear();
        for (TownName t : reachableTowns.keySet()) {
            townsSelection.put(index++, CachedData.getInstance().getTown(t));
        }
        townsSelection.keySet().iterator().forEachRemaining(integer -> {
            System.out.println(integer + ") " + townsSelection.get(integer).getTownName().toString());
        });
        internalState = CHOOSE_TOWN;
    }

    private void buildReachableTowns() {
        List<TownInterface> towns = new ArrayList<>(CachedData.getInstance().getTowns().values());
        for (TownInterface town : towns) {
            if (town.isKingHere()) {
                withKing = town;
                break;
            }
        }
        reachableTowns.clear();
        reachableTowns = GraphsAlgorithms.reachableTowns(
                CachedData.getInstance().getTowns(),
                withKing.getTownName(),
                startingMoney
        );
    }

    private void sendBuildAction(String input) {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice < 0 || choice > townsSelection.size()) throw new IllegalArgumentException();
        if (choice == 0) {
            internalState = BASE_STATE;
        } else {
            TownInterface chosenTown = townsSelection.get(choice);
            moneyForBuilding = reachableTowns.get(chosenTown.getTownName());
            CachedData.getInstance().getController().sendInfo(new BuildEmpoKingAction(
                    (Player) CachedData.getInstance().getMe(),
                    new ArrayList<>(chosenPolitics),
                    getRegionType(chosenTown.getTownName()),
                    withKing.getTownName(),
                    chosenTown.getTownName(),
                    moneyForBuilding + moneyForPolitics
            ));
        }
    }

    private RegionType getRegionType(TownName name) {
        if (name.ordinal() <= 4) return RegionType.SEA;
        if (name.ordinal() <= 9) return RegionType.HILLS;
        return RegionType.MOUNTAINS;
    }

    private void resetState() {
        internalState = BASE_STATE;
        validPolitics.clear();
        chosenPolitics.clear();
        startingMoney = moneyForBuilding = moneyForPolitics = 0;
        withKing = null;
        townsSelection.clear();
        reachableTowns.clear();
    }
}
