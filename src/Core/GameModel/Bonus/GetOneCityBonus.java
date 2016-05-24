package Core.GameModel.Bonus;

/**
 * Created by Matteo on 24/05/16.
 */
public class GetOneCityBonus extends Bonus {
    public GetOneCityBonus(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    @Override
    protected int getRandomValue(BonusNumber probabilityLevel) {
        return 1;
    }
}
