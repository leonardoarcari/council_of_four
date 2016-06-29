package server;

import server.serverconnection.RMIService;
import server.serverconnection.SocketService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;

/**
 * Main class. Sets the server up by initialing the TCP and RMI connections services
 */
public class Server {
    public static int MAX_PLAYERS = 4;
    public static void main(String[] args) throws RemoteException {
        new Thread(new SocketService()).start();
        new RMIService();
    }
}
