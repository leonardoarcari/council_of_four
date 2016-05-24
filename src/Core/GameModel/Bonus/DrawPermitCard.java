package Core.GameModel.Bonus;

/**
 * Created by Matteo on 24/05/16.
 */
public class DrawPermitCard extends Bonus {

    public DrawPermitCard(int fixedValue) {
        super(fixedValue);
    }

    @Override
    protected int getRandomValue(BonusNumber probabilityLevel) {
        return 1;
    }
}
