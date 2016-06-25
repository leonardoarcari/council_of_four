package core.gamemodel.bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The VictoryPoint class is one of the bonuses of the game: it allows
 * the player to advance in the victory path by as much boxes as the bonus
 * value.
 */
public class VictoryPoint extends Bonus {
    /**
     * @param bonusNumber is the number of other bonuses in the owner
     * @see Bonus
     */
    public VictoryPoint(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    /**
     * This constructor is invoked when creating special cards, such
     * as the royal cards or the region cards.
     *
     * @param bonusFixedValue is the fixed value assigned to the bonus
     * @see Bonus
     */
    public VictoryPoint(int bonusFixedValue) { super(bonusFixedValue); }

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
                probabilities.addAll(Arrays.asList((float)(1.0/19),(float)(2.0/19),(float)(5.0/19),
                        (float)(8.0/19),(float)(11.0/19),(float)(15.0/19),(float)(19.0/19)));
                break;
            case TWO_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(1.0/12),(float)(2.0/12),(float)(4.0/12),
                        (float)(8.0/12),(float)(10.0/12),(float)(11.0/12),(float)(12.0/12)));
                break;
            case THREE_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(10.0/45),(float)(25.0/45),(float)(36.0/45),
                        (float)(42.0/45),(float)(43.0/45),(float)(44.0/45),(float)(45.0/45)));
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
