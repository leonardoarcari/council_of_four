package client;

import client.View.GUI;
import client.clientconnection.GUInfoProcessor;
import client.clientconnection.RMIConnection;
import core.connection.Connection;
import core.connection.InfoProcessor;
import core.connection.SocketConnection;
import core.gamelogic.actions.Action;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class ControllerUI {
    private Connection connection;
    private InfoProcessor processor;
    private GUI gui;

    public ControllerUI(GUI gui) {
        this.gui = gui;
        processor = new GUInfoProcessor(gui);
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
            new Thread((SocketConnection) connection).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(Object info) {
        new Thread(() -> connection.sendInfo(info)).start();
    }
}
