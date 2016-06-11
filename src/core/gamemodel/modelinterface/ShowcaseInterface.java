package core.gamemodel.modelinterface;

import core.gamemodel.OnSaleItem;

import java.util.Iterator;

/**
 * Created by Leonardo Arcari on 11/06/2016.
 */
public interface ShowcaseInterface {
    Iterator<OnSaleItem> onSaleItemIterator();
}
