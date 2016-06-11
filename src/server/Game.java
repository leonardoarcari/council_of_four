package server;

import core.Player;
import core.connection.InfoProcessor;
import core.gamelogic.actions.EndTurnAction;
import core.gamelogic.actions.MarketSyncAction;
import core.gamelogic.actions.SyncAction;
import core.gamemodel.GameBoard;

import java.util.*;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class Game implements Runnable{
    private List<Player> players;
    private GameBoard gameBoard;
    private InfoProcessor processor;

    private Turn currentTurn;
    private MarketPhase market;

    private int playerIndex;
    private boolean marketPhase;

    public Game() {
        processor = new ServerProcessor(this);
        marketPhase = false;
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
        gameBoard = GameBoard.createGameBoard(players);

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
        currentTurn = new Turn(players.get(playerIndex));
    }

    public Player getPlayerInstance(Player player) {
        int playerIndex;
        playerIndex = players.indexOf(player);
        if (playerIndex == -1) throw new NoSuchElementException();
        else return players.get(playerIndex);
    }

    /* Turn synchronization */
    public boolean hasMoreMainActions(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            return !currentTurn.mainActionTokens.isEmpty();
        } else throw new NotYourTurnException();
    }

    public void addMainActionToken(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            currentTurn.mainActionTokens.push(true);
        } else throw new NotYourTurnException();
    }

    public void popMainActionToken(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            if (hasMoreMainActions(player)) {
                currentTurn.mainActionTokens.pop();
                if (currentTurn.mainActionTokens.isEmpty()) player.getConnection().sendInfo(SyncAction.MAIN_ACTION_DONE);
                else player.getConnection().sendInfo(SyncAction.MAIN_ACTION_AGAIN);
            }
        } else throw new NotYourTurnException();
    }

    public boolean hasDoneFastAction(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            return currentTurn.doneFastAction;
        } else throw new NotYourTurnException();
    }

    public void fastActionDone(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            if (!hasDoneFastAction(player)) {
                currentTurn.doneFastAction = true;
                player.getConnection().sendInfo(SyncAction.FAST_ACTION_DONE);
            }
        } else throw new NotYourTurnException();
    }

    public void endTurn(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            player.getConnection().sendInfo(new EndTurnAction(player));
            playerIndex = (playerIndex + 1) % players.size();
            if (playerIndex == 0) setUpMarket();
            else currentTurn = new Turn(players.get(playerIndex));
        } else throw new NotYourTurnException();
    }

    /* Actions' events methods */
    public void drawnPermitCard(Player player) {
        if (isAllowedToGame(player)) {
            player.getConnection().sendInfo(SyncAction.DRAW_PERMIT_BONUS);
        }
    }

    public void redeemPermitCardAgain(Player player) {
        if (isAllowedToGame(player)) {
            player.getConnection().sendInfo(SyncAction.PICK_PERMIT_AGAIN);
        }
    }

    public void redeemATownBonus(Player player) {
        if (isAllowedToGame(player)) {
            player.getConnection().sendInfo(SyncAction.PICK_TOWN_BONUS);
        }
    }

    /* Market phase methods */
    public synchronized void sellableExposed(Player player) throws NotYourTurnException {
        if (isAllowedToMarket(player)) {
            if (market.playersReady < players.size()) {
                market.playersReady++;
                if (market.playersReady == players.size()) {
                    market.setUpAction(players);
                    market.currentPlayer.getConnection().sendInfo(MarketSyncAction.AUCTION_START_ACTION);
                }
            } else throw new UnsupportedOperationException("Every player is already ready!");
        } else throw new NotYourTurnException();
    }

    public synchronized void endAuctionOf(Player player) throws NotYourTurnException {
        if (isAllowedToMarket(player)) {
            player.getConnection().sendInfo(MarketSyncAction.END_AUCTION_ACTION);
            try {
                market.changePlayer();
                market.currentPlayer.getConnection().sendInfo(MarketSyncAction.AUCTION_START_ACTION);
            } catch (MarketPhase.EndMarketException e) {
                endMarket();
            }
        } else throw new NotYourTurnException();
    }

    private void setUpMarket() {
        marketPhase = true;
        for (Player player : players) {
            player.getConnection().sendInfo(MarketSyncAction.MARKET_START_ACTION);
        }
        market = new MarketPhase();
    }

    public void endMarket() {
        marketPhase = false;
        for (Player player : players) {
            player.getConnection().sendInfo(MarketSyncAction.END_MARKET_ACTION);
        }
        gameBoard.getShowcase().returnItemsToOwner();
        System.out.println("new round");
        currentTurn = new Turn(players.get(playerIndex));
    }


    private boolean isAllowedToMarket(Player player) {
        return (market.playersReady != players.size() || player.equals(market.currentPlayer)) && marketPhase;
    }

    private boolean isAllowedToGame(Player player) {
        return player.equals(currentTurn.currentPlayer) && !marketPhase;
    }

    private class Turn {
        final Player currentPlayer;
        final Stack<Boolean> mainActionTokens;
        boolean doneFastAction;

        public Turn(Player currentPlayer) {
            this.currentPlayer = currentPlayer;
            mainActionTokens = new Stack<>();
            mainActionTokens.push(true);
            doneFastAction = false;
            currentPlayer.addPoliticsCard(gameBoard.drawPoliticsCard());
            currentPlayer.getConnection().sendInfo(SyncAction.YOUR_TURN);
        }
    }

    private class MarketPhase {
        int playersReady;
        int playerMarketIndex;
        Player currentPlayer;
        List<Player> auctionPlayers;

        public MarketPhase() {
            playersReady = 0;
            playerMarketIndex = 0;
        }

        void setUpAction(List<Player> playerList) {
            auctionPlayers = new ArrayList<>(playerList);
            Collections.shuffle(auctionPlayers);
            currentPlayer = auctionPlayers.get(0);
        }

        void changePlayer() throws EndMarketException {
            if (playerMarketIndex < auctionPlayers.size() - 1) {
                currentPlayer = auctionPlayers.get(++playerMarketIndex);
            } else throw new EndMarketException();
        }

        class EndMarketException extends Exception {
            public EndMarketException() {
            }

            public EndMarketException(String message) {
                super(message);
            }
        }

    }

    public class NotYourTurnException extends Exception {
        public NotYourTurnException() {
            super("not your turn");
        }

        public NotYourTurnException(String message) {
            super(message);
        }
    }
}
