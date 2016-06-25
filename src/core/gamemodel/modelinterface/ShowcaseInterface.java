package core.gamemodel.modelinterface;

import core.gamemodel.OnSaleItem;

import java.util.Iterator;

/**
 * This interface contains the method of the Showcase class. Moreover, it acts as
 * a proxy towards the client, forcing it to use only such method. So this interface
 * is part of the Façade Pattern, applied to the model-client relationship.
 */
public interface ShowcaseInterface {
    /**
     * @return the iterator of the showcase exposed items
     */
    Iterator<OnSaleItem> onSaleItemIterator();
}
