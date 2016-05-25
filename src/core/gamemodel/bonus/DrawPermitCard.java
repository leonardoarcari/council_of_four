package core.gamemodel.bonus;

/**
 * Created by Matteo on 24/05/16.
 */
public class DrawPermitCard extends Bonus {

    public DrawPermitCard(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    @Override
    protected int getRandomValue(BonusNumber probabilityLevel) {
        return 1;
    }
}
