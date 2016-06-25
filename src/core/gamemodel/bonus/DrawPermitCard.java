package core.gamemodel.bonus;

/**
 * The DrawPermitCard class is one of the special bonuses of the game,
 * gained from the nobility path: it allows the player to pick a permit card
 * for free.
 */
public class DrawPermitCard extends Bonus {
    /**
     * @param bonusNumber is the number of other bonuses in the owner
     * @see Bonus
     */
    public DrawPermitCard(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    /**
     * @param probabilityLevel is the probability set number of the bonus
     * @see Bonus
     * @return 1, because this bonus can only appear with a single value
     */
    @Override
    protected int getRandomValue(BonusNumber probabilityLevel) {
        return 1;
    }
}
