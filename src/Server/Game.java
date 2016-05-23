package Server;

import Core.Connection.InfoProcessor;
import Core.GameModel.GameBoard;
import Core.Player;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class Game implements Runnable{
    private List<Player> players;
    private GameBoard gameBoard;
    private InfoProcessor processor;

    public Game() {
        processor = new ServerProcessor(this);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Iterator<Player> playerIterator() {
        return players.iterator();
    }

    public InfoProcessor getProcessor() {
        return processor;
    }

    @Override
    public void run() {
        players = WaitingHall.getInstance().pullPlayers();
        WaitingHall.getInstance().createNewGame();
    }
}
