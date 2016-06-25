package core.gamemodel.bonus;

/**
 * This enumeration allows, for each bonus, the selection of a fixed cumulative function,
 * given the number of other bonuses of the owner. If an object has two bonuses, the cumulative
 * function that calculates the probability of a bonus with a specific type would be
 * different from the cumulative function of the same bonus type, owned by another object with
 * one or two bonuses. With this enumeration values, every bonus is able to generate randomly its
 * value, knowing how many "brothers" it has.
 */
public enum BonusNumber {ONE_PROBABILITY, TWO_PROBABILITY, THREE_PROBABILITY, NO_PROBABILITY}
