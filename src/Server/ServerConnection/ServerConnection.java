package Server.ServerConnection;

import Core.Connection.Connection;
import Core.Player;

/**
 * Created by Leonardo Arcari on 21/05/2016.
 */
public interface ServerConnection extends Connection {
    void setPlayer(Player player);
    Player getPlayer();
}
