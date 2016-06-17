package core.gamelogic.actions;

import java.io.Serializable;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public class ServerMessage implements Serializable {
    private final String message;

    public ServerMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
