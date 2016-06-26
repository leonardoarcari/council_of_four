package core.gamelogic.actions;

import core.Player;

/**
 * This class represents the status action sent by a player after the login,
 * thanks to which the player can communicate his info's.
 *
 * @see SpecialAction
 */
public class PlayerInfoAction implements SpecialAction {
    // Attributes of the action
    private final Player player;
    private final String username;
    private final String nickname;

    /**
     * @param player is the player doing the action
     * @param username is the username chosen by the player
     * @param nickname is the nickname chosen by the player
     */
    public PlayerInfoAction(Player player, String username, String nickname) {
        this.player = player;
        this.username = username;
        this.nickname = nickname;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }
}
