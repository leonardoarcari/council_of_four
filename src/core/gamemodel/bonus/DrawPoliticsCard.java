package core.gamemodel.bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The DrawPoliticsCard class is one of the bonuses of the game: it allows
 * the player to draw a/some politics card from the board deck.
 */
public class DrawPoliticsCard extends Bonus {
    /**
     * @param bonusNumber is the number of other bonuses in the owner
     * @see Bonus
     */
    public DrawPoliticsCard(BonusNumber bonusNumber) {
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
                probabilities.addAll(Arrays.asList((float)(1.0/9),(float)(2.0/9),(float)(6.0/9),(float)(9.0/9)));
                break;
            case TWO_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(4.0/12),(float)(8.0/12),(float)(11.0/12),(float)(12.0/12)));
                break;
            case THREE_PROBABILITY:
                probabilities.addAll(Arrays.asList((float)(7.0/17),(float)(14.0/17),(float)(16.0/17),(float)(17.0/17)));
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
