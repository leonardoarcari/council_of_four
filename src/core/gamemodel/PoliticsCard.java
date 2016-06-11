package core.gamemodel;

import core.gamemodel.modelinterface.SellableItem;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Matteo on 20/05/16.
 */
public class PoliticsCard implements SellableItem, Serializable{
    private CouncilColor cardColor;
    private String id;

    public PoliticsCard(CouncilColor cardColor) {
        id = generateID();
        this.cardColor = cardColor;
    }

    private String generateID() {
        String id = "";
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            id += random.nextInt(10);
        }
        return id;
    }

    public CouncilColor getCardColor() {
        return cardColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PoliticsCard that = (PoliticsCard) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
