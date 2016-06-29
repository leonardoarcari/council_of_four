package server.serverconnection;

import core.Observer;
import core.Player;
import core.connection.Connection;

/**
 * A <code>ServerConnection</code> is a {@link Connection Connection} that contains a reference to the
 * {@link Player Player} object that communicates through this connection. It also acts as Observer in the
 * Publish/Subscribe pattern implemented between connections on the server and the game model
 */
public interface ServerConnection extends Connection, Observer {
    /**
     * Registers the <code>player</code> that owns this <code>Connection</code>
     * @param player Owner of this connection
     */
    void setPlayer(Player player);

    /**
     * @return The reference to the Player that uses this connection to communicate
     */
    Player getPlayer();
}
