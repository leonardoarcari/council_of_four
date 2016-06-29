package server.serverconnection;

import core.Player;
import core.Subject;
import core.connection.InfoProcessor;
import core.connection.SocketConnection;

import java.io.IOException;
import java.net.Socket;

/**
 * A <code>ServerSocketConnection</code> is a {@link SocketConnection SocketConnection} behaving also like a
 * {@link ServerConnection ServerConnection}.
 */
public class ServerSocketConnection extends SocketConnection implements ServerConnection {
    private Player me;

    /**
     * Initializes a <code>ServerSocketConnection</code>. Any call to {@link #getPlayer()} before setting it will return
     * <code>null</code>.
     * @param processor InfoProcessor to ask for messages processing after de-serializing them
     * @param socket TCP Socket
     * @throws IOException
     */
    public ServerSocketConnection(InfoProcessor processor, Socket socket) throws IOException {
        super(processor, socket);
        me = null;
    }

    /**
     * Serialize and send a <code>Subject</code> to the player returned by {@link #getPlayer()}
     * @param subject Game model object to send that changed its state
     */
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
