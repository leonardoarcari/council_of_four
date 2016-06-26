package core.gamelogic.actions;

import java.io.Serializable;

/**
 * This class allows the server to communicate the players that
 * someone has disconnected, using a message.
 */
public class ServerMessage implements Serializable {
    // The message sent by the server
    private final String message;

    /**
     * @param message is the message sent from the server to communicate
     *                a player disconnection
     */
    public ServerMessage(String message) {
        this.message = message;
    }

    /**
     * @return the message sent
     */
    public String getMessage() {
        return message;
    }
}
