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
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class ControllerUI {
    private Connection connection;
    private InfoProcessor processor;
    private UserInterface userInterface;
    private Runnable closeTask;
    private ScheduledExecutorService executorService;
    private ScheduledFuture timer;

    private int timeLeft;

    public ControllerUI(UserInterface userInterface) {
        this.userInterface = userInterface;
        processor = new UInfoProcessor(userInterface);
        closeTask = () -> System.exit(0);
        executorService = Executors.newScheduledThreadPool(1);
        timeLeft = -1;
    }

    public void rmiConnection() {
        try {
            connection = new RMIConnection(processor);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

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

    public void stopTimer() {
        timer.cancel(true);
    }

    public void quit() {
        closeTask.run();
    }

    public void sendInfo(Object info) {
        new Thread(() -> connection.sendInfo(info)).start();
    }
}
