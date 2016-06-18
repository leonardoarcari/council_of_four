package server;


import core.Player;
import core.connection.InfoProcessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private List<ConfigParser> maps;
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
        waitingPlayers = new Vector<>();
        maps = new ArrayList<>();
        scheduledExecutor = new ScheduledThreadPoolExecutor(1);
        timer = null;
        gamesCounter = 0;
        loadMapsConfig();
        game = new Game(maps.get(Double.valueOf(Math.random() * maps.size()).intValue()));
    }

    private void loadMapsConfig() {
        Path paths = Paths.get("", "mapsConfig", "paths");
        try {
            Files.list(paths).forEach(System.out::println);
            Files.list(paths).forEach(path -> {
                try {
                    System.out.println(path.toString() + "\t" + path.getFileName().toString());
                    maps.add(new ConfigParser(path.toFile(), path.getFileName().toString()));
                } catch (FileNotFoundException | ConfigParser.SyntaxErrorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        game = new Game(maps.get(Double.valueOf(Math.random() * maps.size()).intValue()));
    }

    public Game getGame() {
        return game;
    }

    public InfoProcessor getInfoProcessor() { return game.getProcessor();}

    public int getGamesCounter() {
        return gamesCounter;
    }
}
