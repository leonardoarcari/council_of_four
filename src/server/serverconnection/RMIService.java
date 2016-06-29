package server.serverconnection;

import core.Player;
import core.connection.RMIProcessor;
import core.connection.RMIServiceInterface;
import server.WaitingHall;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * A <code>RMIService</code> class that accept incoming RMI connections, setting a
 * {@link core.connection.Connection Connection} up representing a playing client on the server.
 */
public class RMIService implements RMIServiceInterface {
    private String startingGameProcessorName = "rmi_server_processor_";

    /**
     * Initializes a <code>RMIService</code> running the RMIRegistry on the local machine on the <code>1099 port</code>
     * and exposing itself on it.
     * @throws RemoteException
     */
    public RMIService() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);
        UnicastRemoteObject.exportObject(this, 0);
        registry.rebind("rmi_server_service", this);
    }

    /**
     * Sets a RMI connection up by exchanging between client and server the respective {@link RMIProcessor RMIProcessors}
     * so that the server can send message to be processed on the client and the client with the server as well.
     * <code>clientProcessor</code> is wrapped in a {@link RMIConnection RMIConnection}, a newly allocated
     * {@link Player Player} is set into it and added to the waiting players' list in {@link WaitingHall WaitingHall}.
     * @param clientProcessor The client's RMIProcessor
     * @return A RMIProcessor the client will interact with to communicate with the server
     * @throws RemoteException
     */
    @Override
    public RMIProcessor connect(RMIProcessor clientProcessor) throws RemoteException {
        RMIConnection playerConnection = new RMIConnection(clientProcessor);
        Player player = new Player(playerConnection);
        playerConnection.setPlayer(player);
        WaitingHall.getInstance().addPlayer(player);
        return playerConnection;
    }
}
