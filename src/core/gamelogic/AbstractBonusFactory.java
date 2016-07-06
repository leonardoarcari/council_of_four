package core.gamelogic;

import core.gamemodel.bonus.Bonus;

import java.util.List;

/**
 * This class acts as the abstract class of the Abstract Factory
 * pattern. It allows the creation of different factories.
 */
public interface AbstractBonusFactory {
    /**
     * @return a list of bonus randomly generate
     */
    List<Bonus> generateBonuses();

    /**
     * @return a single bonus, useful for objects that has no more than
     * one bonus to create
     */
    Bonus createOneBonus();
}
