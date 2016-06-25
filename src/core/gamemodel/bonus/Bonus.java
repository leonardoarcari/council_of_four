package core.gamemodel.bonus;

import java.io.Serializable;

/**
 * The Bonus class, as the name implies, represents all the bonuses of the game.
 */
public abstract class Bonus implements Serializable {
    private int value; // The value of the bonus, depends on the type

    /**
     * This constructor gets a random value, given the probability level,
     * that is the number of other bonuses in the owner (from one to three
     * for the permit card, one or two for the nobility path and one for
     * the town)
     *
     * @param bonusNumber is the number of other bonuses in the bonus owner
     */
    public Bonus(BonusNumber bonusNumber) {
        value = getRandomValue(bonusNumber);
    }

    /**
     * This other constructor fixes a value to the bonus. This is useful when
     * dealing with constant objects, whose bonus value doesn't change, such as
     * the royal cards, the region cards and the town type cards
     *
     * @param bonusFixedValue is the fixed value assigned to the bonus
     */
    public Bonus(int bonusFixedValue) {
        value = bonusFixedValue;
    }

    /**
     * This method generates a random value selecting a cumulative function by the
     * probability level and confronting it with a randomly generated float
     *
     * @param probabilityLevel is the probability set number of the bonus
     * @return the randomly generated value of the bonus
     */
    protected abstract int getRandomValue(BonusNumber probabilityLevel);

    /**
     * @return the value of the bonus
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the formatted value of the bonus, that is his name, given by
     * the class name, and the value, omitted if 1
     */
    @Override
    public String toString() {
        String className = this.getClass().getName();
        className = className.substring(className.lastIndexOf(".")+1);
        String[] classNames = className.split("(?=[A-Z])");
        className = "";
        for(String part : classNames)
            className = className + part + " ";
        if(value == 1) return className;
        else return value + " " + className;
    }
}
