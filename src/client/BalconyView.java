package client;

import core.gamemodel.Councilor;
import core.gamemodel.CouncilorsBalcony;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matteo on 05/06/16.
 */
public class BalconyView extends ObjectImageView {
    private ObservableList<Councilor> councilors;
    private List<Image> councilorImages;

    public BalconyView(Image image, double leftX, double topY, double width) {
        super(image, leftX, topY, width);
        councilorImages = new ArrayList<>();
        councilors = FXCollections.observableArrayList();
        setUpCouncilors();
    }

    private void setCouncilors(List<Councilor> councilors) {
        this.councilors.clear();
        this.councilors.addAll(councilors);
    }

    public void setBalcony(CouncilorsBalcony balcony) {
        List<Councilor> councilors = new ArrayList<>();
        Iterator<Councilor> councilorIterator = balcony.councilorsIterator();
        while(councilorIterator.hasNext()) {
            councilors.add(councilorIterator.next());
        }
        setCouncilors(councilors);
    }

    private void setUpCouncilors() {
        councilors.addListener((ListChangeListener<Councilor>) c -> {
            ClassLoader loader = BalconyDrawer.class.getClassLoader();
            while(c.next()) {
                if(c.wasAdded()) {
                    councilorImages.clear();
                    c.getList().forEach(o -> councilorImages.add(loadCouncilorImage(o, loader)));
                    this.setImage(drawBalcony(councilorImages, loader));
                }
            }
        });
    }

    private static Image drawBalcony(List<Image> councilorImages, ClassLoader loader) {
        Canvas free = new Canvas(255, 140);
        GraphicsContext gc = free.getGraphicsContext2D();
        Image backImage = new Image(loader.getResourceAsStream("backbalcony.png"));
        gc.drawImage(backImage,0,0,255,140);

        int index = 0;
        for(Image image : councilorImages) {
            gc.drawImage(image,15+index*56.25,5,56.25,140);
            index++;
        }

        Image balcImage = new Image(loader.getResourceAsStream("balcony.png"));
        gc.drawImage(balcImage,0,0,255,140);

        WritableImage balc = new WritableImage(255,140);
        free.snapshot(null,balc);
        BufferedImage bi = SwingFXUtils.fromFXImage(balc, null);
        return SwingFXUtils.toFXImage(bi, null);
    }

    private Image loadCouncilorImage(Councilor councilor, ClassLoader loader) {
        String counColor = councilor.getCouncilorColor().toString();
        counColor = counColor + "_councilor.png";
        counColor = counColor.toLowerCase();
        return new Image(loader.getResourceAsStream(counColor));
    }
}