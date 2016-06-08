package core.gamemodel;

import core.gamemodel.modelinterface.SellableItem;

import java.io.Serializable;

/**
 * Created by Matteo on 20/05/16.
 */
public class PoliticsCard implements SellableItem, Serializable{
    private CouncilColor cardColor;

    public PoliticsCard(CouncilColor cardColor) {
        this.cardColor = cardColor;
    }

    public CouncilColor getCardColor() {
        return cardColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PoliticsCard that = (PoliticsCard) o;

        return cardColor == that.cardColor;
    }

    @Override
    public int hashCode() {
        return cardColor.hashCode();
    }
}
