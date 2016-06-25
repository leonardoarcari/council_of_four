package client.View.cli;

/**
 * This class is the first state the player find himself in. Here he is asked to insert
 * his info's, such as nickname and username, and the type of the connection, socket
 * or RMI. To handle these requests, the class needs different internal state, that will
 * be sequentially called after each one is completed.
 */
public class LoginState implements CLIState {
    // Internal states of the class
    private static final int BASE_STATE = 0;
    private static final int INSERT_USERNAME = 1;
    private static final int INSERT_NICKNAME = 2;
    private static final int CHOOSE_CONNECTION = 3;

    private int internalState;

    // Reference to the context
    private CLI cli;

    /**
     * The constructor sets the beginning state
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public LoginState(CLI cli) {
        this.cli = cli;
        internalState = BASE_STATE;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        if (internalState == BASE_STATE) askUserName();
        else if (internalState == INSERT_USERNAME) askNickName();
        else if (internalState == INSERT_NICKNAME) askConnectionType();
    }

    /**
     * @param input is the choice of the player
     * @see CLIState
     * @throws IllegalArgumentException
     */
    @Override
    public void readInput(String input) throws IllegalArgumentException {
        if (internalState == INSERT_USERNAME) storeUsername(input);
        else if (internalState == INSERT_NICKNAME) storeNickname(input);
        else if (internalState == CHOOSE_CONNECTION) login(input);
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        internalState = BASE_STATE;
    }

    private void askUserName() {
        System.out.println("--- Login ---\n" +
                "\n" +
                "Type your username:");
        internalState = INSERT_USERNAME;
    }

    private void storeUsername(String input) {
        cli.setUsername(input.trim());
    }

    private void askNickName() {
        System.out.println("Type your nickname: ( 0 to go back )");
        internalState = INSERT_NICKNAME;
    }

    private void storeNickname(String input) {
        if (input.trim().equals("0")) internalState = BASE_STATE;
        else cli.setNickname(input.trim());
    }

    private void askConnectionType() {
        System.out.println("Choose your connection type:\n" +
                "1) Socket\n" +
                "2) RMI\n" +
                "0) Back");
        internalState = CHOOSE_CONNECTION;
    }

    private void login(String input) throws IllegalArgumentException{
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (choice == 0) internalState = INSERT_NICKNAME;
        else {
            if (choice == 1) cli.getController().socketConnection();
            else if (choice == 2) cli.getController().rmiConnection();
            cli.setCurrentState(cli.getWaitingState());
        }
    }
}
