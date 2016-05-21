package Core.GameModel.Bonus;

import Core.GameModel.Bonus.Bonus;
import Core.GameModel.Bonus.BonusNumber;

/**
 * Created by Matteo on 16/05/16.
 */
public class RainbowStar extends Bonus {
    public RainbowStar(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    @Override
    public int getRandomValue(BonusNumber probabilityLevel) {
        return 1;
    }
}
