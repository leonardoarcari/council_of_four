package core.gamelogic;

/**
 * Created by Matteo on 16/05/16.
 */
public class BonusFactory {
    private BonusFactory() {}

    public static AbstractBonusFactory getFactory(BonusOwner type) {
        if (type.equals(BonusOwner.PERMIT)) return new PermitBonusFactory();
        else if (type.equals(BonusOwner.TOWN)) return new TownBonusFactory();
        else return new NobilityBonusFactory();
    }
}