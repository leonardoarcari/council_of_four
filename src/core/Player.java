package core;

import core.connection.Connection;
import core.gamemodel.*;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Matteo on 19/05/16.
 */
public class Player implements Subject, Serializable {
    private String uniqueID;
    private String username;
    private String nickname;
    private transient Connection connection;

    // The hand
    private List<PermitCard> permitCards;
    private List<RegionCard> regionCards;
    private List<PoliticsCard> politicsCards;
    private List<RoyalCard> royalCards;
    private List<TownTypeCard> townTypeCards;
    private List<Servant> servants;

    private transient List<Observer> observers;

    public Player(Connection connection) {
        this.connection = connection;

        uniqueID = generateID();
        username = null;
        nickname = null;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public void removePoliticsCard(PoliticsCard card) {
        politicsCards.remove(card);
        notifyObservers();
    }

    public void removePermitCard(PermitCard card) {
        permitCards.remove(card);
        notifyObservers();
    }

    public void addPermitCard(PermitCard permitCard) {
        permitCards.add(permitCard);
        notifyObservers();
    }

    public void addPoliticsCard(PoliticsCard card) {
        politicsCards.add(card);
        notifyObservers();
    }

    public void addRegionCard(RegionCard regionCard) {
        regionCards.add(regionCard);
        notifyObservers();
    }

    public void addRoyalCard(RoyalCard royalCard) {
        royalCards.add(royalCard);
        notifyObservers();
    }

    public void addTownTypeCard(TownTypeCard townTypeCard) {
        townTypeCards.add(townTypeCard);
        notifyObservers();
    }

    public void hireServants(List<Servant> servant) {
        servants.addAll(servant);
        notifyObservers();
    }

    public Servant removeServant() {
        return servants.remove(0);
    }

    public Iterator<PermitCard> permitCardIterator() {
        return permitCards.iterator();
    }

    public Iterator<RegionCard> regionCardIterator() {
        return regionCards.iterator();
    }

    public Iterator<PoliticsCard> politicsCardIterator() {
        return politicsCards.iterator();
    }

    public Iterator<RoyalCard> royalCardIterator() {
        return royalCards.iterator();
    }

    public Iterator<TownTypeCard> townCardIterator() {
        return townTypeCards.iterator();
    }

    public int servantsNumber() {
        return servants.size();
    }

    public void burnPermitCard(PermitCard permitCard) {
        if (permitCards.contains(permitCard)) {
            permitCard.burn();
            notifyObservers();
        }
    }

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

        if (!uniqueID.equals(player.uniqueID)) return false;
        if (username != null ? !username.equals(player.username) : player.username != null) return false;
        return nickname != null ? nickname.equals(player.nickname) : player.nickname == null;

    }

    @Override
    public int hashCode() {
        int result = uniqueID.hashCode();
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        return result;
    }
}
