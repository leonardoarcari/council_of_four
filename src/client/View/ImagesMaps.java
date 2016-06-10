package client.View;

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

        loadPolitics();
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

    public Image getPolitics(String color) {
        return politics.get(color);
    }

}
