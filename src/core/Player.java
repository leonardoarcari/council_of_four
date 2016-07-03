package core;

import core.connection.Connection;
import core.gamemodel.*;
import core.gamemodel.modelinterface.PlayerInterface;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.*;

/**
 * A <code>Player</code> is a class representing a client playing to the game. Thus, it provides access to its game
 * items in addition to personal informations such as nickname, username and color. It also exposes the {@link Connection
 * Connection} the server uses to communicate with the client.
 */
public class Player implements Subject, Serializable, PlayerInterface {
    private String uniqueID;
    private String username;
    private String nickname;
    private String color;
    private transient Connection connection;

    // The hand
    private List<PermitCard> permitCards;
    private List<RegionCard> regionCards;
    private List<PoliticsCard> politicsCards;
    private List<RoyalCard> royalCards;
    private List<TownTypeCard> townTypeCards;
    private List<Servant> servants;

    private transient List<Observer> observers;

    /**
     * Initializes a <code>Player</code> given the <code>Connection</code> the server uses to communicate with the client.
     * Random color is generated and the sets of game items the player own are initialized to be empty
     * @param connection A reference to the <code>Connection</code> on the server to communicate with the client.
     */
    public Player(Connection connection) {
        this.connection = connection;

        uniqueID = generateID();
        username = "";
        nickname = "";
        color = generateColor().toString();
        permitCards = new ArrayList<>();
        regionCards = new ArrayList<>();
        politicsCards = new ArrayList<>();
        royalCards = new ArrayList<>();
        townTypeCards = new ArrayList<>();
        servants = new ArrayList<>();
        observers = new Vector<>();
    }

    private String generateID() {
        String id = "";
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            id += random.nextInt(10);
        }
        return id;
    }

    private Color generateColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        return Color.rgb(red, green, blue);
    }

    /**
     * Registers the new Player's nickname
     * @param nickname new nickname of the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyObservers();
    }

    /**
     * Registers the new Player's username
     * @param username new username of the player
     */
    public void setUsername(String username) {
        this.username = username;
        notifyObservers();
    }

    /**
     * @return Player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return Player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Removes <code>card</code> from player's hand
     * @param card PoliticsCard to remove
     */
    public void removePoliticsCard(PoliticsCard card) {
        politicsCards.remove(card);
        notifyObservers();
    }

    /**
     * Removes <code>card</code> from player's hand
     * @param card PermitCard to remove
     */
    public void removePermitCard(PermitCard card) {
        permitCards.remove(card);
        notifyObservers();
    }

    /**
     * Adds <code>permitCard</code> to player's hand
     * @param permitCard PermitCard to add
     */
    public void addPermitCard(PermitCard permitCard) {
        permitCards.add(permitCard);
        notifyObservers();
    }

    /**
     * Adds <code>card</code> to player's hand
     * @param card PoliticsCard to add
     */
    public void addPoliticsCard(PoliticsCard card) {
        politicsCards.add(card);
        notifyObservers();
    }

    /**
     * Adds <code>regionCard</code> to player's hand
     * @param regionCard RegionCard to add
     */
    public void addRegionCard(RegionCard regionCard) {
        regionCards.add(regionCard);
        notifyObservers();
    }

    /**
     * Adds <code>royalCard</code> to player's hand
     * @param royalCard RoyalCard to add
     */
    public void addRoyalCard(RoyalCard royalCard) {
        royalCards.add(royalCard);
        notifyObservers();
    }

    /**
     * Adds <code>townTypeCard</code> to player's hand
     * @param townTypeCard TownTypeCard to add
     */
    public void addTownTypeCard(TownTypeCard townTypeCard) {
        townTypeCards.add(townTypeCard);
        notifyObservers();
    }

    /**
     * Adds a set of Servant to player's pool
     * @param servant A list of servants to add
     */
    public void hireServants(List<Servant> servant) {
        servants.addAll(servant);
        notifyObservers();
    }

    /**
     * Removes a servant from the player's pool
     * @return The removed Servant
     */
    public Servant removeServant() {
        return servants.remove(0);
    }

    /**
     * @return An iterator of player's PermitCards
     */
    public Iterator<PermitCard> permitCardIterator() {
        return permitCards.iterator();
    }

    /**
     * @return An iterator of player's RegionCards
     */
    public Iterator<RegionCard> regionCardIterator() {
        return regionCards.iterator();
    }

    /**
     * @return An iterator of player's PoliticsCard
     */
    public Iterator<PoliticsCard> politicsCardIterator() {
        return politicsCards.iterator();
    }

    /**
     * @return An iterator of player's RoyalCards
     */
    public Iterator<RoyalCard> royalCardIterator() {
        return royalCards.iterator();
    }

    /**
     * @return An iterator of player's TownCards
     */
    public Iterator<TownTypeCard> townCardIterator() {
        return townTypeCards.iterator();
    }

    /**
     * @return The number of servants in player's pool
     */
    public int getServantsNumber() {
        return servants.size();
    }

    /**
     * @return The number of PermitCards in player's hand
     */
    public int getPermitCardsNumber() { return permitCards.size(); }

    /**
     * @return The number of PoliticsCards in player's hand
     */
    public int getPoliticsCardsNumber() { return politicsCards.size(); }

    /**
     * @return The number of RoyalCards in player's hand
     */
    public int getRoyalCardsNumber() { return royalCards.size(); }

    /**
     * @return The player's color
     */
    public Color getColor() {
        return Color.valueOf(color);
    }

    /**
     * Marks <code>permitCard</code> as burned that means that this player has already built an emporium in one of the
     * towns written on the input <code>permitCard</code>
     * @param permitCard PermitCard to mark as burned
     */
    public void burnPermitCard(PermitCard permitCard) {
        if (permitCards.contains(permitCard)) {
            permitCards.get(permitCards.indexOf(permitCard)).burn();
            notifyObservers();
        }
    }

    /**
     * @return A reference to <code>Connection</code> on the server to communicate with the client
     */
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return uniqueID.equals(player.uniqueID);

    }

    @Override
    public int hashCode() {
        return uniqueID.hashCode();
    }
}
