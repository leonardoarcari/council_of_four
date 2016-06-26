package core.gamelogic.actions;

/**
 * The values of this enumeration represents the signals that the client
 * receives when a market phase ends.
 *
 * @see MarketStartAction
 * @see AuctionStartAction
 */
public enum MarketSyncAction {
    END_AUCTION_ACTION,
    END_MARKET_ACTION
}
