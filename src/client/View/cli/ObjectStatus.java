package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamemodel.Councilor;
import core.gamemodel.CouncilorsBalcony;
import core.gamemodel.Region;
import core.gamemodel.Town;

import java.util.Iterator;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Created by Matteo on 14/06/16.
 */
public class ObjectStatus implements CLIState {
    //TODO add context variable

    @Override
    public void showMenu() {
        System.out.println("Choose object to see its state:");
        System.out.println("1) Councilors pool");
        System.out.println("2) Balconies");
        System.out.println("3) Towns");
        System.out.println("4) Wealth path");
        System.out.println("5) Nobility path");
        System.out.println("6) Victory path");
        System.out.println("0) Back");
    }

    @Override
    public void readInput(String input) {
        int choiche = Integer.valueOf(input);
        switch(choiche) {
            case 1:
                showCouncilorPool();
                break;
            case 2:
                showBalconies();
                break;
            case 3:
                showTowns();
                break;
            case 4:
                //showWealthPath();
                break;
            case 5:
                //showNobilityPath();
                break;
            case 6:
                //showVictoryPath();
                break;
            case 0:
                //TODO: change context state
                break;
            default:
                throw new IllegalArgumentException();
        }
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
        printBalconiesStatus(CachedData.getInstance().getSeaRegion());
        printBalconiesStatus(CachedData.getInstance().getHillsRegion());
        printBalconiesStatus(CachedData.getInstance().getMountainsRegion());
    }

    private void printBalconiesStatus(Region region) {
        CouncilorsBalcony balcony = region.getRegionBalcony();
        balcony.toFormattedString();
    }

    private void showTowns() {
        List<Town> towns = singletonList((Town) CachedData.getInstance().getTowns().values());
        for(Town town : towns) {
            System.out.println("\tTown name: " + town.getTownName().name());
            System.out.println("\tTown type:" + town.getTownType().name());
            System.out.println("\tTown bonus: " + town.getTownBonus().toString());
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
}
