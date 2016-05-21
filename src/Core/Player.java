package Core;

import Core.Connection.Connection;
import Core.GameModel.*;
import java.util.ArrayList;

/**
 * Created by Matteo on 19/05/16.
 */
public class Player {
    private String username;
    private String nickname;
    private transient Connection connection;

    /**
     * GameObjects references of the current game
     */
    private ArrayList<PermitCard> permitCards;
    private ArrayList<RegionCard> regionCards;
    private ArrayList<PoliticsCard> politicsCards; // The hand
    private ArrayList<RoyalCard> royalCards;
    private ArrayList<TownTypeCard>  townTypeCards;
    private ArrayList<Servant> servants;

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

    public ArrayList<PermitCard> getPermitCards() {
        return permitCards;
    }

    public ArrayList<RegionCard> getRegionCards() {
        return regionCards;
    }

    public ArrayList<PoliticsCard> getPoliticsCards() {
        return politicsCards;
    }

    public ArrayList<RoyalCard> getRoyalCards() {
        return royalCards;
    }

    public ArrayList<TownTypeCard> getTownTypeCards() {
        return townTypeCards;
    }

    public ArrayList<Servant> getServants() {
        return servants;
    }
}
