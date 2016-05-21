package Core.GameModel;

import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Matteo on 20/05/16. Need to add LinkTown method and attributes
 */
public class Region {
    boolean regionCardTaken;
    private CouncilorsBalcony regionBalcony;
    private Stack<PermitCard> regionPermitCards;
    private PermitCard rightPermitCard;
    private PermitCard leftPermitCard;
    private Vector<Town> regionTowns;
    private RegionCard regionCard;

    public Region(RegionCard regionCard, RegionType region) {
        this.regionCard = regionCard;
        createPermitCards(region);

        regionTowns = new Vector<>();
        regionPermitCards = new Stack<>();
        regionBalcony = new CouncilorsBalcony();
        regionCardTaken = false;
    }

    private void createPermitCards(RegionType region) {
        for(int i = 0; i < 15; i++) {
            regionPermitCards.add(new PermitCard(region));
        }
    }

    public void createRegionBalcony(Councilor[] councilors) {
        for(int i = 0; i < councilors.length; i++) {    //Length has to be 4
            regionBalcony.addCouncilor(councilors[i]);
        }
    }

    public boolean isRegionCardTaken() {
        return regionCardTaken;
    }

    public PermitCard drawPermitCard(boolean isRight) {
        PermitCard drawn;
        if(isRight == PermitCard.RIGHT_CARD) {
            drawn = rightPermitCard;
            rightPermitCard = regionPermitCards.pop();
        } else {
            drawn = leftPermitCard;
            leftPermitCard = regionPermitCards.pop();
        }
        return drawn;
    }

    public RegionCard drawRegionCard() throws AlreadyTakenException {
        if(!regionCardTaken) {
            regionCardTaken = true;
            return regionCard;
        } else throw  new AlreadyTakenException();
    }

    private class AlreadyTakenException extends RuntimeException {
        public AlreadyTakenException() {
            super();
        }

        public AlreadyTakenException(String message) {
            super(message);
        }
    }
}
