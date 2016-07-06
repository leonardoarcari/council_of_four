package client;

import client.View.UserInterface;
import client.clientconnection.UInfoProcessor;
import client.clientconnection.RMIConnection;
import core.Player;
import core.connection.Connection;
import core.connection.InfoProcessor;
import core.connection.SocketConnection;
import core.gamelogic.actions.BuyObjectsAction;
import core.gamelogic.actions.EndTurnAction;
import core.gamelogic.actions.ExposeSellablesAction;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A <code>ControllerUI</code> is a class useful for <code>UserInterface</code> for handling the connection establishment,
 * timers run and stop and sending messages to the server.
 */
public class ControllerUI {
    private Connection connection;
    private InfoProcessor processor;
    private UserInterface userInterface;
    private Runnable closeTask;
    private ScheduledExecutorService executorService;
    private ScheduledFuture timer;

    private int timeLeft;

    /**
     * Initializes a <code>ControllerUI</code> by creating a new <code>UIInfoProcessor</code> for the input <code>
     * UserInterface</code>.
     * @param userInterface UserInterface reference to create a ControllerUI for.
     */
    public ControllerUI(UserInterface userInterface) {
        this.userInterface = userInterface;
        processor = new UInfoProcessor(userInterface);
        closeTask = () -> System.exit(0);
        executorService = Executors.newScheduledThreadPool(1);
        timeLeft = -1;
    }

    /**
     * Establishes a RMIConnection to the game server
     */
    public void rmiConnection() {
        try {
            connection = new RMIConnection(processor);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establishes a standard TCP Socket connection to the game server running on <code>localhost</code> on
     * <code>2828</code> port.
     */
    public void socketConnection() {
        try {
            Socket socket = new Socket("127.0.0.1", 2828);
            connection = new SocketConnection(processor, socket);
            Thread socketThread = new Thread((SocketConnection) connection);
            closeTask = () -> {
                try {
                    ((SocketConnection) connection).close();
                    socketThread.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            socketThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Schedules a routing to be run every second. This is the turn timer that updates the userInterface
     * @param time Timer time in seconds
     */
    public void runTurnTimer(int time) {
        timeLeft = time;
        timer = executorService.scheduleAtFixedRate(
                () -> {
                    if (--timeLeft < 1) onTurnTimeUp();
                    userInterface.setTimer(timeLeft);
                },
                2,
                1,
                TimeUnit.SECONDS
        );
    }

    private void onTurnTimeUp() {
        timer.cancel(true);
        sendInfo(new EndTurnAction((Player) CachedData.getInstance().getMe()));
    }

    /**
     * Schedules a routing to be run every second. This is the turn timer that updates the userInterface
     * @param time Timer time in seconds
     * @param exposure <code>true</code> if the timer starting is for the exposure phase of the market, <code>false</code>
     * if for the auction phase
     */
    public void runMarketTimer(int time, boolean exposure) {
        timeLeft = time;
        timer = executorService.scheduleAtFixedRate(
                () -> {
                    if (--timeLeft < 1) onMarketTimer(exposure);
                    userInterface.setTimer(timeLeft);
                },
                2,
                1,
                TimeUnit.SECONDS
        );
    }

    private void onMarketTimer(boolean exposure) {
        timer.cancel(true);
        if (exposure) {
            userInterface.forceExposureEnd();
            sendInfo(new ExposeSellablesAction((Player) CachedData.getInstance().getMe(), new ArrayList<>()));
        } else {
            userInterface.forceBuyingEnd();
            sendInfo(new BuyObjectsAction((Player) CachedData.getInstance().getMe(), new ArrayList<>()));
        }
    }

    /**
     * Stops a timer previously started
     */
    public void stopTimer() {
        timer.cancel(true);
    }

    /**
     * Executes final cleanups before closing the application
     */
    public void quit() {
        closeTask.run();
    }

    /**
     * Sends in an independent thread <code>info</code> to the server over a previously established connection. Does
     * nothing if no connection is available
     * @param info Information to send to the server.
     */
    public void sendInfo(Object info) {
        if (connection != null)
            new Thread(() -> connection.sendInfo(info)).start();
    }
}
