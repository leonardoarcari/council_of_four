package Client;

import Client.ClientConnection.ClientProcessor;
import Client.ClientConnection.RMIConnection;
import Core.Connection.Connection;
import Core.Connection.InfoProcessor;
import Core.Connection.SocketConnection;

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
    private View view;

    public ControllerUI() {
        view = new View(this);
        processor = new ClientProcessor(view);
        view.chooseOption();
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
            new Thread(new SocketConnection(processor, socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
