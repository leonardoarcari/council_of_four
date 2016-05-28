package client.clientconnection;


import client.View;
import core.Player;
import core.connection.InfoProcessor;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class ClientProcessor implements InfoProcessor {
    private View view;

    public ClientProcessor(View view) {
        this.view = view;
    }

    @Override
    public void processInfo(Object info) {
        Class<?> objClass = info.getClass();
        if (info.getClass().equals(Player.class)) {
            Player player = (Player) info;
            view.setPlayer(player);
        }
    }
}
