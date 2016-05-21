package Core.GameModel;

import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Matteo on 20/05/16. Need to add LinkTown method and attributes
 */
public class Region {
    boolean regionCardTaken;
    CouncilorsBalcony regionBalcony;
    Stack<PermitCard> regionPermitCards;
    PermitCard rightPermitCard;
    PermitCard leftPermitCard;
    Vector<Town> regionTowns;
    RegionCard regionCard;

    public Region(Town[] regionTowns, RegionCard regionCard, Councilor[] councilors) {
        this.regionTowns = new Vector<>(Arrays.asList(regionTowns));
        this.regionCard = regionCard;

        createPermitCards();
        createRegionBalcony(councilors);
        regionCardTaken = false;
    }

    private void createPermitCards() {
        for(int i = 0; i < 15; i++) {
            regionPermitCards.add(new PermitCard());
        }
    }

    private void createRegionBalcony(Councilor[] councilors) {
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
