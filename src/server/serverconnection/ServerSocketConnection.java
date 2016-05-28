package server.serverconnection;

import core.connection.InfoProcessor;
import core.connection.SocketConnection;
import core.Player;
import core.Observer;
import core.Subject;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Leonardo Arcari on 21/05/2016.
 */
public class ServerSocketConnection extends SocketConnection implements ServerConnection {
    private Player me;

    public ServerSocketConnection(InfoProcessor processor, Socket socket) throws IOException {
        super(processor, socket);
        me = null;
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
