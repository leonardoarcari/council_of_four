package client.View.cli;

/**
 * The class represents the state where the player goes into to choose a fast
 * action or eventually go back to the main menu.
 */
public class FastActionState implements CLIState {
    private CLI cli;

    /**
     * The constructor sets the context
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public FastActionState(CLI cli) {
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        System.out.println("Select a fast action: ");
        System.out.println("1) Elect a councilor");
        System.out.println("2) Hire a servant");
        System.out.println("3) Change permit cards of a region");
        System.out.println("4) Make one more main action");
        System.out.println("0) Go back");
    }

    /**
     * @param input is the choice of the player
     * @see CLIState
     * @throws IllegalArgumentException
     */
    @Override
    public void readInput(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        chooseFastActionState(choice);
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        //Do nothing
    }

    private void chooseFastActionState(int choice) throws IllegalArgumentException{
        if(choice < 0 || choice > 4) throw new IllegalArgumentException();

        switch(choice) {
            case 1:
                cli.setCurrentState(cli.getElectCouncilorFastState());
                break;
            case 2:
                cli.setCurrentState(cli.getHireServantState());
                break;
            case 3:
                cli.setCurrentState(cli.getChangePermitState());
                break;
            case 4:
                cli.setCurrentState(cli.getDoOtherActionState());
                break;
            case 0:
                cli.setCurrentState(cli.getMainState());
                break;
        }
    }
}
