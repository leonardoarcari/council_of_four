package Core.GameModel;

/**
 * Created by Matteo on 20/05/16.
 */
public class CouncilorsBalcony {
    private Councilor[] councilorsBalcony;

    public CouncilorsBalcony() {
        councilorsBalcony = new Councilor[4];
    }

    /**
     * Clean punch in the balls function
     */
    public Councilor addCouncilor(Councilor councilor) {
        Councilor councilorToAddToPool = councilorsBalcony[3];
        for(int i = 3; i > 0; i--) {
            councilorsBalcony[i] = councilorsBalcony[i-1];
        }
        councilorsBalcony[0] = councilor;
        return councilorToAddToPool;
    }

    @Override
    public String toString() {
        String toString = "";
        for (int i = 0; i < councilorsBalcony.length; i++) {
            toString = toString + councilorsBalcony[i] + " ";
        }
        return toString;
    }
}
