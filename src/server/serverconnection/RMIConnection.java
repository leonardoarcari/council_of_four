package server.serverconnection;

import core.Player;
import core.Subject;
import core.connection.InfoProcessor;
import core.connection.RMIProcessor;
import server.WaitingHall;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class RMIConnection implements RMIProcessor, ServerConnection {
    private RMIProcessor clientRMIProcessor;
    private InfoProcessor serverProcessor;
    private Player me;
    private Runnable onDisconnect;

    public RMIConnection(RMIProcessor clientProcessor) throws RemoteException {
        this.clientRMIProcessor = clientProcessor;
        serverProcessor = WaitingHall.getInstance().getInfoProcessor();
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void setInfoProcessor(InfoProcessor processor) {
        // Does nothing. InfoProcessor is set by the model for all RMI clients
    }

    @Override
    public void sendInfo(Object info) {
        try {
            clientRMIProcessor.processInfo(info);
        } catch (RemoteException e) {
            // client disconnected. Remove this player from game's ones.
            if (onDisconnect != null) onDisconnect.run();
        }
    }

    @Override
    public void processInfo(Object info) throws RemoteException{
        serverProcessor.processInfo(info);
    }

    @Override
    public void setProcessor(InfoProcessor processor) throws RemoteException {
        serverProcessor = processor;
    }

    @Override
    public void setOnDisconnection(Runnable runnable) {
        onDisconnect = runnable;
    }

    @Override
    public void update(Subject subject) {
        sendInfo(subject);
    }

    @Override
    public void setPlayer(Player player) {
        me = player;
    }

    @Override
    public Player getPlayer() {
        return me;
    }
}
