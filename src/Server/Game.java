package Server;

import Core.GameModel.GameBoard;
import Core.Player;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class Game {
    private List<Player> players;
    private GameBoard gameBoard;

    public Game() {
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Iterator<Player> playerIterator() {
        return players.iterator();
    }
}
