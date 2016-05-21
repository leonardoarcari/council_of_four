package Core.GameLogic;

import Core.GameModel.Bonus.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Matteo on 16/05/16.
 */
public abstract class BonusFactory {
    private static final ArrayList<Float> LOW_PROBABILITIES = new ArrayList<>(Arrays.asList(
            (float)(3.0/13),(float)(6.0/13),(float)(9.0/13),(float)(10.0/13),(float)(12.0/13),(float)(13.0/13)
    ));
    private static final ArrayList<Float> MEDIUM_PROBABILITIES = new ArrayList<>(Arrays.asList(
            (float)(7.0/31),(float)(14.0/31),(float)(20.0/31),(float)(25.0/31),(float)(29.0/31),(float)(31.0/31)
    ));
    private static final ArrayList<Float> HIGH_PROBABILITIES = new ArrayList<>(Arrays.asList(
            (float)(2.0/10),(float)(4.0/10),(float)(6.0/10),(float)(8.0/10),(float)(9.0/10),(float)(10.0/10)
    ));

    public static Bonus createBonus(BonusNumber bonusNumber) {
        Bonus bonusType = BonusFactory.bonusTypeGenerator(bonusNumber);
        return bonusType;

    }

    private static Bonus bonusTypeGenerator(BonusNumber bonusNumber) {

        float selector = new Random().nextFloat();
        ArrayList<Float> probabilities = null;

        switch(bonusNumber) {
            case ONE_PROBABILITY:
                probabilities = LOW_PROBABILITIES;
                break;
            case TWO_PROBABILITY:
                probabilities = MEDIUM_PROBABILITIES;
                break;
            case THREE_PROBABILITY:
                probabilities = HIGH_PROBABILITIES;
                break;
            default:
                break;
        }
        for (float i : probabilities) {
            if (selector <= i) {
                selector = i;
                break;
            }
        }

        Bonus bonus;
        switch(probabilities.indexOf(selector)) {
            case 0:
                bonus = new Coin(bonusNumber);
                break;
            case 1:
                bonus = new HireServant(bonusNumber);
                break;
            case 2:
                bonus = new VictoryPoint(bonusNumber);
                break;
            case 3:
                bonus = new NobilityPoint(bonusNumber);
                break;
            case 4:
                bonus = new DrawPoliticsCard(bonusNumber);
                break;
            case 5:
                bonus = new RainbowStar(bonusNumber);
                break;
            default:
                bonus = null;
                break;
        }

        return bonus;
    }
}