package Core.GameModel;

import Core.GameLogic.BonusFactory;
import Core.GameLogic.TownNameFactory;
import Core.GameModel.Bonus.Bonus;
import Core.GameModel.Bonus.BonusNumber;
import Core.GameModel.ModelInterface.SellableItem;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Matteo on 16/05/16.
 */
public class PermitCard implements SellableItem {
    public static final boolean RIGHT_CARD = true;
    public static final boolean LEFT_CARD = false;

    //TODO: add card identifier
    private RegionType region;
    private Vector<Bonus> bonuses = new Vector<>();
    private ArrayList<TownName> townEmporiumPermit = new ArrayList<>();

    public PermitCard(RegionType region) {
        this.region = region;
        BonusNumber bonusNumber = bonusNumberCalculator();
        int townEmporiumPermitNumber = emporiumPermitCalculator();

        for(int i = 0; i <= bonusNumber.ordinal(); i++) {
            bonuses.add(BonusFactory.createBonus(bonusNumber));
        }
        for(int i = 0; i < townEmporiumPermitNumber; i++) {
            ArrayList<TownName> supporter = new ArrayList<>(townEmporiumPermit);
            townEmporiumPermit.add(TownNameFactory.getTownName(supporter, region));

        }
    }

    private BonusNumber bonusNumberCalculator() {
        float randomNumber = new Random().nextFloat();

        BonusNumber bonusNumber;
        float ONES = (float) (13.0 / 45);
        float TWOS = (float) (31.0 / 45);

        if(randomNumber < ONES) {
            bonusNumber = BonusNumber.ONE_PROBABILITY;
        } else if(randomNumber < TWOS + ONES) {
            bonusNumber = BonusNumber.TWO_PROBABILITY;
        } else {
            bonusNumber = BonusNumber.THREE_PROBABILITY;
        }
        return bonusNumber;
    }

    private int emporiumPermitCalculator() {
        return new Random().nextInt(3) + 1;
    }

    public Vector<Bonus> getBonuses() {
        return bonuses;
    }

    public ArrayList<TownName> getCityPermits() { return townEmporiumPermit; }
}
