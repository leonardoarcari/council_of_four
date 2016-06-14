package core.gamemodel.bonus;

import java.io.Serializable;

/**
 * Created by Matteo on 16/05/16.
 */
public abstract class Bonus implements Serializable {
    private int value;

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

    @Override
    public String toString() {
        String className = this.getClass().getName();
        className = className.substring(className.lastIndexOf("."));
        if(value == 1) return className;
        else return value + " " + className;
    }
}
