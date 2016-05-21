package Core.GameModel.Bonus;

import Core.GameModel.Bonus.Bonus;
import Core.GameModel.Bonus.BonusNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Matteo on 16/05/16.
 */
public class VictoryPoint extends Bonus {
    public VictoryPoint(BonusNumber bonusNumber) {
        super(bonusNumber);
    }
    public VictoryPoint(int bonusFixedValue) { super(bonusFixedValue); }

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
