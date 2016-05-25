package server;

import core.connection.InfoProcessor;
import core.gamelogic.actions.*;
import core.gamemodel.*;
import core.gamemodel.bonus.*;

import java.util.Iterator;

import core.gamemodel.Councilor;
import core.gamemodel.PermitCard;
import core.gamemodel.RegionType;
import core.gamemodel.Town;

import core.Player;

/**
 * Created by Leonardo Arcari on 20/05/2016.
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
            } else if (info.getClass().equals(BuyPermitCardAction.class)) {
                buyPermitCardAction((BuyPermitCardAction) info);
            } else if(info.getClass().equals(BuildEmpoPCAction.class)) {
                buildEmpoWithPermit((BuildEmpoPCAction) info);
            } else {
                buildEmpoKingHelp((BuildEmpoKingAction) info);
            }
        } else if (info instanceof MarketAction) {
            //TODO: Add Market actions
        } else if (info instanceof FastAction) {
            //TODO: Add Fast actions
        } else if (info instanceof SyncAction) {
            //TODO: Add Sync Action
        }
    }

    private void buyPermitCardAction(BuyPermitCardAction action) {
        // Add politics card to discarted deck
        int rainbowCards = 0;
        int cardsNo = 0;
        Iterator<PoliticsCard> cardIterator = action.discartedIterator();
        while (cardIterator.hasNext()) {
            PoliticsCard card = cardIterator.next();
            game.getGameBoard().discardCard(card);
            action.getPlayer().removePoliticsCard(card);
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
        game.getGameBoard().moveWealthPath(action.getPlayer(), -coinsToPay);

        // Draw permit card and redeem bonuses
        PermitCard card = game.getGameBoard().drawPermitCard(action.getRegionType(), action.getDrawnPermitCard());
        action.getPlayer().addPermitCard(card);
        card.getBonuses().forEach(bonus -> redeemBonus(bonus, action.getPlayer()));
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
        //TODO: research algorithm that calls redeemBonus
        player.burnPermitCard(permitCard);
    }

    private void buildEmpoKingHelp(BuildEmpoKingAction action) {

    }
}
