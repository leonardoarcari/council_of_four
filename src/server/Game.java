package server;

import core.Player;
import core.connection.InfoProcessor;
import core.gamelogic.actions.*;
import core.gamemodel.GameBoard;
import core.gamemodel.Town;
import core.gamemodel.TownName;
import server.serverconnection.ServerConnection;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The <code>Game</code> class represents an instance of a game and coordinates players' turns and game's phases. It has
 * knowledge of playing and disconnected clients, match's gameboard and turns' timers.<p>
 * Class is designed as a finite state machine, having stable states and moving through them in a consistent way thanks
 * to methods made available to the final user. This class does not implement the logic of the actions a player can do
 * but handles turns, market phases and game end.<p>
 * Instancing Game does not (un-wisely) allow to call methods of the class. For a full working <code>Game</code> you need
 * to call the {@link #run()} method that will perform the final bindings pulling players from {@link WaitingHall} and
 * instancing the game model.
 * After that, a random player is chosen and first turn starts. Each turn has a timer associated implemented with a
 * Scheduled thread. When the time is up the current player's turn is forced to end. After a full round market phase is
 * started allowing players to buy items following a turn cycle similar to the one described above.
 */
public class Game implements Runnable{
    private List<Player> players;
    private List<Player> lastPlayingPlayers;
    private List<Player> disconnectedPlayers;
    private GameBoard gameBoard;
    private ConfigParser mapConfig;
    private InfoProcessor processor;

    private Stack<Player> turnPlayers;
    private Turn currentTurn;
    private MarketPhase market;

    private int playerIndex;
    private boolean marketPhase;
    private boolean lastTurn;

    private ScheduledExecutorService timerGenerator;
    private ScheduledFuture timer;
    public static final int TURN_TIMER = 100;
    public static final int EXPOSURE_TIMER = 100;
    public static final int BUY_ITEMS_TIMER = 100;

    /**
     * Construct a game with no links between towns. Use this for testing purpose if you are not planning to interact
     * with the map topology.
     */
    public Game() {
        processor = new ServerProcessor(this);
        marketPhase = false;
        mapConfig = null;
        timerGenerator = new ScheduledThreadPoolExecutor(1);
    }

    /**
     * Construct a game with links provided by {@link ConfigParser mapConfig}.
     * @param mapConfig The topology of the game map
     */
    public Game(ConfigParser mapConfig) {
        this.mapConfig = mapConfig;
        processor = new ServerProcessor(this);
        marketPhase = false;
        timerGenerator = new ScheduledThreadPoolExecutor(1);
    }

    /**
     * @return A reference to the game's GameBoard
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * @return Iterator of the live players of the Game (this does not include the disconnected ones)
     */
    public Iterator<Player> playerIterator() {
        return players.iterator();
    }

    /**
     * @return A reference to the Game's {@link InfoProcessor}, implemented by {@link ServerProcessor}
     */
    public InfoProcessor getProcessor() {
        return processor;
    }

    /**
     * Set up the Game. This method MUST be called in order to have a fully working game.<p>
     * Players are pulled from {@link WaitingHall}, a new Game instance is created in the WaitingHall for new incoming
     * clients. Then the model is instanced and sent to all players. First turn is run.
     */
    @Override
    public void run() {
        players = WaitingHall.getInstance().pullPlayers();
        disconnectedPlayers = new ArrayList<>();
        turnPlayers = new Stack<>();
        WaitingHall.getInstance().createNewGame();
        gameBoard = GameBoard.createGameBoard(players);
        if (mapConfig != null) {
            setTownLinks();
            players.iterator().forEachRemaining(player -> player.getConnection().sendInfo(new LoadMapAction(mapConfig.getFileName())));
        }
        players.iterator().forEachRemaining(this::setOnDisconnect);

        gameBoard.notifyChildren();
        for (Player player : players) {
            player.notifyObservers();
        }

        players.forEach(player -> player.getConnection().sendInfo(SyncAction.GAME_START));

        // Choose randomly the beginning player
        int beginningIndex = Double.valueOf(Math.random() * players.size()).intValue();
        for (int i = beginningIndex - 1; i >= 0; i--) {
            players.add(players.remove(i));
        }
        playerIndex = 0;
        lastPlayingPlayers = new ArrayList<>();
        fillTurnPlayers();
        currentTurn = new Turn(turnPlayers.peek());
    }

    private void setTownLinks() {
        Map<TownName, Town> gameMap = gameBoard.getTownsMap();
        gameMap.keySet().forEach(townName -> {
            Town town = gameMap.get(townName);
            mapConfig.getLinksFor(townName).forEachRemaining(town::addNearby);
        });
    }

    /**
     * Return the real instance of a {@link Player}. This is particularly useful as communicating involves lot of
     * serialization/deserialization, thus player object lose their identity on the server. To the "real" model properties
     * the local copy of player is required.
     * @param player Player object to look the local instance for
     * @return "retP" the local player instance for which <code>retP.equals(player)</code> is true.
     */
    public Player getPlayerInstance(Player player) {
        int playerIndex;
        playerIndex = players.indexOf(player);
        if (playerIndex == -1) throw new NoSuchElementException();
        else return players.get(playerIndex);
    }

    /* Turn synchronization */

    /**
     * Check for <code>player</code> if he has main actions left
     * @param player Player to check for main actions availability
     * @return true if it has main actions left, false otherwise
     * @throws NotYourTurnException in case at the time of invocation it's not <code>player</code>'s turn
     */
    public boolean hasMoreMainActions(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            return !currentTurn.mainActionTokens.isEmpty();
        } else throw new NotYourTurnException();
    }

    /**
     * Add the possibility to <code>player</code> to do another main action. Subsequent calls to this method stack.
     * @param player Player to add a new main action to
     * @throws NotYourTurnException in case at the time of invocation it's not <code>player</code>'s turn
     */
    public void addMainActionToken(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            currentTurn.mainActionTokens.push(true);
        } else throw new NotYourTurnException();
    }

    /**
     * Remove a main action from <code>player</code> because, for example, he has done a main action.
     * @param player Player to pop a main action token from
     * @throws NotYourTurnException in case at the time of invocation it's not <code>player</code>'s turn or he has
     * no main actions left
     */
    public void popMainActionToken(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            if (hasMoreMainActions(player)) {
                currentTurn.mainActionTokens.pop();
                if (currentTurn.mainActionTokens.isEmpty()) player.getConnection().sendInfo(SyncAction.MAIN_ACTION_DONE);
                else player.getConnection().sendInfo(SyncAction.MAIN_ACTION_AGAIN);
            }
        } else throw new NotYourTurnException();
    }

    /**
     * Checks whether <code>player</code> has already sent a fast action previously in the current turn.
     * @param player Player to check for
     * @return True if <code>player</code> has already done a fast action in current turn. False otherwise.
     * @throws NotYourTurnException in case at the time of invocation it's not <code>player</code>'s turn
     */
    public boolean hasDoneFastAction(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            return currentTurn.doneFastAction;
        } else throw new NotYourTurnException();
    }

    /**
     * Registers that <code>player</code> has done a fast action and notify him for succeeding in it
     * @param player Player to register fast action
     * @throws NotYourTurnException in case at the time of invocation it's not <code>player</code>'s turn
     */
    public void fastActionDone(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            if (!hasDoneFastAction(player)) {
                currentTurn.doneFastAction = true;
                player.getConnection().sendInfo(SyncAction.FAST_ACTION_DONE);
            }
        } else throw new NotYourTurnException();
    }

    /**
     * Registers <code>player</code> has ended his turn. This method could be invoked either from a message by the
     * player itself either from the turn timer thread. It acknowledges the player of turn ended and takes the next
     * decision:<p>
     *     - If the round is not over yet a new turn is run the respective client notified
     *     - If the round is over market phase is started
     *     - If the turn ending is the last of the last round, the end game routine is executed
     * @param player Player is ending its turn
     * @throws NotYourTurnException in case at the time of invocation it's not <code>player</code>'s turn
     */
    public void endTurn(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            if (!timer.isDone()) timer.cancel(true);
            if (players.contains(player)) player.getConnection().sendInfo(new EndTurnAction(player));
            if (!lastTurn) {
                if (!turnPlayers.isEmpty()) turnPlayers.pop();
                if (turnPlayers.isEmpty()) setUpMarket();
                else currentTurn = new Turn(turnPlayers.peek());
            } else {
                if (!turnPlayers.isEmpty()) turnPlayers.pop();
                if (turnPlayers.isEmpty()) endGame();
                else {
                    currentTurn = new Turn(turnPlayers.peek());
                }
            }
        } else throw new NotYourTurnException();
    }

    /* Actions' events methods */

    /**
     * Notifies <code>player</code> that a 'Draw a permit card' action is required.
     * @param player Player to notify
     */
    public void drawnPermitCard(Player player) {
        if (isAllowedToGame(player)) {
            player.getConnection().sendInfo(SyncAction.DRAW_PERMIT_BONUS);
        }
    }

    /**
     * Notifies <code>player</code> that a 'Redeem a permit card bonus again' bonus is available.
     * @param player Player to notify
     */
    public void redeemPermitCardAgain(Player player) {
        if (isAllowedToGame(player)) {
            player.getConnection().sendInfo(SyncAction.PICK_PERMIT_AGAIN);
        }
    }

    /**
     * Notifies <code>player</code> that a 'Redeem a town bonus' bonus is available.
     * @param player Player to notify
     */
    public void redeemATownBonus(Player player) {
        if (isAllowedToGame(player)) {
            player.getConnection().sendInfo(SyncAction.PICK_TOWN_BONUS);
        }
    }

    /**
     * Registers a player to be the first to have built 10 emporiums, hence the last round phase must begin.
     * @param player Player who reached 10 emporiums built
     */
    public void firstToTenEmporium(Player player) {
        lastTurn = true;
        gameBoard.moveVictoryPath(player, 3);
        lastPlayingPlayers.clear();
        lastPlayingPlayers.addAll(players);
        lastPlayingPlayers.remove(player);
        turnPlayers.clear();
        for (int i = lastPlayingPlayers.size() - 1; i >= 0; i--) {
            turnPlayers.push(lastPlayingPlayers.get(i));
        }
        turnPlayers.push(player);
    }

    private void endGame() {
        // Give victory points to podium
        Map<Integer, List<Player>> nobilities = gameBoard.getNobilityPath().getPodium();
        if (nobilities.get(1).size() == 1) {
            gameBoard.moveVictoryPath(
                    nobilities.get(1).get(0),
                    5
            );
            nobilities.get(2).iterator().forEachRemaining(player -> gameBoard.moveVictoryPath(player, 2));
        } else {
            nobilities.get(1).iterator().forEachRemaining(player -> gameBoard.moveVictoryPath(player, 5));
        }

        // Give victory points to highest number of permit cards
        gameBoard.moveVictoryPath(
                players.stream().reduce(players.get(0), (player, player2) ->
                        (player.getPermitCardsNumber() >= player2.getPermitCardsNumber()) ? player : player2),
                3
        );

        Map<Integer, List<Player>> podium = gameBoard.getVictoryPath().getPodium();
        players.forEach(player -> player.getConnection().sendInfo(new PodiumAction(podium)));
        // Die
    }

    /* Market phase methods */

    /**
     * Registers a <code>player</code> to have exposed a (even empty) set of
     * {@link core.gamemodel.modelinterface.SellableItem SellableItem} in the {@link core.gamemodel.Showcase Showcase}.
     * If all the players are registered the market auction phase is started and the first potential buyer is notified.
     * @param player Player who exposed a (even empty) set of SellableItem
     * @throws NotYourTurnException in case at the time of invocation the game state is not 'market phase'
     * @throws UnsupportedOperationException in case at the time of invocation all the players already registered their
     * exposure actions
     */
    public synchronized void sellableExposed(Player player) throws NotYourTurnException {
        if (isAllowedToMarket(player)) {
            if (market.playersReady < players.size()) {
                market.playersReady++;
                if (market.playersReady == players.size()) {
                    timer.cancel(true);
                    market.setUpAction(players);
                    market.currentPlayer.getConnection().sendInfo(new AuctionStartAction(BUY_ITEMS_TIMER - 10));
                }
            } else throw new UnsupportedOperationException("Every player is already ready!");
        } else throw new NotYourTurnException();
    }

    /**
     * Registers a <code>player</code> to have sent a (even empty) buying action, thus its market turn is ended. A new
     * player is notified that it's its turn or, in case every player has done a buying action, market phase ends.
     * @param player Player who sent a (even empty) buying action
     * @throws NotYourTurnException in case at the time of invocation it's not <code>player</code>'s market turn
     */
    public synchronized void endAuctionOf(Player player) throws NotYourTurnException {
        if (isAllowedToMarket(player)) {
            timer.cancel(true);
            player.getConnection().sendInfo(MarketSyncAction.END_AUCTION_ACTION);
            try {
                market.changePlayer();
                market.currentPlayer.getConnection().sendInfo(new AuctionStartAction(BUY_ITEMS_TIMER - 10));
            } catch (MarketPhase.EndMarketException e) {
                endMarket();
            }
        } else throw new NotYourTurnException();
    }

    private void setUpMarket() {
        marketPhase = true;
        for (Player player : players) {
            player.getConnection().sendInfo(new MarketStartAction(EXPOSURE_TIMER - 10));
        }
        market = new MarketPhase();
        timer = timerGenerator.schedule(
                () -> {
                    market.playersReady = players.size();
                    try {
                        sellableExposed(players.get(0));
                    } catch (NotYourTurnException e) {
                        e.printStackTrace();
                    }
                },
                EXPOSURE_TIMER,
                TimeUnit.SECONDS
        );
    }

    private void endMarket() {
        marketPhase = false;
        for (Player player : players) {
            player.getConnection().sendInfo(MarketSyncAction.END_MARKET_ACTION);
        }
        gameBoard.getShowcase().returnItemsToOwner();
        fillTurnPlayers();
        currentTurn = new Turn(turnPlayers.peek());
    }


    private boolean isAllowedToMarket(Player player) {
        return (market.playersReady != players.size() || player.equals(market.currentPlayer)) && marketPhase;
    }

    private boolean isAllowedToGame(Player player) {
        return player.equals(currentTurn.currentPlayer) && !marketPhase;
    }

    private void fillTurnPlayers() {
        turnPlayers.clear();
        for (int i = players.size() - 1; i >= 0; i--) {
            turnPlayers.push(players.get(i));
        }
    }

    private void setOnDisconnect(Player player) {
        player.getConnection().setOnDisconnection(() -> {
            // Remove player from observer lists in game Model
            gameBoard.removeObserver((ServerConnection) player.getConnection());
            player.removeObserver((ServerConnection) player.getConnection());

            // Remove it from playing players
            disconnectedPlayers.add(players.remove(players.indexOf(player)));
            if (!marketPhase && !currentTurn.currentPlayer.equals(player)) turnPlayers.remove(player);

            // Stop running timers and end its turn in case it's needed
            if ((!marketPhase && currentTurn.currentPlayer.equals(player)) ||
                    marketPhase && market.currentPlayer.equals(player)) {
                timer.cancel(true);
                if (!marketPhase) { // In case of Turn of the Game
                    try {
                        endTurn(player);
                    } catch (NotYourTurnException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Inform other players on its disconnection
            players.iterator().forEachRemaining(player1 -> player1.getConnection().sendInfo(
                    new ServerMessage(
                            "Player " + player.getUsername() +
                                    " aka " + player.getNickname() +
                                    " has disconnected"
                    )));
        });
    }

    private class Turn {
        final Player currentPlayer;
        final Stack<Boolean> mainActionTokens;
        boolean doneFastAction;

        Turn(Player currentPlayer) {
            this.currentPlayer = currentPlayer;
            mainActionTokens = new Stack<>();
            mainActionTokens.push(true);
            doneFastAction = false;
            currentPlayer.addPoliticsCard(gameBoard.drawPoliticsCard());
            timer = timerGenerator.schedule(
                    () -> {
                        try {
                            endTurn(currentPlayer);
                        } catch (NotYourTurnException e) {
                            e.printStackTrace();
                        }
                    },
                    TURN_TIMER,
                    TimeUnit.SECONDS);
            currentPlayer.getConnection().sendInfo(new YourTurnAction(TURN_TIMER - 10));
        }
    }

    private class MarketPhase {
        int playersReady;
        int playerMarketIndex;
        Player currentPlayer;
        List<Player> auctionPlayers;

        MarketPhase() {
            playersReady = 0;
            playerMarketIndex = 0;
        }

        void setUpAction(List<Player> playerList) {
            auctionPlayers = new ArrayList<>(playerList);
            Collections.shuffle(auctionPlayers);
            currentPlayer = auctionPlayers.get(0);
            timer = timerGenerator.schedule(
                    () -> {
                        try {
                            endAuctionOf(currentPlayer);
                        } catch (NotYourTurnException e) {
                            e.printStackTrace();
                        }
                    },
                    BUY_ITEMS_TIMER,
                    TimeUnit.SECONDS
            );
        }

        void changePlayer() throws EndMarketException {
            if (playerMarketIndex < auctionPlayers.size() - 1) {
                currentPlayer = auctionPlayers.get(++playerMarketIndex);
                timer = timerGenerator.schedule(
                        () -> {
                            try {
                                endAuctionOf(currentPlayer);
                            } catch (NotYourTurnException e) {
                                e.printStackTrace();
                            }
                        },
                        BUY_ITEMS_TIMER,
                        TimeUnit.SECONDS
                );
            } else throw new EndMarketException();
        }

        class EndMarketException extends Exception {
            EndMarketException() {}
            public EndMarketException(String message) {
                super(message);
            }
        }

    }

    class NotYourTurnException extends Exception {
        NotYourTurnException() {
            super("not your turn");
        }

        public NotYourTurnException(String message) {
            super(message);
        }
    }
}
