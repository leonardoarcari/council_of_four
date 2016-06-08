package server.serverconnection;

import core.Observer;
import core.Player;
import core.connection.Connection;

/**
 * Created by Leonardo Arcari on 21/05/2016.
 */
public interface ServerConnection extends Connection, Observer {
    void setPlayer(Player player);
    Player getPlayer();
}
