package server;

import core.connection.InfoProcessor;
import core.gamelogic.actions.AnotherMainActionAction;
import core.gamelogic.actions.SyncAction;
import core.gamemodel.GameBoard;
import core.Player;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Created by Leonardo Arcari on 23/05/2016.
 */
public class Game implements Runnable{
    private List<Player> players;
    private GameBoard gameBoard;
    private InfoProcessor processor;

    private Turn currentTurn;
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
            player.getConnection().sendInfo(SyncAction.MAIN_ACTION_AGAIN);
        } else throw new NotYourTurnException();
    }

    public void popMainActionToken(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            if (hasMoreMainActions(player)) currentTurn.mainActionTokens.pop();
        } else throw new NotYourTurnException();
    }

    public boolean hasDoneFastAction(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            return currentTurn.doneFastAction;
        } else throw new NotYourTurnException();
    }

    public void fastActionDone(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            if (!hasDoneFastAction(player)) currentTurn.doneFastAction = true;
        } else throw new NotYourTurnException();
    }

    public void endTurn(Player player) throws NotYourTurnException {
        if (isAllowedToGame(player)) {
            playerIndex = (playerIndex + 1) % Server.MAX_PLAYERS;
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
    public void setUpMarket() {
        //TODO: Add market feature
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
        }
    }

    public class NotYourTurnException extends Exception {
        public NotYourTurnException() {
        }

        public NotYourTurnException(String message) {
            super(message);
        }
    }
}
