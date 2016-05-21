package Core.GameModel.Bonus;

import Core.GameModel.Bonus.Bonus;
import Core.GameModel.Bonus.BonusNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Matteo on 16/05/16.
 */
public class HireServant extends Bonus {
    public HireServant(BonusNumber bonusNumber) {
        super(bonusNumber);
    }

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
