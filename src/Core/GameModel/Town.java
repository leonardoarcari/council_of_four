package Core.GameModel;

import Core.GameLogic.BonusFactory;
import Core.GameModel.Bonus.Bonus;
import Core.GameModel.Bonus.BonusNumber;
import Core.Player;

import java.util.Vector;

/**
 * Created by Matteo on 20/05/16.
 */
public class Town {
    TownName townName;
    TownType townType;
    Vector<Town> nearbyTown;
    Bonus townBonus;
    Vector<Player> playersEmporium;
    boolean kingHere;

    public Town(TownName townName, TownType townType, Vector<Town> nearbyTown) {
        this.townName = townName;
        this.townType = townType;
        this.nearbyTown = nearbyTown;

        this.townBonus = BonusFactory.createBonus(BonusNumber.ONE_PROBABILITY);

        if(townName.equals(TownName.J)) {
            kingHere = true;
        } else {
            kingHere = false;
        }
    }

    public boolean isKingHere() {
        return kingHere;
    }

    public void setKingHere() {
        kingHere = true;
    }

    public TownType getTownType() {
        return townType;
    }

    public void createEmporium(Player player) {
        playersEmporium.add(player);
    }

    public boolean hasPlayerEmporium(Player player) {
        return playersEmporium.contains(player);
    }

    public Bonus getTownBonus() {
        return townBonus;
    }
}
