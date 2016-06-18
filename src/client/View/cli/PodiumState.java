package client.View.cli;

import client.CachedData;
import core.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matteo on 18/06/16.
 */
public class PodiumState implements CLIState {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private Map<Integer, List<Player>> podium;

    public PodiumState() {
        podium = new HashMap<>();
    }

    public void setUpPodium(Map<Integer, List<Player>> podium) {
        this.podium.clear(); //Should do nothing, because it's called once for game
        this.podium.putAll(podium);
    }

    @Override
    public void showMenu() {
        System.out.println("Game results: ");
        for(int index : podium.keySet()) {
            String suffix = numberSuffix(index);
            if(podium.get(1).equals(CachedData.getInstance().getMe())){
                System.out.println(ANSI_GREEN + "\nYou are the winner!!!\n" + ANSI_RESET);
            } else {
                System.out.println(ANSI_BLUE + "\nYou lost...\n" + ANSI_RESET);
            }
            System.out.println(index+suffix + " place:");
            List<Player> players = podium.get(index);
            for(Player player : players) {
                if(player.equals(CachedData.getInstance().getMe())) System.out.print(ANSI_CYAN);

                System.out.print("\t" + player.getNickname() + " with: ");
                System.out.print(CachedData.getInstance().getWealthPath().getPlayerPosition(player) + " coin(s),");
                System.out.print(CachedData.getInstance().getNobilityPath().getPlayerPosition(player) + " nobility(ies)");
                System.out.println(CachedData.getInstance().getVictoryPath().getPlayerPosition(player) + "victory point(s)");
                System.out.print(ANSI_RESET);
            }
        }
        System.exit(0);
    }

    @Override
    public void readInput(String input) throws IllegalArgumentException {
        //Should not arrive here
    }

    @Override
    public void invalidateState() {
        //Should not arrive here, process already ended
    }

    private String numberSuffix(int index) {
        if(index == 1) return "st";
        else if(index == 2) return "nd";
        else if(index == 3) return "rd";
        else return "th";
    }
}
