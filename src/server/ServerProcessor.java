package server;

import core.connection.InfoProcessor;
import core.gamelogic.GraphsAlgorithms;
import core.gamelogic.actions.*;
import core.gamemodel.*;
import core.gamemodel.bonus.*;

import java.util.Iterator;
import java.util.List;

import core.gamemodel.Councilor;
import core.gamemodel.PermitCard;
import core.gamemodel.RegionType;
import core.gamemodel.Town;

import core.Player;

/**
 * Created by Matteo on 20/05/2016.
 */
public class ServerProcessor implements InfoProcessor {
    private Game game;

    public ServerProcessor(Game game) {
        this.game = game;
    }

    @Override
    public void processInfo(Object info) {

        if (info instanceof NormalAction) {
            //TODO: mark normal action done
            if(info.getClass().equals(CouncilorElectionAction.class)){
                councilorElection((CouncilorElectionAction) info);
            } else if(info.getClass().equals(BuyPermitCardAction.class)) {
                buyPermitCardAction((BuyPermitCardAction) info);
            } else if(info.getClass().equals(BuildEmpoPCAction.class)) {
                buildEmpoWithPermit((BuildEmpoPCAction) info);
            } else {
                buildEmpoKingHelp((BuildEmpoKingAction) info);
            }
        } else if(info instanceof MarketAction) {
            //TODO: Add Market actions
        } else if(info instanceof FastAction) {
            if(info.getClass().equals(HireServantAction.class)) {
                hireServantAction((HireServantAction) info);
            } else if(info.getClass().equals(ChangePermitsAction.class)) {
                changePermitsAction((ChangePermitsAction) info);
            } else if(info.getClass().equals(FastCouncilorElectionAction.class)) {
                fastCouncilorElection((FastCouncilorElectionAction) info);
            } else {
                //TODO: inform client of new normal action
            }
        } else if (info instanceof SyncAction) {
            //TODO: Add Sync Action
        }
    }

    private void buyPermitCardAction(BuyPermitCardAction action) {
        // Add politics card to discarted deck
        Iterator<PoliticsCard> cardIterator = action.discartedIterator();
        discardAndPay(cardIterator, action.getPlayer());

        // Draw permit card and redeem bonuses
        PermitCard card = game.getGameBoard().drawPermitCard(action.getRegionType(), action.getDrawnPermitCard());
        action.getPlayer().addPermitCard(card);
        card.getBonuses().forEach(bonus -> redeemBonus(bonus, action.getPlayer()));
    }

    private void discardAndPay(Iterator<PoliticsCard> cardIterator, Player player) {
        int rainbowCards = 0;
        int cardsNo = 0;

        while (cardIterator.hasNext()) {
            PoliticsCard card = cardIterator.next();
            game.getGameBoard().discardCard(card);
            player.removePoliticsCard(card);
            if (card.getCardColor() == CouncilColor.RAINBOW) rainbowCards++;
            cardsNo++;
        }

        // Pay for discarted cards
        int coinsToPay = 0;

        if (cardsNo == 1) coinsToPay = 10;
        else if (cardsNo == 2) coinsToPay = 7;
        else if (cardsNo == 3) coinsToPay = 4;
        else if (cardsNo == 4) coinsToPay = 0;

        coinsToPay += rainbowCards;
        game.getGameBoard().moveWealthPath(player, -coinsToPay);
    }

    private void councilorElection(CouncilorElectionAction action) {
        Player player = action.getPlayer();
        Councilor councilor = action.getNewCouncilor();
        RegionType regionType = action.getRegionType();
        game.getGameBoard().electCouncilor(councilor, regionType);
        game.getGameBoard().moveWealthPath(player, 4);
    }

