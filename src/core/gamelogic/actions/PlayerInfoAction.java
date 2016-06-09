package core.gamelogic.actions;

import core.Player;

/**
 * Created by Leonardo Arcari on 08/06/2016.
 */
public class PlayerInfoAction implements SpecialAction {
    private final Player player;
    private final String username;
    private final String nickname;

    public PlayerInfoAction(Player player, String username, String nickname) {
        this.player = player;
        this.username = username;
        this.nickname = nickname;
    }

    public Player getPlayer() {
        return player;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }
}
