package client.View;

import client.CachedData;
import core.Player;
import core.gamemodel.*;

import java.util.List;
import java.util.Vector;

/**
 * Created by Matteo on 10/06/16.
 */
public class ViewAlgorithms {
    public static synchronized boolean checkForSatisfaction(List<Councilor> councilors, List<PoliticsCard> cards) {
        List<CouncilColor> myColors = new Vector<>();
        councilors.forEach(councilor -> {
            myColors.add(councilor.getCouncilorColor());
        });

        if(CachedData.getInstance().getPlayerPoliticsCards().size() == 0) return false;
        if(CachedData.getInstance().getWealthPath() == null)
            return false;

        int rainbowSize = 0;
        List<CouncilColor> politicsColor = new Vector<>();
        List<Integer> availableColoursIndexes = new Vector<>();
        List<PoliticsCard> politics = CachedData.getInstance().getPlayerPoliticsCards();
        for(PoliticsCard card : politics) {
            politicsColor.add(card.getCardColor());
        }

        int index = 0;
        for(CouncilColor color : politicsColor) {
            if(myColors.contains(color)) {
                myColors.remove(color);
                availableColoursIndexes.add(index);
            }
            if(color.equals(CouncilColor.RAINBOW)) {
                availableColoursIndexes.add(index);
                rainbowSize++;
            }
            index++;
        }

        int myCoins = CachedData.getInstance().getWealthPath().getPlayerPosition((Player)CachedData.getInstance().getMe());
        int sum = (availableColoursIndexes.size()>=4) ? rainbowSize : 10+rainbowSize-3*(availableColoursIndexes.size()-1);
        availableColoursIndexes.forEach(ind -> {
            cards.add(politics.get(ind));
        });
        return sum <= myCoins;
    }

    public static synchronized boolean checkAvailablePermits(TownName townName, List<PermitCard> availablePermits) {
        List<PermitCard> playerPermits = CachedData.getInstance().getMyPermitCards();

        for(PermitCard card : playerPermits) {
            if(card.isBurned()) System.out.println(card.isBurned());
            if(card.getCityPermits().contains(townName) && !card.isBurned()) {
                availablePermits.add(card);
            }
        }

        return availablePermits.size()>0;
    }

    public static synchronized int coinForSatisfaction(List<PoliticsCard> politicsCardsList) {
        int rainbows = 0;
        for(PoliticsCard politicsCard : politicsCardsList)
            if(politicsCard.getCardColor().equals(CouncilColor.RAINBOW)) rainbows++;
        return 10+rainbows-3*(politicsCardsList.size()-1);
    }
}
