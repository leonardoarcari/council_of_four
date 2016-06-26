package core.gamelogic.actions;

/**
 * This enumeration allows the server to communicate to the players
 * a special event, such as the beginning of the game, a special bonus
 * action available, and so on.
 */
public enum SyncAction {
    // Values of the enumeration
    GAME_START,
    DRAW_PERMIT_BONUS,
    PICK_PERMIT_AGAIN,
    PICK_TOWN_BONUS,
    MAIN_ACTION_AGAIN,
    MAIN_ACTION_DONE,
    FAST_ACTION_DONE
}
