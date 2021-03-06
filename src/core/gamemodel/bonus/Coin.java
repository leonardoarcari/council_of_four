package core.gamemodel.bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The Coin class is one of the bonuses of the game: it allows the player
 * to gain coins.
 */
public class Coin extends Bonus {
    /**
     * @param bonusNumber is the number of other bonuses in the owner
     * @see Bonus
     */
    public Coin(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

    /**
     * @param probabilityLevel is the probability set number of the bonus
     * @see Bonus
     * @return a randomly generated value
     */
    protected int getRandomValue(BonusNumber probabilityLevel) {
        ArrayList<Float> probabilities = new ArrayList<>();
        switch(probabilityLevel) {
            case ONE_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(1.0/19),(float)(2.0/19),(float)(5.0/19),
                        (float)(8.0/19),(float)(13.0/19),(float)(16.0/19),(float)(19.0/19)));
                break;
            case TWO_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(1.0/11),(float)(2.0/11),(float)(4.0/11),
                        (float)(7.0/11),(float)(9.0/11),(float)(10.0/11),(float)(11.0/11)));
                break;
            case THREE_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(10.0/41),(float)(22.0/41),(float)(32.0/41),
                        (float)(38.0/41),(float)(39.0/41),(float)(40.0/41),(float)(41.0/41)));
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