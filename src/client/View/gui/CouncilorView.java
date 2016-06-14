package client.View.gui;

import core.gamemodel.Councilor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Matteo on 05/06/16.
 */
public class CouncilorView extends ImageView {
    private Councilor councilor;

    public CouncilorView(Councilor councilor) {
        this.councilor = councilor;
    }

    public void setViewImage(Image image) {
        super.setImage(image);
    }

    public Councilor getCouncilor() {
        return councilor;
    }

    public void setWidth(Double value) {
        super.setFitWidth(value);
    }
}
