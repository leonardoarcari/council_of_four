package client.View.cli;

import client.CachedData;
import core.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class shows the podium and the position of the player. It slightly
 * differs from the other states because the real settings are done only
 * when the podium phase occurs, and not at the creation of the context.
 * This is done because the podium phase is the only one unknown at the
 * beginning of the game.
 */
public class PodiumState implements CLIState {
    // Colors escape code to apply at strings
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";

    // Attribute of the class
    private Map<Integer, List<Player>> podium;

    /**
     * This constructor only initialize the class attribute
     */
    public PodiumState() {
        podium = new HashMap<>();
    }

    /**
     * This method sets up the class attribute and is invoked before the
     * context double phase of show and read
     *
     * @param podium is the map with the players positions
     */
    public void setUpPodium(Map<Integer, List<Player>> podium) {
        this.podium.clear(); //Should do nothing, because it's called once for game
        this.podium.putAll(podium);
    }

    /**
     * This method shows the podium, informing the player whether he as lost or won
     *
     * @see CLIState
     */
    @Override
    public void showMenu() {
        System.out.println("Game results: ");

        if(podium.get(1).equals(CachedData.getInstance().getMe())){
            System.out.println(ANSI_GREEN + "\nYou are the winner!!!\n" + ANSI_RESET);
        } else {
            System.out.println(ANSI_BLUE + "\nYou lost...\n" + ANSI_RESET);
        }

        for(int index : podium.keySet()) {
            String suffix = numberSuffix(index);
            System.out.println(index+suffix + " place:");
            List<Player> players = podium.get(index);
            for(Player player : players) {
                if(player.equals(CachedData.getInstance().getMe())) System.out.print(ANSI_CYAN);

                System.out.print("\t" + player.getNickname() + " with: ");
                System.out.print(CachedData.getInstance().getWealthPath().getPlayerPosition(player) + " coin(s), ");
                System.out.print(CachedData.getInstance().getNobilityPath().getPlayerPosition(player) + " nobility(ies), ");
                System.out.println(CachedData.getInstance().getVictoryPath().getPlayerPosition(player) + " victory point(s)");
                System.out.print(ANSI_RESET);
            }
        }
        System.exit(0);
    }

    /**
     * This method should never be reached, because the application
     * terminates after the context show phase.
     *
     * @param input is the choice of the player
     * @see CLIState
     * @throws IllegalArgumentException
     */
    @Override
    public void readInput(String input) throws IllegalArgumentException {
        //Should not arrive here
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        //Should not arrive here, process already ended
    }

    private String numberSuffix(int index) {
        if(index == 1) return "st";
        else return "nd";
    }
}

