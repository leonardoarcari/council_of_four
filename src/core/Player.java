package core;

import core.connection.Connection;
import core.gamemodel.*;
import core.gamemodel.modelinterface.PlayerInterface;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Matteo on 19/05/16.
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyObservers();
    }

    public void setUsername(String username) {
        this.username = username;
        notifyObservers();
    }

    public String getUniqueID() {
        return uniqueID;
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

    public int getServantsNumber() {
        return servants.size();
    }

    public int getPermitCardsNumber() { return permitCards.size(); }

    public int getPoliticsCardsNumber() { return politicsCards.size(); }

    public int getRoyalCardsNumber() { return royalCards.size(); }

    public Color getColor() {
        return Color.valueOf(color);
    }

    public void burnPermitCard(PermitCard permitCard) {
        if (permitCards.contains(permitCard)) {
            permitCards.get(permitCards.indexOf(permitCard)).burn();
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

        return uniqueID.equals(player.uniqueID);

    }

    @Override
    public int hashCode() {
        return uniqueID.hashCode();
    }
}
