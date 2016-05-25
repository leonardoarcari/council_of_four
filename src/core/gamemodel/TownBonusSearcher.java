package core.gamemodel;

/**
 * Created by Matteo on 23/05/16.
 */
public class TownBonusSearcher {
    /**
     * All the method within this class are static. The first is an implementation
     * of a tree-research algorithm and it gradually fills a list of bonus gained
     * by the player who owns multiple connected emporiums in different cities.
     * Then an analyzer cycles through this list and for each bonus (or the sum...)
     * it calls a game object method of update. An example to clear our minds: if
     * the players has only cities which give him coins, at the end of the algorithm
     * the list will contain only bonus objects of Coin type. The other static
     * private method will analyze this list and, at the end, will call the WealthPath
     * movePlayer method. There are two disadvantages with such approach.
     * 1) There are five possible bonuses: coins, servants, victory points,
     *    nobility points and draw politic card. The first four will call the
     *    respective object method, as in the previous example, but what about the
     *    latter one?
     * 2) Having a reference of 4 objects (3 Paths and the Player) may be a pain to
     *    handle. Is there any alternative?
     */
}
