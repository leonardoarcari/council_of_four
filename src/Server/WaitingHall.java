package Server;


import Core.Connection.InfoProcessor;
import Core.Connection.RMIProcessor;
import Core.Player;

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
    private Model model;
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
        model = new Model();
        waitingPlayers = new Vector<>();
        scheduledExecutor = new ScheduledThreadPoolExecutor(1);
        timer = null;
        gamesCounter = 0;
    }

    public void addPlayer(Player p) {
        waitingPlayers.add(p);
        if (waitingPlayers.size() == 2) {
            timer = scheduledExecutor.schedule(model, 5L, TimeUnit.SECONDS);
        }
    }

    public List<Player> pullPlayers() {
        List<Player> result = new ArrayList<>(waitingPlayers);
        waitingPlayers.clear();
        return result;
    }

    public Model getModel() {
        return model;
    }

    public RMIProcessor getRMIGameProcessor() {
        return model.getRMIGameProcessor();
    }

    public InfoProcessor getInfoProcessor() { return model.getProcessor();}

    public int getGamesCounter() {
        return gamesCounter;
    }

    public void incrementGamesCounter() {
        gamesCounter++;
    }
}
