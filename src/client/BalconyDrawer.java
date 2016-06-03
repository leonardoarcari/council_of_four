package client;

import core.gamemodel.Councilor;
import core.gamemodel.CouncilorsBalcony;
import core.gamemodel.bonus.Bonus;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * Created by Matteo on 03/06/16.
 */
public class BalconyDrawer {
    public static Image drawBalcony(CouncilorsBalcony balcony) {
        ClassLoader loader = BalconyDrawer.class.getClassLoader();
        Canvas free = new Canvas(255, 140);
        GraphicsContext gc = free.getGraphicsContext2D();
        Image backImage = new Image(loader.getResourceAsStream("backbalcony.png"));
        gc.drawImage(backImage,0,0,255,140);

        Iterator<Councilor> councilorIterator = balcony.councilorsIterator();
        int index = 0;
        while(councilorIterator.hasNext()) {
            Councilor councilor = councilorIterator.next();
            Image councImage = loadCouncilorImage(councilor, loader);
            gc.drawImage(councImage,15+index*56.25,5,56.25,140);
            index++;
        }
        Image balcImage = new Image(loader.getResourceAsStream("balcony.png"));
        gc.drawImage(balcImage,0,0,255,140);

        WritableImage balc = new WritableImage(255,140);
        free.snapshot(null,balc);
        BufferedImage bi = SwingFXUtils.fromFXImage(balc, null);
        return SwingFXUtils.toFXImage(bi, null);
    }

    private static Image loadCouncilorImage(Councilor councilor, ClassLoader loader) {
        String counColor = councilor.getCouncilorColor().toString();
        counColor = counColor + "_councilor.png";
        counColor = counColor.toLowerCase();
        System.out.println(counColor);
        return new Image(loader.getResourceAsStream(counColor));
    }
}
