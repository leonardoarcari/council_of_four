package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.BuildEmpoPCAction;
import core.gamemodel.PermitCard;
import core.gamemodel.RegionType;
import core.gamemodel.TownName;
import core.gamemodel.modelinterface.PlayerInterface;
import core.gamemodel.modelinterface.TownInterface;

import java.util.*;

/**
 * Created by Leonardo Arcari on 14/06/2016.
 */
public class BuildEmporiumState implements CLIState{
    private static final int BEGIN_STATE = 0;
    private static final int CHOOSE_TOWN = 1;
    private static final int CHOOSE_PERMIT = 2;
    private int currentState;
    private boolean validState;

    private PlayerInterface me;
    private Map<TownName, List<PermitCard>> permitsPerTown;
    private Map<Integer, TownInterface> townOptions;
    private TownInterface townChoice;


    public BuildEmporiumState() {
        currentState = BEGIN_STATE;
        validState = false;
        permitsPerTown = new HashMap<>(15);
        townOptions = new HashMap<>();
    }

    @Override
    public void showMenu() {
        if (currentState == BEGIN_STATE) printTowns();
        else if (currentState == CHOOSE_PERMIT) printPermits();
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        if (currentState == CHOOSE_TOWN) storeTownChoice(input);
        else if (currentState == CHOOSE_PERMIT) {
            sendBuildAction(input);
            resetState();
        }
    }

    private void printTowns() {
        me = CachedData.getInstance().getMe();
        if (!validState) buildMaps();
        townOptions.keySet().iterator().forEachRemaining(integer -> {
            System.out.println(integer + ") " + townOptions.get(integer).getTownName().toString());
        });
        currentState = CHOOSE_TOWN;
    }

    private void buildMaps() {
        permitsPerTown.clear();
        townOptions.clear();
        me.permitCardIterator().forEachRemaining(card -> {
            card.getCityPermits().iterator().forEachRemaining(townName -> {
                if (!haveAlreadyBuilt(me, townName) && hasEnoughServants(me, townName)) {
                    permitsPerTown.putIfAbsent(townName, new ArrayList<>());
                    permitsPerTown.get(townName).add(card);
                }
            });
        });

        int option = 1;
        for (TownName name : permitsPerTown.keySet()) {
            if (permitsPerTown.get(name) != null && permitsPerTown.get(name).size() > 0) { // Meaningful list
                townOptions.put(option++, CachedData.getInstance().getTown(name));
            }
        }
        validState = true;
    }

    private boolean haveAlreadyBuilt(PlayerInterface player, TownName townName) {
        TownInterface town = CachedData.getInstance().getTown(townName);
        return town.hasEmporium((Player) player);
    }

    private boolean hasEnoughServants(PlayerInterface player, TownName townName) {
        TownInterface town = CachedData.getInstance().getTown(townName);
        return player.getServantsNumber() >= town.getEmporiumsNumber();
    }

    private void storeTownChoice(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice < 0 || choice > townOptions.size()) throw new IllegalArgumentException();
        if (choice == 0) {
            //TODO: Add go back to previous state
            resetState();
        }
        else townChoice = townOptions.get(choice);
    }

    private void printPermits() {
        List<PermitCard> permitCards = permitsPerTown.getOrDefault(townChoice, null);
        if (permitCards != null) {
            for (int i = 0; i < permitCards.size(); i++) {
                System.out.println(i+1 + ") " + permitCards.get(i).toString());
            }
        }
        currentState = CHOOSE_PERMIT;
    }

    private void sendBuildAction(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice < 0 || choice > townOptions.size()) throw new IllegalArgumentException();
        if (choice == 0) {
            currentState = BEGIN_STATE;
        } else {
            List<PermitCard> permitCards = permitsPerTown.getOrDefault(townChoice, null);
            if (permitCards != null) {
                PermitCard card = permitCards.get(choice-1);
                CachedData.getInstance().getController().sendInfo(
                        new BuildEmpoPCAction(
                                (Player) CachedData.getInstance().getMe(),
                                getRegionType(townChoice.getTownName()),
                                townChoice.getTownName(),
                                card
                        )
                );
            }
            System.out.println("Action sent");
        }
    }

    private RegionType getRegionType(TownName name) {
        if (name.ordinal() <= 4) return RegionType.SEA;
        if (name.ordinal() <= 9) return RegionType.HILLS;
        return RegionType.MOUNTAINS;
    }

    private void resetState() {
        currentState = BEGIN_STATE;
        validState = false;
        me = null;
        permitsPerTown.clear();
        townOptions.clear();
        townChoice = null;
    }
}
