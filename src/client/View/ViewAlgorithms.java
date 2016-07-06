package client.View;

import client.CachedData;
import core.Player;
import core.gamemodel.*;

import java.util.List;
import java.util.Vector;

/**
 * Utility class providing algorithms useful on the clients to do checks on the availability of actions to send to the
 * server
 */
public class ViewAlgorithms {
    /**
     * Checks whether the client can satisfy the set of councilors given in input (typically from a balcony) with any
     * combination of <code>PoliticsCard</code>s in player's hand. Game rules say that a player must pay:
     * <ul>
     *     <li>10 coins for 1 PoliticsCard matching any councilor color</li>
     *     <li>7 coins for 2 PoliticsCard matching any councilor color</li>
     *     <li>4 coins for 3 PoliticsCard matching any councilor color</li>
     *     <li>0 coins for 4 PoliticsCard matching any councilor color</li>
     *     <li>1 additional coin for each rainbow card in place of a colored one</li>
     * </ul>
     * The algorithm, in case of <code>true</code> result, populates <code>cards</code> with a subset of the PoliticsCards
     * in player's hand that can satisfy <code>councilors</code>
     * @param councilors List of <code>Councilor</code>s to check for satisfiability
     * @param cards Empty List to populate
     * @return <code>true</code> if above rules are satisfied, <code>false</code> otherwise
     */
    public static synchronized boolean checkForSatisfaction(List<Councilor> councilors, List<PoliticsCard> cards) {
        List<CouncilColor> myColors = new Vector<>();
        councilors.forEach(councilor -> myColors.add(councilor.getCouncilorColor()));

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
        availableColoursIndexes.forEach(ind -> cards.add(politics.get(ind)));
        return sum <= myCoins;
    }

    /**
     * Looks the player's <code>PermitCard</code>s for those providing a permit to build in <code>townName</code>.
     * Each valid card is added to input <code>availablePermits</code> list.
     * @param townName TownName to look permit cards for
     * @param availablePermits Empty list to populate with permit cards for the input townName
     * @return <code>true</code> if after searching, <code>availablePermits.size() &gt; 0</code>, <code>false</code> otherwise.
     */
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

    /**
     * Evaluates the numbers of coins required to satisfy a <code>Balcony</code> with the input <code>PoliticsCards</code>.
     * @see #checkForSatisfaction(List, List) for the rules on how coins are calculated.
     * @param politicsCardsList List of <code>PoliticsCard</code> to evaluate the number of coins to pay for
     * @return The number of coins to pay
     */
    public static synchronized int coinForSatisfaction(List<PoliticsCard> politicsCardsList) {
        int rainbows = 0;
        for(PoliticsCard politicsCard : politicsCardsList)
            if(politicsCard.getCardColor().equals(CouncilColor.RAINBOW)) rainbows++;
        return 10+rainbows-3*(politicsCardsList.size()-1);
    }
}
