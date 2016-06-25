package core.gamemodel.bonus;

/**
 * Created by Matteo on 24/05/16.
 */
public class GetOneCityBonus extends Bonus {
    public GetOneCityBonus(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    protected int getRandomValue(BonusNumber probabilityLevel) {
        return 1;
    }
}
