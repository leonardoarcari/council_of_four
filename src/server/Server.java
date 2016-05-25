package server;

import server.serverconnection.RMIService;
import server.serverconnection.SocketService;

import java.rmi.RemoteException;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class Server {
    public static int MAX_PLAYERS = 4;
    public static void main(String[] args) throws RemoteException {
        new Thread(new SocketService()).start();
        new RMIService();
    }
}
