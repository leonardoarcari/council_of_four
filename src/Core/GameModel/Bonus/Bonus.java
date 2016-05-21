package Core.GameModel.Bonus;

/**
 * Created by Matteo on 16/05/16.
 */
public abstract class Bonus {
    private final int value;

    public Bonus(BonusNumber bonusNumber) {
        value = getRandomValue(bonusNumber);
    }

    public Bonus(int bonusFixedValue) {
        value = bonusFixedValue;
    }

    protected abstract int getRandomValue(BonusNumber probabilityLevel);

    public int getValue() {
        return value;
    }
}
