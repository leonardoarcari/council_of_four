package core.gamemodel.bonus;

/**
 * The RainbowStar class is one of the bonuses of the game: it allows
 * the player to do another main action.
 */
public class RainbowStar extends Bonus {
    /**
     * @param bonusNumber is the number of other bonuses in the owner
     * @see Bonus
     */
    public RainbowStar(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    /**
     * @param probabilityLevel is the probability set number of the bonus
     * @see Bonus
     * @return 1, because this bonus can only appear with a single value
     */
    protected int getRandomValue(BonusNumber probabilityLevel) {
        return 1;
    }
}
