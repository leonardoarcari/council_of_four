package client;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Leonardo Arcari on 01/06/2016.
 */
public class ObjectImageView extends ImageView {
    private double leftX;
    private double topY;
    private double width;
    private Image image;

    public ObjectImageView(Image image, double leftX, double topY, double width) {
        super(image);
        this.leftX = leftX;
        this.topY = topY;
        this.width = width;
        this.setSmooth(true);
        this.setPreserveRatio(true);
        this.setCache(true);
    }

    public double getLeftX() {
        return leftX;
    }

    public double getTopY() {
        return topY;
    }

    public double getWidth() {
        return width;
    }
}
