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
 * Class for queueing incoming players that are waiting to start a new game. Provides methods to add a player to the
 * waiting list and pull them from it. It also contains a reference to the game that's getting set up and will begin as
 * soon as the timer fires or the maximum number of queued players is reached. Hence, a method to create a new Game
 * is provided to substitute the old one.
 *
 * Due to its nature, this class is implemented with the Singleton pattern in its thread-safe variant.
 */

public class WaitingHall {
    private volatile static WaitingHall instance = null;
    private Game game;
    private List<ConfigParser> maps;
    private List<Player> waitingPlayers;
    private ScheduledExecutorService scheduledExecutor;
    private ScheduledFuture<?> timer;

    /**
     * Standard Singleton getInstance method.
     * @return a reference to the instance of WaitingHall. The first time this method is called WaitingHall is created.
     */
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

    /**
     * Add a {@link Player} to the list of waiting players. If <code>p</code> is the second player added a thread is
     * scheduled to automatically start a game after the time is up. If <code>p</code> is the maximum-number-th player
     * the scheduled thread is canceled and the pending game is manually started. See {@link Game#run()} for more
     * details on game start.
     * @param p Player to add to a list of waiting players
     */
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

    /**
     * Return to the invoker a list of players waiting at the moment of invocation and clears the one in WaitingHall.
     * @return List of players waiting for game to start
     */
    public synchronized List<Player> pullPlayers() {
        List<Player> result = new ArrayList<>(waitingPlayers);
        waitingPlayers.clear();
        return result;
    }

    /**
     * Create a new {@link Game} initialized with a map chosen randomly among those available. Reference is stored and
     * represents the pending Game players are joining.
     */
    public void createNewGame() {
        game = new Game(maps.get(Double.valueOf(Math.random() * maps.size()).intValue()));
    }

    public InfoProcessor getInfoProcessor() { return game.getProcessor();}
}
