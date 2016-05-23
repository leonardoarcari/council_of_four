package Server;

import Core.Connection.InfoProcessor;
import Core.GameLogic.Actions.*;
import Core.GameModel.*;
import Core.GameModel.Bonus.*;

import java.util.Iterator;

import Core.GameModel.Councilor;
import Core.GameModel.PermitCard;
import Core.GameModel.RegionType;
import Core.GameModel.Town;

import Core.Player;

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
            if(info.getClass().equals(CouncilorElectionAction.class)){
                councilorElection((CouncilorElectionAction) info);
            } else if (info.getClass().equals(BuyPermitCardAction.class)) {
                buyPermitCardAction((BuyPermitCardAction) info);
            } else if(info.getClass().equals(BuildEmpoPCAction.class)) {
                buildEmpoWithPermit((BuildEmpoPCAction) info);
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
        Iterator<PoliticsCard> cardIterator = action.discartedIterator();
        while (cardIterator.hasNext()) {
            PoliticsCard card = cardIterator.next();
            game.getGameBoard().discardCard(cardIterator.next());
            action.getPlayer().removePoliticsCard(card);
        }

        // Pay for discarted cards
        //TODO: Once we have a wealth path

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
            for (int i = 0; i < bonus.getValue(); i++) {
                Servant servant = game.getGameBoard().hireServant();
                toPlayer.hireServant(servant);
            }
        } else if (bonusType.equals(NobilityPoint.class)) {
            game.getGameBoard().moveNobilityPath(toPlayer, bonus.getValue()).stream().forEach(
                    otherBonus -> redeemBonus(otherBonus, toPlayer));
        } else if (bonusType.equals(RainbowStar.class)) {
            //TODO: New main action for toPlayer
        } else if (bonusType.equals(VictoryPoint.class)) {
            //TODO: Move player in victory path
        }
    }

    private void buildEmpoWithPermit(BuildEmpoPCAction action) {
        Player player = action.getPlayer();
        PermitCard permitCard = action.getUsedPermitCard();
        Town town = action.getSelectedTown();
        game.getGameBoard().updateTown(player, town);
        player.burnPermitCard(permitCard);
    }
}
