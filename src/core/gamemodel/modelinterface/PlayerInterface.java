package core.gamemodel.modelinterface;

import core.gamemodel.*;
import javafx.scene.paint.Color;

import java.util.Iterator;

/**
 * Created by Matteo on 07/06/16.
 */
public interface PlayerInterface  {
    String getUsername();
    String getNickname();
    Color getColor();
    int getServantsNumber();
    int getPermitCardsNumber();
    int getPoliticsCardsNumber();
    int getRoyalCardsNumber();
    Iterator<PermitCard> permitCardIterator();
    Iterator<RoyalCard> royalCardIterator();
    Iterator<PoliticsCard> politicsCardIterator();
    Iterator<RegionCard> regionCardIterator();
    Iterator<TownTypeCard> townCardIterator();
}
