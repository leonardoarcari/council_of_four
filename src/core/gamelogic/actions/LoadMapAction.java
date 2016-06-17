package core.gamelogic.actions;

import java.io.Serializable;

/**
 * Created by Leonardo Arcari on 16/06/2016.
 */
public class LoadMapAction implements Serializable {
    private final String mapName;

    public LoadMapAction(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }
}
