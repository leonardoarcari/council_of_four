package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamemodel.Councilor;
import core.gamemodel.RegionType;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.RegionInterface;
import core.gamemodel.modelinterface.TownInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The class acts as an information centre of the game. The player can select
 * what specific game object status wats to see, from the regions to the paths.
 */
public class ObjectStatusState implements CLIState {
    // Reference to the context
    private CLI cli;

    /**
     * The constructor sets the context
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public ObjectStatusState(CLI cli) {
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        System.out.println("Choose object to see its state:");
        System.out.println("1) Councilors pool");
        System.out.println("2) Balconies");
        System.out.println("3) Regions");
        System.out.println("4) Towns");
        System.out.println("5) Wealth path");
        System.out.println("6) Nobility path");
        System.out.println("7) Victory path");
        System.out.println("0) Back");
    }

    /**
     * @param input is the choice of the player
     * @see CLIState
     * @throws IllegalArgumentException
     */
    @Override
    public void readInput(String input) throws IllegalArgumentException{
        int choice = 0;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        switch(choice) {
            case 1:
                showCouncilorPool();
                break;
            case 2:
                showBalconies();
                break;
            case 3:
                showRegions();
                break;
            case 4:
                showTowns();
                break;
            case 5:
                showWealthPath();
                break;
            case 6:
                showNobilityPath();
                break;
            case 7:
                showVictoryPath();
                break;
            case 0:
                cli.setCurrentState(cli.getMainState());
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @see CLIState
     */
    @Override
    public void invalidateState() {
        //Do nothing
    }

    private void showCouncilorPool() {
        Iterator<Councilor> councilorIterator = CachedData.getInstance().getCouncilorPool();
        while(councilorIterator.hasNext()) {
            Councilor currentCouncilor = councilorIterator.next();
            System.out.println("\t" + currentCouncilor.toString() + " councilor");
        }
        System.out.println();
    }

    private void showBalconies() {
        System.out.println((CachedData.getInstance().getBalcony(RegionType.SEA)).toFormattedString());
        System.out.println((CachedData.getInstance().getBalcony(RegionType.HILLS)).toFormattedString());
        System.out.println((CachedData.getInstance().getBalcony(RegionType.MOUNTAINS)).toFormattedString());
        System.out.println((CachedData.getInstance().getBalcony(RegionType.KINGBOARD)).toFormattedString());
    }

    private void showRegions() {
        printRegionStatus(CachedData.getInstance().getSeaRegion());
        printRegionStatus(CachedData.getInstance().getHillsRegion());
        printRegionStatus(CachedData.getInstance().getMountainsRegion());
    }

    private void printRegionStatus(RegionInterface region) {
        System.out.println(region.getRegionType().name() + " region");
        System.out.println((!region.isRegionCardTaken()) ?
                ("Region card: "+ region.getRegionCard().getRegionBonus().toString()) : "Region card already taken!");
        System.out.println((region.getLeftPermitCard() == null) ?
                "No more left permit card" : ("Left permit card: "+region.getLeftPermitCard().toString()));
        System.out.println((region.getRightPermitCard() == null) ?
                "No more right permit card" : ("Right permit card: "+region.getRightPermitCard().toString()));
    }

    private void showTowns() {
        List<TownInterface> towns = new ArrayList<>(CachedData.getInstance().getTowns().values());
        for(TownInterface town : towns) {
            System.out.println(town.getTownName().toString().toUpperCase());
            System.out.println("\tType:" + town.getTownType().name());
            System.out.println("\tBonus: " + ((town.getTownBonus()==null) ? "no bonus" : town.getTownBonus().toString()));
            System.out.print("\tNearby towns: [ ");
            town.nearbiesIterator().forEachRemaining(townName -> System.out.print(townName.name() + " "));
            System.out.println("]");
            System.out.println("\tTown built emporiums: ");
            if(!town.getPlayersEmporium().hasNext()) System.out.println("\t\tnone");
            else {
                Iterator<Player> emporiumIterator = town.getPlayersEmporium();
                while(emporiumIterator.hasNext()) {
                    Player emporium = emporiumIterator.next();
                    System.out.println("\t\t(Username , Nickname) " + emporium.getUsername() + " - " + emporium.getNickname());
                }
            }
            System.out.println("\n");
        }
    }

    private void showWealthPath() {
        List<List<Player>> playerLists = CachedData.getInstance().getWealthPath().getPlayers();
        showPlayerList(playerLists, "coin(s)");
    }

    private void showNobilityPath() {
        List<List<Bonus>> bonusLists = CachedData.getInstance().getNobilityPath().getBonusPath();
        List<List<Player>> playerLists = CachedData.getInstance().getNobilityPath().getPlayers();
        int position = 0;

        System.out.println("Nobility path bonuses:");
        for(List<Bonus> bonusList : bonusLists) {
            System.out.print("\tPosition" + position + ": ");
            if(bonusList.size()>0)
                bonusList.forEach(bonus -> System.out.print(bonus.toString() + "  "));
            else {
                System.out.print("no bonus");
            }
            position++;
            System.out.println();
        }

        showPlayerList(playerLists, "nobility(ies)");
    }

    private void showVictoryPath() {
        List<List<Player>> playerLists = CachedData.getInstance().getVictoryPath().getPlayers();
        showPlayerList(playerLists, "point(s)");
    }

    private void showPlayerList(List<List<Player>> playerLists, String spec) {
        int position = 0;
        System.out.println("Players: ");
        for(List<Player> playerList : playerLists) {
            if(playerList.size() != 0) {
                showPlayersInfo(playerList.iterator(), position, spec);
            }
            position++;
        }
        System.out.println("\n");
    }

    private void showPlayersInfo(Iterator<Player> playerIterator, int position, String spec) {
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            System.out.println("\t" + player.getNickname() + " with " + position + " " + spec);
        }
    }
}