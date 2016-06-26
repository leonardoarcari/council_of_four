package core.gamelogic.actions;

import java.io.Serializable;

/**
 * The class informs the clients who choose the GUI as the interface of
 * the URL of the image to load as the map of the game.
 */
public class LoadMapAction implements Serializable {
    // URL of the map image
    private final String mapName;

    /**
     * @param mapName is the URL of the image to load
     */
    public LoadMapAction(String mapName) {
        this.mapName = mapName;
    }

    /**
     * @return the URL of the map image
     */
    public String getMapName() {
        return mapName;
    }
}
