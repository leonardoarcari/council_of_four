package core.gamemodel;

import java.io.Serializable;

/**
 * The enumeration is the set of names of the towns in the game.
 */
public enum TownName implements Serializable {
    // Values of the enumeration
    A("Arkon"),
    B("Burgen"),
    C("Castrum"),
    D("Dorful"),
    E("Esti"),
    F("Framek"),
    G("Graden"),
    H("Hellar"),
    I("Indur"),
    J("Juvelar"),
    K("Kultos"),
    L("Lyram"),
    M("Merkatim"),
    N("Naris"),
    O("Osium");

    // Attribute of the enumeration
    private final String townName;

    /**
     * @param townName is the value
     */
    TownName(final String townName) {
        this.townName = townName;
    }

    /**
     * @return the value of the object, meaning the name of a town
     */
    @Override
    public String toString(){
        return townName;
    }
}
