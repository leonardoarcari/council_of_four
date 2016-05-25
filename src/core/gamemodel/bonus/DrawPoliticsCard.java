package core.gamemodel.bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Matteo on 16/05/16.
 */
public class DrawPoliticsCard extends Bonus {
    public DrawPoliticsCard(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

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
