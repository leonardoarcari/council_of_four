package core.gamemodel.bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The NobilityPoint class is one of the bonuses of the game: it allows
 * the player to advance in the nobility path by as much boxes as the bonus
 * value.
 */
public class NobilityPoint extends Bonus {
    /**
     * @param bonusNumber is the number of other bonuses in the owner
     * @see Bonus
     */
    public NobilityPoint(BonusNumber bonusNumber) {
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
                probabilities.addAll(Arrays.asList((float)(1.0/6),(float)(6.0/6)));
                break;
            case TWO_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(4.0/7),(float)(7.0/7)));
                break;
            case THREE_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(5.0/6),(float)(6.0/6)));
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
