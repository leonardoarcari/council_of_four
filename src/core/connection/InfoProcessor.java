package core.connection;

/**
 * A class acts as <code>InfoProcessor</code> when takes an <code>Object</code> as parameter and take decisions upon
 * its content. Think the input as a message you have to process. It's the dual of {@link Communicator Communicator}
 */
public interface InfoProcessor {
    /**
     * Processes <code>info</code>, like a message and make decisions upon its content
     * @param info Message/Information to handle
     */
    void processInfo(Object info);
}
