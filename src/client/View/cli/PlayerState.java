package client.View.cli;

import client.CachedData;
import core.Player;
import core.gamemodel.*;

import java.util.Iterator;

/**
 * This class acts as an information centre of the player. Here he can choose
 * his own info's such as Username and Nickname, politics cards, coins and so on.
 */
public class PlayerState implements CLIState {
    // Reference to the context
    private CLI cli;

    /**
     * The constructor sets the context
     *
     * @param cli is the context owning all the possible game states; it is needed to
     *            change the current state from this class
     */
    public PlayerState(CLI cli) {
        this.cli = cli;
    }

    /**
     * @see CLIState
     */
    @Override
    public void showMenu() {
        System.out.println("Choose object to see its state:");
        System.out.println("1) Username and Nickname");
        System.out.println("2) My politics cards");
        System.out.println("3) Coins and servants number");
        System.out.println("4) My permit cards");
        System.out.println("5) My bonus cards");
        System.out.println("0) Back");
    }

    /**
     * @param input is the choice of the player
     * @see CLIState
     * @throws IllegalArgumentException
     */
    @Override
    public void readInput(String input) throws IllegalArgumentException {
        int choice;
        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        switch(choice) {
            case 1:
                showUserNick();
                break;
            case 2:
                showPoliticCards();
                break;
            case 3:
                showCoinsAndServants();
                break;
            case 4:
                showPermitCards();
                break;
            case 5:
                showBonusCards();
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

    private void showUserNick() {
        Player me = (Player)CachedData.getInstance().getMe();
        System.out.println("My infos:");
        System.out.println("\tUsername: " + me.getUsername() + "\n\tNickname: " + me.getNickname() + "\n");
    }

    private void showPoliticCards() {
        Player me = (Player)CachedData.getInstance().getMe();
        System.out.println("My politics cards: ");

        System.out.print("\t[");
        Iterator<PoliticsCard> politicsCardIterator = me.politicsCardIterator();
        while(politicsCardIterator.hasNext()) {
            System.out.print(politicsCardIterator.next().toString());
            if(politicsCardIterator.hasNext()) System.out.print(" ");
        }
        System.out.println("]\n");
    }

    private void showCoinsAndServants() {
        Player me = (Player)CachedData.getInstance().getMe();
        System.out.println("My numbers: ");
        System.out.println("\tCoin(s): " + CachedData.getInstance().getWealthPath().getPlayerPosition(me));
        System.out.println("\tServant(s): " + me.getServantsNumber() + "\n");
    }

    private void showPermitCards() {
        Player me = (Player)CachedData.getInstance().getMe();
        System.out.println("My permit cards: ");

        Iterator<PermitCard> permitCardIterator = me.permitCardIterator();
        if(permitCardIterator.hasNext()) System.out.println("[]\n");
        while(permitCardIterator.hasNext()) {
            System.out.println(permitCardIterator.next().toString()+"\n");
        }
        System.out.println();
    }

    private void showBonusCards() {
        Player me = (Player)CachedData.getInstance().getMe();
        System.out.println("My bonus cards:");

        System.out.print("\tTown bonus cards: [");
        Iterator<TownTypeCard> townTypeCardIterator = me.townCardIterator();
        printCardInfo(townTypeCardIterator, " -- ");
        System.out.println("]");

        System.out.print("\tRoyal bonus cards: [");
        Iterator<RoyalCard> royalCardIterator = me.royalCardIterator();
        printCardInfo(royalCardIterator, " -- ");
        System.out.println("]");

        System.out.print("\tRegion bonus cards: [");
        Iterator<RegionCard> regionCardIterator = me.regionCardIterator();
        printCardInfo(regionCardIterator, " -- ");
        System.out.println("]\n");
    }

    private void printCardInfo(Iterator<?> specIterator, String separator) {
        while(specIterator.hasNext()) {
            System.out.print(specIterator.next().toString());
            if(specIterator.hasNext()) System.out.print(separator);
        }
    }
}
