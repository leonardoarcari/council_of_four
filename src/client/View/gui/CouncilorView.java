package client.View.gui;

import core.gamemodel.Councilor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A <code>CouncilorView</code> shows a model's {@link Councilor Councilor}
 */
public class CouncilorView extends ImageView {
    private Councilor councilor;

    /**
     * Initializes a CouncilorView with the given Councilor
     * @param councilor Game model's councilor to show
     */
    public CouncilorView(Councilor councilor) {
        this.councilor = councilor;
    }

    /**
     * Set the Image to show representing {@link #getCouncilor()}
     * @param image Image representing councilor passed during initialization
     */
    public void setViewImage(Image image) {
        super.setImage(image);
    }

    /**
     * @return A reference to the councilor object this represents
     */
    public Councilor getCouncilor() {
        return councilor;
    }
}
