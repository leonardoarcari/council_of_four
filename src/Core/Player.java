package Core;

import Core.Connection.Connection;
import Core.GameModel.*;
import Server.Observer;
import Server.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 19/05/16.
 */
public class Player implements Subject {
    private String username;
    private String nickname;
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

    public void removePoliticsCard(PoliticsCard card) {
        politicsCards.remove(card);
    }

    public List<PermitCard> getPermitCards() {
        return permitCards;
    }

    public List<RegionCard> getRegionCards() {
        return regionCards;
    }

    public List<PoliticsCard> getPoliticsCards() {
        return politicsCards;
    }

    public List<RoyalCard> getRoyalCards() {
        return royalCards;
    }

    public List<TownTypeCard> getTownTypeCards() {
        return townTypeCards;
    }

    public List<Servant> getServants() {
        return servants;
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
}
