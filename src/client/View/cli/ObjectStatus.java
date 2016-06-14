package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamemodel.*;
import core.gamemodel.bonus.Bonus;

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
        System.out.println("3) Regions");
        System.out.println("4) Towns");
        System.out.println("5) Wealth path");
        System.out.println("7) Nobility path");
        System.out.println("7) Victory path");
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
        ((CouncilorsBalcony) CachedData.getInstance().getBalcony(RegionType.SEA)).toFormattedString();
        ((CouncilorsBalcony) CachedData.getInstance().getBalcony(RegionType.HILLS)).toFormattedString();
        ((CouncilorsBalcony) CachedData.getInstance().getBalcony(RegionType.MOUNTAINS)).toFormattedString();
        ((CouncilorsBalcony) CachedData.getInstance().getBalcony(RegionType.KINGBOARD)).toFormattedString();
    }

    private void showRegions() {
        printRegionStatus(CachedData.getInstance().getSeaRegion());
        printRegionStatus(CachedData.getInstance().getHillsRegion());
        printRegionStatus(CachedData.getInstance().getMountainsRegion());
    }

    private void printRegionStatus(Region region) {
        System.out.println(region.getRegionType().name() + " region");
        System.out.println((!region.isRegionCardTaken()) ?
                ("Region card: "+ region.getRegionCard().getRegionBonus().toString()) : "Region card already taken!");
        System.out.println((region.getLeftPermitCard() == null) ?
                "No more left permit card" : ("Left permit card: "+region.getLeftPermitCard().toString()));
        System.out.println((region.getRightPermitCard() == null) ?
                "No more right permit card" : ("Right permit card: "+region.getRightPermitCard().toString()));
    }

    private void showTowns() {
        List<Town> towns = singletonList((Town) CachedData.getInstance().getTowns().values());
        for(Town town : towns) {
            System.out.println(town.getTownName().name().toUpperCase());
            System.out.println("\tType:" + town.getTownType().name());
            System.out.println("\tBonus: " + town.getTownBonus().toString());
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