package core.connection;

/**
 * A class acts as <code>Communicator</code> when takes a <code>Object</code>, serializes it and sends it to another
 * machine. It's the dual of {@link InfoProcessor InfoProcessor}
 */
public interface Communicator {
    /**
     * Serializes <code>info</code> and sends it to another machine (e.g. over the internet)
     * @param info Object to send
     */
    void sendInfo(Object info);
}
