package server;

import core.connection.InfoProcessor;
import core.gamemodel.GameBoard;
import core.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
        gameBoard = GameBoard.createGameBoard(players);

        // Choose randomly the beginning player
        int beginningIndex = Double.valueOf(Math.random() * players.size()).intValue();
        for (int i = beginningIndex - 1; i >= 0; i--) {
            players.add(players.remove(i));
        }
        playerIndex = 0;
        currentTurn = new Turn(players.get(playerIndex));
    }

    public void addMainActionToken(Player player) throws NotYourTurnException {
        if (player.equals(currentTurn.currentPlayer)) {
            currentTurn.mainActionTokens.push(true);
        } else throw new NotYourTurnException();
    }

    public boolean hasMoreMainActions(Player player) throws NotYourTurnException {
        if (player.equals(currentTurn.currentPlayer)) {
            return !currentTurn.mainActionTokens.isEmpty();
        } else throw new NotYourTurnException();
    }

    public void popMainActionToken(Player player) throws NotYourTurnException {
        if (player.equals(currentTurn.currentPlayer)) {
            if (hasMoreMainActions(player)) currentTurn.mainActionTokens.pop();
        } else throw new NotYourTurnException();
    }

    public void endTurn(Player player) throws NotYourTurnException {
        if (player.equals(currentTurn.currentPlayer)) {
            playerIndex = (playerIndex + 1) % Server.MAX_PLAYERS;
            if (playerIndex == 0) setUpMarket();
            else currentTurn = new Turn(players.get(playerIndex));
        } else throw new NotYourTurnException();
    }

    public void setUpMarket() {
        //TODO: Add market feature
    }

    private class Turn {
        final Player currentPlayer;
        Stack<Boolean> mainActionTokens;

        public Turn(Player currentPlayer) {
            this.currentPlayer = currentPlayer;
            mainActionTokens = new Stack<>();
            mainActionTokens.push(true);
        }
    }

    private class NotYourTurnException extends Exception {
        public NotYourTurnException() {
        }

        public NotYourTurnException(String message) {
            super(message);
        }
    }
}
