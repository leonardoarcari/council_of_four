package core;

import core.connection.Connection;
import core.gamemodel.*;
import server.Observer;
import server.Subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by Matteo on 19/05/16.
 */
public class Player implements Subject, Serializable {
    private String username;
    private String nickname;
    private DummyRef dummyRef;
    private transient Connection connection;

    /**
     * GameObjects references of the current game
     */
    private List<PermitCard> permitCards;
    private List<RegionCard> regionCards;
    private List<PoliticsCard> politicsCards; // The hand
    private List<RoyalCard> royalCards;
    private List<TownTypeCard> townTypeCards;
    private List<Servant> servants;

    private transient List<Observer> observers;

    public Player(Connection connection) {
        this.connection = connection;

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

    public void addDummy(DummyRef dummyRef) {
        this.dummyRef = dummyRef;
    }

    public DummyRef getDummyRef() {
        return dummyRef;
    }

    public void removePoliticsCard(PoliticsCard card) {
        politicsCards.remove(card);
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

    public void hireServants(List<Servant> servant) {
        servants.addAll(servant);
        //notifyObservers();
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
        if(permitCards.contains(permitCard)) {
            permitCard.burn();
            notifyObservers();
        }
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
    public boolean equals(Object object) {
        Player player = (Player) object;
        return this.dummyRef.getId() == player.dummyRef.getId();
    }
}
