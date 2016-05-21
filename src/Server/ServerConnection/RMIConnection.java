package Server.ServerConnection;

import Core.Connection.InfoProcessor;
import Core.Connection.RMIProcessor;
import Core.Player;
import Server.Model;
import Server.Observer;
import Server.Subject;
import Server.WaitingHall;

import java.rmi.RemoteException;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class RMIConnection implements ServerConnection, Observer {
    private RMIProcessor clientRMIProcessor;
    private RMIProcessor serverRMIProcessor;
    private Player me;

    public RMIConnection(RMIProcessor clientProcessor) {
        this.clientRMIProcessor = clientProcessor;
        serverRMIProcessor = WaitingHall.getInstance().getRMIGameProcessor();
        WaitingHall.getInstance().getModel().registerObserver(this);
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
            // Client disconnected. Remove this player from game's ones.
            e.printStackTrace();
        }
    }

    @Override
    public void processInfo(Object info) {
        try {
            serverRMIProcessor.processInfo(info);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Subject subject) {
        if (subject.getClass().equals(Model.class)) {
            sendInfo(subject);
        }
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
