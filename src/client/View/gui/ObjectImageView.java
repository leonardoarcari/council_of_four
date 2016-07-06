package client.View.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A <code>ObjectImageView</code> is a <code>ImageView</code> with parameters already set for disposing on a anchor pane
 */
public class ObjectImageView extends ImageView {
    private double leftX;
    private double topY;
    private double width;

    /**
     * Initializes a ObjectImageView
     * @param image source image
     * @param leftX Normalized left x coordinate of this in case it's added to an anchor pane
     * @param topY Normalized top y coordinate of this in case it's added to an anchor pane
     * @param width Normalized width of this
     */
    public ObjectImageView(Image image, double leftX, double topY, double width) {
        super(image);
        this.leftX = leftX;
        this.topY = topY;
        this.width = width;
        this.setSmooth(true);
        this.setPreserveRatio(true);
        this.setCache(true);
    }

    /**
     * @return normalized left x coordinate of this
     */
    public double getLeftX() {
        return leftX;
    }

    /**
     * @return normalized top y coordinate of this
     */
    public double getTopY() {
        return topY;
    }

    /**
     * @return normalized width of this
     */
    public double getWidth() {
        return width;
    }
}
