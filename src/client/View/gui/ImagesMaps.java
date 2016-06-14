package client.View.gui;

import core.gamemodel.PermitCard;
import core.gamemodel.RegionType;
import core.gamemodel.TownType;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leonardoarcari on 10/06/16.
 */
public class ImagesMaps {
    private volatile static ImagesMaps instance = null;
    private ClassLoader loader;

    private Map<String, Image> politics;
    private Map<PermitCard, Image> permits;
    private Map<String, Image> royals;
    private Map<RegionType, Image> regionCards;
    private Map<TownType, Image> townTypeCards;
    private Image servant;

    public static ImagesMaps getInstance() {
        if (instance == null) {
            synchronized (ImagesMaps.class) {
                if (instance == null) {
                    instance = new ImagesMaps();
                }
            }
        }
        return instance;
    }

    private ImagesMaps() {
        loader = this.getClass().getClassLoader();
        politics = new HashMap<>(7);
        permits = new HashMap<>(10);
        royals = new HashMap<>(5);
        regionCards = new HashMap<>(3);
        townTypeCards = new HashMap<>(4);

        loadPolitics();
        loadRoyals();
        loadRegionCards();
        loadTownTypeCards();
        servant = new Image(loader.getResourceAsStream("BonusImages/hireservant_1.png"));
    }

    private void loadPolitics() {
        politics.put("CYAN", new Image(loader.getResourceAsStream("bluePolitics.png")));
        politics.put("PINK", new Image(loader.getResourceAsStream("pinkPolitics.png")));
        politics.put("PURPLE", new Image(loader.getResourceAsStream("purplePolitics.png")));
        politics.put("ORANGE", new Image(loader.getResourceAsStream("orangePolitics.png")));
        politics.put("BLACK", new Image(loader.getResourceAsStream("blackPolitics.png")));
        politics.put("WHITE", new Image(loader.getResourceAsStream("whitePolitics.png")));
        politics.put("RAINBOW", new Image(loader.getResourceAsStream("rainbowPolitics.png")));
    }

    private void loadRoyals() {
        royals.put("first", new Image(loader.getResourceAsStream("fifthRoyal.png")));
        royals.put("second", new Image(loader.getResourceAsStream("fourthRoyal.png")));
        royals.put("third", new Image(loader.getResourceAsStream("thirdRoyal.png")));
        royals.put("fourth", new Image(loader.getResourceAsStream("secondRoyal.png")));
        royals.put("fifth", new Image(loader.getResourceAsStream("firstRoyal.png")));
    }

    private void loadRegionCards() {
        regionCards.put(RegionType.SEA, new Image(loader.getResourceAsStream("seaBonus.png")));
        regionCards.put(RegionType.HILLS, new Image(loader.getResourceAsStream("hillsBonus.png")));
        regionCards.put(RegionType.MOUNTAINS, new Image(loader.getResourceAsStream("mountainsBonus.png")));
    }

    private void loadTownTypeCards() {
        townTypeCards.put(TownType.IRON, new Image(loader.getResourceAsStream("ironBonus.png")));
        townTypeCards.put(TownType.BRONZE, new Image(loader.getResourceAsStream("bronzeBonus.png")));
        townTypeCards.put(TownType.SILVER, new Image(loader.getResourceAsStream("silverBonus.png")));
        townTypeCards.put(TownType.GOLD, new Image(loader.getResourceAsStream("goldBonus.png")));
    }

    public Image getPolitics(String color) {
        return politics.get(color);
    }

    public Image getRoyal(String position) {
        return royals.get(position);
    }

    public Image getRegionBonus(RegionType type) {
        return regionCards.get(type);
    }

    public Image getTownTypeBonus(TownType type) {
        return townTypeCards.get(type);
    }

    public boolean isPermitLoaded(PermitCard card) {
        return permits.containsKey(card);
    }

    public void putPermitImage(PermitCard card, Image image) {
        permits.put(card, image);
    }

    public Image getPermit(PermitCard card) {
        return permits.get(card);
    }

    public Image getServant() {
        return servant;
    }
}
