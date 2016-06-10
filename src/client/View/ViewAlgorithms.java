package client.View;

import client.CachedData;
import core.Player;
import core.gamemodel.CouncilColor;
import core.gamemodel.Councilor;
import core.gamemodel.PoliticsCard;

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
        List<CouncilColor> availableColours = new Vector<>();
        List<PoliticsCard> politics = CachedData.getInstance().getPlayerPoliticsCards();
        for(PoliticsCard card : politics) {
            if(card.getCardColor().equals(CouncilColor.RAINBOW)) {
                availableColours.add(CouncilColor.RAINBOW);
                rainbowSize++;
            }
            politicsColor.add(card.getCardColor());
        }

        int index = CachedData.getInstance().getWealthPath().getPlayerPosition((Player)CachedData.getInstance().getMe());

        for(CouncilColor color : myColors) {
            if(politicsColor.contains(color)) {
                int i = politicsColor.indexOf(color);
                availableColours.add(politicsColor.remove(i));
            }
        }

        int sum = (availableColours.size()>=4) ? rainbowSize : 10+rainbowSize-3*(availableColours.size()-1);
        availableColours.forEach(color -> {
            System.out.print(color.name() + " ");
            cards.add(new PoliticsCard(color));
        });
        System.out.println();
        return sum <= index;
    }
}
