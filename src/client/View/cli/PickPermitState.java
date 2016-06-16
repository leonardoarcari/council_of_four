package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.PermitNoPayAction;
import core.gamemodel.PermitCard;
import core.gamemodel.Region;
import core.gamemodel.RegionType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matteo on 16/06/16.
 */
public class PickPermitState implements CLIState {
    private Map<PermitCard, Region> permitsMap;
    private Map<Integer, PermitCard> indexMap;
    private int currentIndex;
    private boolean validate;

    public PickPermitState() {
        permitsMap = new HashMap<>();
        indexMap = new HashMap<>();
        validate = false;
    }

    @Override
    public void showMenu() {
        if(!validate) {
            permitsMap.clear();
            indexMap.clear();
            currentIndex = 1;
            fillMaps();
        }

        if(currentIndex == 1) {
            System.out.println("No permits available to buy");
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

        if(indexMap.keySet().contains(choice)) {
            PermitCard permitCard = indexMap.get(choice);
            Region region = permitsMap.get(permitCard);
            Action action = new PermitNoPayAction((Player)CachedData.getInstance().getMe(), region.getRegionType(),
                    (region.getLeftPermitCard().equals(permitCard))? Region.PermitPos.LEFT: Region.PermitPos.RIGHT);
            CachedData.getInstance().getController().sendInfo(action);
            validate = false;
            //TODO change state
        } else throw new IllegalArgumentException();
    }

    private void fillMaps() {
       fillMapsBy(CachedData.getInstance().getSeaRegion());
       fillMapsBy(CachedData.getInstance().getHillsRegion());
        fillMapsBy(CachedData.getInstance().getMountainsRegion());
        validate = true;
    }

    private void fillMapsBy(Region currentRegion) {
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
