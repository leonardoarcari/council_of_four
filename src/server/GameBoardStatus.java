package server;

import core.Player;
import core.gamemodel.*;
import core.gamemodel.bonus.Bonus;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by Matteo on 26/05/16.
 */
public class GameBoardStatus {
    public static void printStatus(GameBoard gameBoard) {

        for(RegionType type : RegionType.values()) {
            if(type.equals(RegionType.KINGBOARD)) break;
            List<Town> towns = new Vector<>();
            Iterator<Town> iterator = gameBoard.getRegionBy(type).townIterator();
            System.out.println(type.toString());
            while(iterator.hasNext()){
                towns.add(iterator.next());
            }
            for(Town town : towns) {
                System.out.print(town.getTownName().toString() + " has " + " || ");
            }
            System.out.println();
            for(PermitCard permitCard : gameBoard.getRegionBy(type).getRegionPermitCards()) {
                /*for(Bonus bonus : permitCard.getBonuses()) {
                    System.out.print(bonus.getClass().getName().substring(bonus.getClass().getName().lastIndexOf(".")+1) + " " + bonus.getValue() +  " ");
                }*/
                System.out.print("\t\t\t");
                for(TownName name : permitCard.getCityPermits()) {
                    System.out.print(name + "/");
                }
                System.out.println();
            }
            System.out.println();
        }
        int pos = 0;
        System.out.println("NOBILITY PATH");
        for(List<Bonus> bonusList: gameBoard.getNobilityPath().getBonusPath()) {
            System.out.print("Pos " + pos + " ");
            pos++;
            for (Bonus bonus : bonusList) {
                System.out.print(bonus.getClass().getName().substring(bonus.getClass().getName().lastIndexOf(".") + 1) + " " + bonus.getValue() + " ");
            }
            System.out.println();
        }
        pos = 0;
        for(List<Player> playerList: gameBoard.getNobilityPath().getPlayers()) {
            System.out.println("Pos " + pos + " size " + playerList.size());
            pos++;
        }
        pos = 0;
        System.out.println("\nWEALTH PATH");
        for(List<Player> playerList: gameBoard.getWealthPath().getPlayers()) {
            System.out.println("Wealth " + pos + " size " + playerList.size());
            pos++;
        }
    }
}
