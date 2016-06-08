package core.gamemodel;

import java.io.Serializable;

/**
 * Created by Matteo on 18/05/16.
 */
public enum TownName implements Serializable {
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

    private final String townName;

    TownName(final String townName) {
        this.townName = townName;
    }

    @Override
    public String toString(){
        return townName;
    }
}
