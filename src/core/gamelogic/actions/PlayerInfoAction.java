package core.gamelogic.actions;

/**
 * Created by Leonardo Arcari on 08/06/2016.
 */
public class PlayerInfoAction implements SpecialAction {
    private final String username;
    private final String nickname;

    public PlayerInfoAction(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }
}
