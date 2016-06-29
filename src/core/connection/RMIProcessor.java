package core.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A <code>RMIProcessor</code> has the same behaviour of {@link InfoProcessor InfoProcessor} except that it used in
 * combination with RMI framework. Requiring every <code>Remote</code> class method to throw a
 * <code>RemoteException</code>, inheriting from InfoProcessor is not possible.
 */
public interface RMIProcessor extends Remote {
    /**
     * @see InfoProcessor#processInfo(Object)
     * @param info Message/Information to handle
     * @throws RemoteException
     */
    void processInfo(Object info) throws RemoteException;
}
