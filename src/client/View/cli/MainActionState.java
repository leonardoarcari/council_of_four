package client.View.cli;

/**
 * Created by Matteo on 16/06/16.
 */
public class MainActionState implements CLIState {
    private CLI cli;

    public MainActionState(CLI cli) {
        this.cli = cli;
    }

    @Override
    public void showMenu() {
        System.out.println("Select a normal action: ");
        System.out.println("1) Elect a councilor");
        System.out.println("2) Satisfy a council and buy a permit card");
        System.out.println("3) Build emporium with owned permit card");
        System.out.println("4) Build emporium with the King help");
        System.out.println("0) Go back");
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        chooseMainAction(choice);
    }

    @Override
    public void invalidateState() {
        //Do nothing
    }

    private void chooseMainAction(int choice) {
        if(choice < 0 || choice > 4) throw new IllegalArgumentException();

        switch(choice) {
            case 1:
                cli.setCurrentState(cli.getElectCouncilorMainState());
                break;
            case 2:
                cli.setCurrentState(cli.getBuyPermitCardState());
                break;
            case 3:
                cli.setCurrentState(cli.getBuildEmporiumState());
                break;
            case 4:
                cli.setCurrentState(cli.getBuildEmporiumWithKingState());
                break;
            case 0:
                cli.setCurrentState(cli.getMainState());
                break;
        }
    }
}
