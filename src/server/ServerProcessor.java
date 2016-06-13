package server;

import core.Player;
import core.connection.InfoProcessor;
import core.gamelogic.GraphsAlgorithms;
import core.gamelogic.actions.*;
import core.gamemodel.*;
import core.gamemodel.bonus.*;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Matteo on 20/05/2016.
 */
public class ServerProcessor implements InfoProcessor {
    private Game game;

    public ServerProcessor(Game game) {
        this.game = game;
    }

    @Override
    public synchronized void processInfo(Object info) {
        if (info instanceof NormalAction) {
            Player player = game.getPlayerInstance(((Action) info).getPlayer());
            try {
                if (game.hasMoreMainActions(player)) {
                    if (info.getClass().equals(CouncilorElectionAction.class)){
                        councilorElection((CouncilorElectionAction) info);
                    } else if(info.getClass().equals(BuyPermitCardAction.class)) {
                        buyPermitCardAction((BuyPermitCardAction) info);
                    } else if(info.getClass().equals(BuildEmpoPCAction.class)) {
                        buildEmpoWithPermit((BuildEmpoPCAction) info);
                    } else {
                        buildEmpoKingHelp((BuildEmpoKingAction) info);
                    }
                    game.popMainActionToken(player);
                }
            } catch (Game.NotYourTurnException e) {
                e.printStackTrace();
            }
        } else if(info instanceof MarketAction) {
            if (info.getClass().equals(ExposeSellablesAction.class)) {
                exposeInShowcase((ExposeSellablesAction) info);
            } else
                buyOnSaleItem((BuyObjectsAction) info);
        } else if(info instanceof FastAction) {
            Player player = game.getPlayerInstance(((Action) info).getPlayer());
            try {
                game.fastActionDone(player);
                if (info.getClass().equals(HireServantAction.class)) {
                    hireServantAction((HireServantAction) info);
                } else if (info.getClass().equals(ChangePermitsAction.class)) {
                    changePermitsAction((ChangePermitsAction) info);
                } else if (info.getClass().equals(FastCouncilorElectionAction.class)) {
                    fastCouncilorElection((FastCouncilorElectionAction) info);
                } else {
                    anotherMainAction((AnotherMainActionAction) info);
                }
            } catch (Game.NotYourTurnException e) {
                e.printStackTrace();
            }
        } else if (info instanceof SpecialAction) {
            if (info.getClass().equals(PickTownBonusAction.class)) {
                pickTownBonus((PickTownBonusAction) info);
            } else if (info.getClass().equals(TakePermitBonusAction.class)) {
                takePermitBonus((TakePermitBonusAction) info);
            } else if (info.getClass().equals(PermitNoPayAction.class)) {
                permitNoPay((PermitNoPayAction) info);
            } else if (info.getClass().equals(PlayerInfoAction.class)) {
                changePlayerInfo((PlayerInfoAction) info);
            } else if (info.getClass().equals(ChatAction.class)) {
                forwardChatMessage((ChatAction) info);
            }
        } else if (info instanceof SyncAction) {
            //TODO: Add Sync Action
        } else if(info.getClass().equals(EndTurnAction.class)) {
            endPlayerTurn((EndTurnAction) info);
        }
    }

    private void buyPermitCardAction(BuyPermitCardAction action) {
        // Add politics card to discarted deck
        Player player = game.getPlayerInstance(action.getPlayer());
        Iterator<PoliticsCard> cardIterator = action.discartedIterator();
        discardAndPay(cardIterator, player);

        // Draw permit card and redeem bonuses
        PermitCard card = game.getGameBoard().drawPermitCard(action.getRegionType(), action.getDrawnPermitCard());
        player.addPermitCard(card);

        retrievePermitBonus(card, player);
    }

    private void retrievePermitBonus(PermitCard permitCard, Player player) {
        Iterator<Bonus> iterator = permitCard.getBonusesIterator();
        while(iterator.hasNext()) {
            Bonus bonus = iterator.next();
            redeemBonus(bonus, player);
        }
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

        int coinsToPay = 0;

        if (cardsNo == 1) coinsToPay = 10;
        else if (cardsNo == 2) coinsToPay = 7;
        else if (cardsNo == 3) coinsToPay = 4;
        else if (cardsNo == 4) coinsToPay = 0;

        coinsToPay += rainbowCards;
        game.getGameBoard().moveWealthPath(player, -coinsToPay);
    }

