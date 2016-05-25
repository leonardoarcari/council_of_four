package core.gamelogic;

import core.gamemodel.bonus.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Leonardo Arcari on 25/05/2016.
 */
public class TownBonusFactory implements AbstractBonusFactory {
    private static final ArrayList<Float> TOWN_LOW_PROBABILITIES = new ArrayList<>(Arrays.asList(
            3f/12, 6f/12, 9f/12, 11f/12, 1f));
    private static final ArrayList<Float> TOWN_HIGH_PROBABILITIES = new ArrayList<>(Arrays.asList(
            1f/5, 2f/5, 3f/5, 4f/5, 1f));

    @Override
    public List<Bonus> generateBonuses() {
        BonusNumber bonusNumber = getBonusNumber();
        return bonusList(bonusNumber);
    }

    private BonusNumber getBonusNumber() {
        float randomNumber = new Random().nextFloat();

        float ONES = 12f/14;
        if(randomNumber < ONES) {
            return BonusNumber.ONE_PROBABILITY;
        } else {
            return BonusNumber.TWO_PROBABILITY;
        }
    }

    private List<Bonus> bonusList(BonusNumber bonusNumber) {
        List<Bonus> bonuses = new ArrayList<>();
        List<Float> probabilities = selectProbabilities(bonusNumber);

        for(int i = 0; i <= bonusNumber.ordinal(); i++) {
            bonuses.add(bonusFrom(probabilities, bonusNumber));
        }
        return bonuses;
    }

    private List<Float> selectProbabilities(BonusNumber bonusNumber) {
        if (bonusNumber.equals(BonusNumber.ONE_PROBABILITY)) return TOWN_LOW_PROBABILITIES;
        else return TOWN_HIGH_PROBABILITIES;
    }

    private Bonus bonusFrom(List<Float> probabilities, BonusNumber number) {
        float selector = new Random().nextFloat();
        for (float j : probabilities) {
            if (selector <= j) {
                selector = j;
                break;
            }
        }
        return bonusFrom(probabilities.indexOf(selector), number);
    }

    private Bonus bonusFrom(int seed, BonusNumber number) {
        Bonus bonus;
        switch(seed) {
            case 0:
                bonus = new Coin(number);
                break;
            case 1:
                bonus = new HireServant(number);
                break;
            case 2:
                bonus = new VictoryPoint(number);
                break;
            case 3:
                bonus = new NobilityPoint(number);
                break;
            case 4:
                bonus = new DrawPoliticsCard(number);
                break;
            default:
                bonus = null;
                break;
        }
        return bonus;
    }
}
