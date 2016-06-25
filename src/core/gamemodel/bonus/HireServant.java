package core.gamemodel.bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The HireServant class is one of the bonuses of the game: it allows
 * the player to hire a servant from the servants pool of the game board.
 */
public class HireServant extends Bonus {
    /**
     * @param bonusNumber is the number of other bonuses in the owner
     * @see Bonus
     */
    public HireServant(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    /**
     * @param probabilityLevel is the probability set number of the bonus
     * @see Bonus
     * @return a randomly generated value
     */
    @Override
    public int getRandomValue(BonusNumber probabilityLevel) {
        ArrayList<Float> probabilities = new ArrayList<>();
        switch(probabilityLevel) {
            case ONE_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(1.0/7),(float)(2.0/7),(float)(5.0/7),(float)(7.0/7)));
                break;
            case TWO_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(1.0/6),(float)(3.0/6),(float)(5.0/6),(float)(6.0/6)));
                break;
            case THREE_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(3.0/9),(float)(7.0/9),(float)(8.0/9),(float)(9.0/9)));
                break;
        }
        float value = new Random().nextFloat();
        for (float i : probabilities) {
            if (value <= i) {
                value = i;
                break;
            }
        }
        return probabilities.indexOf(value)+1;
    }
}