    private void councilorElection(CouncilorElectionAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        game.getGameBoard().electCouncilor(action.getNewCouncilor(), action.getRegionType());
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
            //TODO: check for this code
            game.getGameBoard().moveNobilityPath(toPlayer, bonus.getValue()).stream().forEach(
                    otherBonus -> redeemBonus(otherBonus, toPlayer));
        } else if (bonusType.equals(VictoryPoint.class)) {
            game.getGameBoard().moveVictoryPath(toPlayer, bonus.getValue());
        } else if (bonusType.equals(RainbowStar.class)) {
            try {
                game.addMainActionToken(toPlayer);
            } catch (Game.NotYourTurnException e) {
                e.printStackTrace();
            }
        } else if (bonusType.equals(DrawPermitCard.class)) {
            game.drawnPermitCard(toPlayer);
        } else if (bonusType.equals(GetOwnPermitBonus.class)) {
            game.redeemPermitCardAgain(toPlayer);
        } else {
            game.redeemATownBonus(toPlayer);
        }
    }

    private void buildEmpoWithPermit(BuildEmpoPCAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        RegionType regionType = action.getRegionType();
        TownName townName = action.getSelectedTown();

        buildEmporium(player, regionType, townName);
        player.burnPermitCard(action.getUsedPermitCard());
    }

    private void buildEmpoKingHelp(BuildEmpoKingAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        RegionType regionType = action.getRegionType();
        TownName townName = action.getBuildingTown();
        Iterator<PoliticsCard> cardIterator = action.getSatCardIterator();

        discardAndPay(cardIterator, player);
        buildEmporium(player, regionType, townName);
        game.getGameBoard().moveWealthPath(player, -action.getSpentCoins());
        game.getGameBoard().moveKing(action.getStartingTown(), action.getBuildingTown());
    }

    private void buildEmporium(Player player, RegionType regionType, TownName townName) {
        game.getGameBoard().buildEmporium(player, regionType, townName);
        redeemLinkedTowns(player, townName);
        if(!townName.equals(TownName.J)) {
            redeemTown(player, townName);
            regionCompletion(player, regionType);
            typeCompletion(player, townName);
        }
    }

    private void redeemLinkedTowns(Player player, TownName townName) {
        List<Town> playerTowns = GraphsAlgorithms.townsWithEmporiumOf(game.getGameBoard().getTownsMap(), townName, player);
        for(Town myTown : playerTowns) {
            Bonus townBonus = myTown.getTownBonus();
            redeemBonus(townBonus, player);
        }
    }

    private void regionCompletion(Player player,RegionType regionType) {
        if(game.getGameBoard().checkRegionCompletion(player, regionType)) {
            Region region = game.getGameBoard().getRegionBy(regionType);
            RegionCard regionCard = region.drawRegionCard();
            player.addRegionCard(regionCard);
            redeemBonus(regionCard.getRegionBonus(),player);
            canGetRoyal(player);
        }
    }

    private void typeCompletion(Player player, TownName townName) {
        if(game.getGameBoard().checkTypeCompletion(player,townName)) {
            TownTypeCard townTypeCard = game.getGameBoard().acquireTownTypeCard(townName);
            player.addTownTypeCard(townTypeCard);
            redeemBonus(townTypeCard.getTypeBonus(),player);
            canGetRoyal(player);
        }
    }

    private void canGetRoyal(Player player) {
        if(game.getGameBoard().checkRoyalSize()) {
            RoyalCard royalCard = game.getGameBoard().popRoyalCard();
            player.addRoyalCard(royalCard);
            redeemBonus(royalCard.getRoyalBonus(),player);
        }
    }

    private void hireServantAction(HireServantAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        player.hireServants(game.getGameBoard().hireServants(1));
        game.getGameBoard().moveWealthPath(player, -3);
    }

    private void changePermitsAction(ChangePermitsAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        RegionType regionType = action.getRegionType();

        Servant servant = player.removeServant();
        game.getGameBoard().returnServant(servant);
        PermitCard leftCard = game.getGameBoard().drawPermitCard(regionType, Region.PermitPos.LEFT);
        PermitCard rightCard = game.getGameBoard().drawPermitCard(regionType, Region.PermitPos.RIGHT);
        Region thisRegion = game.getGameBoard().getRegionBy(regionType);
        if(leftCard != null) thisRegion.addPermitEndOfStack(leftCard);
        if(rightCard != null) thisRegion.addPermitEndOfStack(rightCard);
    }

    private void fastCouncilorElection(FastCouncilorElectionAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());

        Servant servant = player.removeServant();
        game.getGameBoard().returnServant(servant);
        game.getGameBoard().electCouncilor(action.getCouncilor(), action.getRegionType());
    }

    private void pickTownBonus(PickTownBonusAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());

        redeemTown(player, action.getTownName());
    }

    private void redeemTown(Player player, TownName townName) {
        Region myRegion = game.getGameBoard().regionFromTownName(townName);
        Town myTown = myRegion.getTownByName(townName);
        Bonus townBonus = myTown.getTownBonus();
        redeemBonus(townBonus, player);
    }

    private void takePermitBonus(TakePermitBonusAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());

        retrievePermitBonus(action.getMyPermitCard(), player);
    }

    private void permitNoPay(PermitNoPayAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());

        PermitCard card = game.getGameBoard().drawPermitCard(action.getRegionType(), action.getPosition());
        player.addPermitCard(card);
        retrievePermitBonus(card, player);
    }

    private void anotherMainAction(AnotherMainActionAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        try {
            game.addMainActionToken(player);
        } catch (Game.NotYourTurnException e) {
            e.printStackTrace();
        }
    }

    private void exposeInShowcase(ExposeSellablesAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        Showcase myShowcase = game.getGameBoard().getShowcase();
        myShowcase.addItems(action.getOnSaleItems(), player);
        try {
            game.sellableExposed(player);
        } catch (Game.NotYourTurnException e) {
            e.printStackTrace();
        }
    }

    private void buyOnSaleItem(BuyObjectsAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        Showcase myShowcase = game.getGameBoard().getShowcase();
        Iterator<OnSaleItem> iterator = action.itemsIterator();

        while(iterator.hasNext()) {
            OnSaleItem currentSaled = iterator.next();
            myShowcase.removeItem(currentSaled, player);
            game.getGameBoard().moveWealthPath(player, -currentSaled.getPrice());
        }

        try {
            game.endAuctionOf(player);
        } catch (Game.NotYourTurnException e) {
            e.printStackTrace();
        }
    }

    private void endPlayerTurn(EndTurnAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        try {
            game.endTurn(player);
        } catch (Game.NotYourTurnException e) {
            e.printStackTrace();
        }
    }

    private void changePlayerInfo(PlayerInfoAction action) {
        Player player = game.getPlayerInstance(action.getPlayer());
        player.setUsername(action.getUsername());
        player.setNickname(action.getNickname());
    }

    private void forwardChatMessage(ChatAction action) {
        game.playerIterator().forEachRemaining(player -> player.getConnection().sendInfo(action));
    }
}
