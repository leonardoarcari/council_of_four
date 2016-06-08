package server;


import core.Player;
import core.connection.InfoProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class WaitingHall {
    private volatile static WaitingHall instance = null;
    private Game game;
    private List<Player> waitingPlayers;
    private ScheduledExecutorService scheduledExecutor;
    private ScheduledFuture<?> timer;
    private int gamesCounter;

    public static WaitingHall getInstance() {
        if (instance == null) {
            synchronized (WaitingHall.class) {
                if (instance == null) {
                    instance = new WaitingHall();
                }
            }
        }
        return instance;
    }

    private WaitingHall() {
        game = new Game();
        waitingPlayers = new Vector<>();
        scheduledExecutor = new ScheduledThreadPoolExecutor(1);
        timer = null;
        gamesCounter = 0;
    }

    public synchronized void addPlayer(Player p) {
        waitingPlayers.add(p);
        if (waitingPlayers.size() == 2) { // Start countdown
            timer = scheduledExecutor.schedule(game, 5L, TimeUnit.SECONDS);
        }
        if (waitingPlayers.size() == Server.MAX_PLAYERS) { // Immediately start the game
            timer.cancel(false);
            new Thread(game).start();
        }
    }

    public synchronized List<Player> pullPlayers() {
        List<Player> result = new ArrayList<>(waitingPlayers);
        waitingPlayers.clear();
        return result;
    }

    public void createNewGame() {
        game = new Game();
    }

    public Game getGame() {
        return game;
    }

    public InfoProcessor getInfoProcessor() { return game.getProcessor();}

    public int getGamesCounter() {
        return gamesCounter;
    }
}
