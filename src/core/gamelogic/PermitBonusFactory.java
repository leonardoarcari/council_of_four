package core.gamelogic;

import core.gamemodel.bonus.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Leonardo Arcari on 25/05/2016.
 */
public class PermitBonusFactory implements AbstractBonusFactory{
    private static final ArrayList<Float> CARD_LOW_PROBABILITIES = new ArrayList<>(Arrays.asList(
            3f/13, 6f/13, 9f/13, 10f/13, 12f/13, 1f));
    private static final ArrayList<Float> CARD_MEDIUM_PROBABILITIES = new ArrayList<>(Arrays.asList(
            7f/31, 14f/31, 20f/31, 25f/31, 30f/31, 1f));
    private static final ArrayList<Float> CARD_HIGH_PROBABILITIES = new ArrayList<>(Arrays.asList(
            2f/10, 4f/10, 6f/10, 8f/10, 9.7f/10, 1f));

    public Bonus createOneBonus() {return bonusFrom(CARD_LOW_PROBABILITIES,BonusNumber.ONE_PROBABILITY);}

    public List<Bonus> generateBonuses() {
        BonusNumber bonusNumber = getBonusNumber();
        return bonusList(bonusNumber);
    }

    private BonusNumber getBonusNumber() {
        float randomNumber = new Random().nextFloat();

        BonusNumber bonusNumber;
        float ONES = 13f/45;
        float TWOS = 31f/45;

        if(randomNumber < ONES) {
            bonusNumber = BonusNumber.ONE_PROBABILITY;
        } else if(randomNumber < TWOS + ONES) {
            bonusNumber = BonusNumber.TWO_PROBABILITY;
        } else {
            bonusNumber = BonusNumber.THREE_PROBABILITY;
        }
        return bonusNumber;
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
        if (bonusNumber.equals(BonusNumber.ONE_PROBABILITY)) return CARD_LOW_PROBABILITIES;
        else if (bonusNumber.equals(BonusNumber.TWO_PROBABILITY)) return CARD_MEDIUM_PROBABILITIES;
        else return CARD_HIGH_PROBABILITIES;
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
            default:
                bonus = null;
                break;
        }
        return bonus;
    }
}
