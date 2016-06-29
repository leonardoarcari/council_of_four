package core.connection;

/**
 * A <code>Connection</code> abstracts the concept of connection between two machines. Acts like a <code>Communicator
 * </code> because it is able to send data over the link. In addition you can set a callback to be called when the
 * connection closes or crashes.
 */
public interface Connection extends Communicator {
    /**
     * Registers a callback to be invoked upon connection closure or crash. An example could be a clean up routine
     * @param runnable Runnable to call on above conditions
     */
    void setOnDisconnection(Runnable runnable);
}
