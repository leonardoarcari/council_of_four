package Server.ServerConnection;

import Core.Connection.RMIProcessor;
import Core.Connection.RMIServiceInterface;
import Core.Player;
import Server.WaitingHall;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class RMIService implements RMIServiceInterface {
    private Registry registry;
    private String startingGameProcessorName = "rmi_server_processor_";

    public RMIService() throws RemoteException {
        registry = LocateRegistry.createRegistry(1099);
        UnicastRemoteObject.exportObject(this, 0);
        registry.rebind("rmi_server_service", this);
    }

    @Override
    public RMIProcessor connect(RMIProcessor clientProcessor) throws RemoteException {
        RMIConnection playerConnection = new RMIConnection(clientProcessor);
        Player player = new Player(playerConnection);
        playerConnection.setPlayer(player);
        WaitingHall.getInstance().addPlayer(player);
        return playerConnection;
    }
}
