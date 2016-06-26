package core.connection;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public interface Connection extends Communicator {
    void setOnDisconnection(Runnable runnable);
}
