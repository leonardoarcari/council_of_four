package Server.ServerConnection;

import Core.Connection.InfoProcessor;
import Core.Connection.SocketConnection;
import Core.GameLogic.Actions.Action;
import Core.Player;
import Server.Observer;
import Server.Subject;
import Server.WaitingHall;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Leonardo Arcari on 21/05/2016.
 */
public class ServerSocketConnection extends SocketConnection implements ServerConnection, Observer {
    private Player me;

    public ServerSocketConnection(InfoProcessor processor, Socket socket) throws IOException {
        super(processor, socket);
        //WaitingHall.getInstance().getModel().registerObserver(this);
    }

    @Override
    protected void packPlayer(Object info) {
        if (info instanceof Action) {
            Action action = (Action) info;
            action.setPlayer(me);
        }
    }

    @Override
    public void update(Subject subject) {
        /*if (subject.getClass().equals(Model.class)) {
            sendInfo(subject);
        }*/
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
