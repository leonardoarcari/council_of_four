package core.gamelogic;

/**
 * This class allows the creation of different factories, all sharing
 * the static type AbstractBonusFactory, given the owner. The owner is
 * the specific objects that needs the factory.
 *
 * @see BonusOwner
 */
public class BonusFactory {
    private BonusFactory() {}

    /**
     * @param type is the object asking for a factory to generate its bonuses
     * @return a specific factory
     */
    public static AbstractBonusFactory getFactory(BonusOwner type) {
        if (type.equals(BonusOwner.PERMIT)) return new PermitBonusFactory();
        else if (type.equals(BonusOwner.TOWN)) return new TownBonusFactory();
        else return new NobilityBonusFactory();
    }
}