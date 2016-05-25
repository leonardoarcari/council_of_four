package Core.GameModel.Bonus;

/**
 * Created by Matteo on 25/05/16.
 */
public class GetOwnPermitBonus extends Bonus {
    public GetOwnPermitBonus(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    @Override
    protected int getRandomValue(BonusNumber probabilityLevel) {
        return 1;
    }
}
