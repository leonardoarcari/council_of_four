package core.gamemodel.bonus;

/**
 * Created by Matteo on 16/05/16.
 */
public class RainbowStar extends Bonus {
    public RainbowStar(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    protected int getRandomValue(BonusNumber probabilityLevel) {
        return 1;
    }
}
