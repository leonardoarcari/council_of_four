package core.gamelogic;

import core.gamemodel.bonus.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Is the factory class that creates, with a Factory pattern, the bonuses for
 * the nobility path. The class creates the number of bonuses and type, delegating
 * the value creation to the bonus itself.
 */
public class NobilityBonusFactory implements AbstractBonusFactory {
    private static final ArrayList<Float> NOBILITY_LOW_PROBABILITIES = new ArrayList<>(Arrays.asList(
            1f/12, 2f/12, 13f/24, 14f/24, 16f/24, 18f/24, 20f/24, 22f/24, 1f));
    private static final ArrayList<Float> NOBILITY_HIGH_PROBABILITIES = new ArrayList<>(Arrays.asList(
            8f/44, 9f/44, 25f/44, 26f/44, 32f/44, 34f/44, 36f/44, 38f/44, 1f));

    /**
     * @return a single bonus
     */
    public Bonus createOneBonus() {return bonusFrom(NOBILITY_LOW_PROBABILITIES,BonusNumber.ONE_PROBABILITY);}

    /**
     * @return a list of randomly generated bonuses
     */
    public List<Bonus> generateBonuses() {
        BonusNumber bonusNumber = getBonusNumber();
        if (bonusNumber.equals(BonusNumber.NO_PROBABILITY)) return new ArrayList<>();
        return bonusList(bonusNumber);
    }

    private BonusNumber getBonusNumber() {
        float randomNumber = new Random().nextFloat();

        float ZEROES = 10f/21;
        float ONES = 18f/21;
        if(randomNumber < ZEROES) {
            return BonusNumber.NO_PROBABILITY;
        } else if(randomNumber < ONES) {
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
        if (bonusNumber.equals(BonusNumber.ONE_PROBABILITY)) return NOBILITY_HIGH_PROBABILITIES;
        else return NOBILITY_LOW_PROBABILITIES;
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
            case 5:
                bonus = new RainbowStar(number);
                break;
            case 6:
                bonus = new GetOwnPermitBonus(number);
                break;
            case 7:
                bonus = new DrawPermitCard(number);
                break;
            case 8:
                bonus = new GetOneCityBonus(number);
                break;
            default:
                bonus = null;
                break;
        }
        return bonus;
    }
}
