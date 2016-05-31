package server;

import core.Player;
import core.Subject;
import core.connection.InfoProcessor;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.BuildEmpoPCAction;
import core.gamelogic.actions.BuyPermitCardAction;
import core.gamelogic.actions.CouncilorElectionAction;
import core.gamemodel.*;
import org.junit.Before;
import org.junit.Test;
import server.serverconnection.ServerConnection;
import server.serverconnection.ServerSocketConnection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Matteo on 27/05/16.
 */
public class ServerProcessorTest {
    ServerProcessor serverProcessor;
    Game game;
    Player pl00;
    Player pl01;

    Action electCouncilor;
    Action buyPermitCard;
    Councilor councilor;
    CouncilorsBalcony balcony;
    Iterator<Councilor> councilorIterator;
    List<PoliticsCard> fakeHand;


    @Before
    public void setUp() throws Exception {
        pl00 = new Player(new FakeConnection());
        pl01 = new Player(new FakeConnection());
        WaitingHall.getInstance().addPlayer(pl00);
        WaitingHall.getInstance().addPlayer(pl01);
        game = new Game();
        game.run();
        serverProcessor = new ServerProcessor(game);
        councilor = game.getGameBoard().councilorIterator().next();
        electCouncilor = new CouncilorElectionAction(pl00,councilor, RegionType.SEA);

        balcony = game.getGameBoard().getRegionBy(RegionType.SEA).getRegionBalcony();
        councilorIterator = balcony.councilorsIterator();
        fakeHand = new ArrayList<>();
        fakeHand.add(new PoliticsCard(councilorIterator.next().getCouncilorColor()));
        fakeHand.add(new PoliticsCard(CouncilColor.RAINBOW));
        buyPermitCard = new BuyPermitCardAction(pl00, fakeHand, RegionType.SEA, Region.PermitPos.LEFT);
    }

    @Test public void electCouncilorAction() {
        int currentPosition = game.getGameBoard().getWealthPath().getPlayerPosition(pl00);
        serverProcessor.processInfo(electCouncilor);
        balcony.addCouncilor(councilor);
        assertTrue(game.getGameBoard().getRegionBy(RegionType.SEA).getRegionBalcony().equals(balcony));
        assertTrue(game.getGameBoard().getWealthPath().getPlayerPosition(pl00) == currentPosition+4);
    }

    @Test public void buyPermitCardAction() {
        PermitCard permitCard = game.getGameBoard().getRegionBy(RegionType.SEA).peekPermitCard(Region.PermitPos.LEFT);
        serverProcessor.processInfo(buyPermitCard);
        Iterator<PermitCard> permitCardIterator = pl00.permitCardIterator();
        Iterator<PoliticsCard> politicsCardIterator = pl00.politicsCardIterator();
        List<PermitCard> playerPermitCard = new ArrayList<>();
        List<PoliticsCard> politicsCards = new ArrayList<>();
        while(permitCardIterator.hasNext())
            playerPermitCard.add(permitCardIterator.next());
        while(politicsCardIterator.hasNext())
            politicsCards.add(politicsCardIterator.next());
        assertTrue(playerPermitCard.contains(permitCard));
        assertFalse(politicsCards.containsAll(fakeHand));
    }

    @Test public void buildEmpoPC() {
        PermitCard permitCard = game.getGameBoard().getRegionBy(RegionType.SEA).drawPermitCard(Region.PermitPos.LEFT);
        pl00.addPermitCard(permitCard);
        TownName townName = permitCard.getCityPermits().get(0);
        Action buildEmpoPC = new BuildEmpoPCAction(pl00, RegionType.SEA, townName,permitCard);
        serverProcessor.processInfo(buildEmpoPC);
        List<PermitCard> playerPermitCard = new ArrayList<>();
        Iterator<PermitCard> permitCardIterator = pl00.permitCardIterator();
        while(permitCardIterator.hasNext())
            playerPermitCard.add(permitCardIterator.next());
        assertTrue(game.getGameBoard().getRegionBy(RegionType.SEA).getTownByName(townName).hasEmporium(pl00));
        assertTrue(playerPermitCard.get(playerPermitCard.indexOf(permitCard)).isBurned());

    }

    private class FakeConnection implements ServerConnection {

        @Override
        public void setPlayer(Player player) {

        }

        @Override
        public Player getPlayer() {
            return null;
        }

        @Override
        public void update(Subject subject) {

        }

        @Override
        public void setInfoProcessor(InfoProcessor processor) {

        }

        @Override
        public void sendInfo(Object info) {

        }
    }
}