    private void redeemBonus(Bonus bonus, Player toPlayer) {
        Class<?> bonusType = bonus.getClass();
        if (bonusType.equals(Coin.class)) {
            game.getGameBoard().moveWealthPath(toPlayer, bonus.getValue());
        } else if (bonusType.equals(DrawPoliticsCard.class)) {
            for (int i = 0; i < bonus.getValue(); i++) {
                PoliticsCard card = game.getGameBoard().drawPoliticsCard();
                toPlayer.addPoliticsCard(card);
            }
        } else if (bonusType.equals(HireServant.class)) {
            toPlayer.hireServants((game.getGameBoard().hireServants(bonus.getValue())));
        } else if (bonusType.equals(NobilityPoint.class)) {
            game.getGameBoard().moveNobilityPath(toPlayer, bonus.getValue()).stream().forEach(
                    otherBonus -> redeemBonus(otherBonus, toPlayer));
        } else if (bonusType.equals(VictoryPoint.class)) {
            game.getGameBoard().moveVictoryPath(toPlayer, bonus.getValue());
        } else if (bonusType.equals(RainbowStar.class)) {
            //TODO: un-mark normal action and send to client new action
        } else if (bonusType.equals(DrawPermitCard.class)) {
            //TODO: send DRAW_PERMIT_BONUS action to player
        } else if (bonusType.equals(GetOwnPermitBonus.class)) {
            //TODO: send PICK_PERMIT_AGAIN
        } else {
            //TODO: send PICK_TOWN_BONUS
        }
    }

    private void buildEmpoWithPermit(BuildEmpoPCAction action) {
        Player player = action.getPlayer();
        PermitCard permitCard = action.getUsedPermitCard();
        TownName townName = action.getSelectedTown();
        game.getGameBoard().buildEmporium(player, townName);
        List<Town> playerTowns = GraphsAlgorithms.townsWithEmporiumOf(player,townName,game.getGameBoard().getTownsMap());
        for(Town myTown : playerTowns) {
            Iterator<Bonus> bonusIterator = myTown.bonusIterator();
            while(bonusIterator.hasNext()) {
                redeemBonus(bonusIterator.next(),player);
            }
        }
        game.getGameBoard().checkCompletion(player, townName);
        player.burnPermitCard(permitCard);
    }

    private void buildEmpoKingHelp(BuildEmpoKingAction action) {
        Iterator<PoliticsCard> cardIterator = action.getSatCardIterator();
        discardAndPay(cardIterator, action.getPlayer());
        TownName townName = action.getBuildingTown();
        game.getGameBoard().buildEmporium(action.getPlayer(), townName);
        List<Town> playerTowns = GraphsAlgorithms.townsWithEmporiumOf(action.getPlayer(),townName,game.getGameBoard().getTownsMap());
        for(Town myTown : playerTowns) {
            Iterator<Bonus> bonusIterator = myTown.bonusIterator();
            while(bonusIterator.hasNext()) {
                redeemBonus(bonusIterator.next(),action.getPlayer());
            }
        }
        game.getGameBoard().moveKing(action.getStartingTown(), action.getBuildingTown());
    }

    private void hireServantAction(HireServantAction action) {
        System.out.println("ricevuto");
        action.getPlayer().hireServants(game.getGameBoard().hireServants(1));
        game.getGameBoard().moveWealthPath(action.getPlayer(), -3);
    }

    private void changePermitsAction(ChangePermitsAction action) {
        Servant servant = action.getPlayer().removeServant();
        game.getGameBoard().returnServant(servant);
        PermitCard leftCard = game.getGameBoard().drawPermitCard(action.getRegionType(), Region.PermitPos.LEFT);
        PermitCard rightCard = game.getGameBoard().drawPermitCard(action.getRegionType(), Region.PermitPos.RIGHT);
        Region thisRegion = game.getGameBoard().getRegionBy(action.getRegionType());
        thisRegion.addPermitEndOfStack(leftCard);
        thisRegion.addPermitEndOfStack(rightCard);
    }

    private void fastCouncilorElection(FastCouncilorElectionAction action) {
        Servant servant = action.getPlayer().removeServant();
        game.getGameBoard().returnServant(servant);
        game.getGameBoard().electCouncilor(action.getCouncilor(), action.getRegionType());
    }
}
