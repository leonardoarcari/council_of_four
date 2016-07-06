package client.View.gui;

import core.gamemodel.PermitCard;
import core.gamemodel.bonus.Bonus;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import org.controlsfx.control.PopOver;

import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * A <code>PermitCardView</code> is a <code>ObjectImageView</code> showing the state of model's
 * {@link PermitCard PermitCard}, displaying bonuses and names of the towns you can build an emporium with the shown card
 */
public class PermitCardView extends ObjectImageView {
    private ImageView myView;
    private PermitCard permitCard;
    private PopOver myPopover;

    /**
     * Initializes a PermitCardView
     * @param image default background image
     * @param leftX Normalized left x coordinate of this in case it's added to an anchor pane
     * @param topY Normalized top y coordinate of this in case it's added to an anchor pane
     * @param width Normalized width of this
     */
    public PermitCardView(Image image, double leftX, double topY, double width) {
        super(image, leftX, topY, width);
        myView = new ImageView();
        setFirstPopOver();
    }

    private void setFirstPopOver() {
        myView.setFitHeight(150);
        myView.setPreserveRatio(true);
        myPopover = new PopOver();
        myPopover.setArrowSize(0.0);
        myView.setImage(this.getImage());
        myPopover.setContentNode(myView);
    }

    /**
     * Registers the <code>PermitCard</code> to show in this
     * @param permitCard new PermitCard model's object to display
     */
    public void setPermitCard(PermitCard permitCard) {
        this.permitCard = permitCard;
        if(permitCard == null) {
            myView.setImage(null);
            return;
        }

        if (!ImagesMaps.getInstance().isPermitLoaded(permitCard)) {
            ImagesMaps.getInstance().putPermitImage(permitCard, permitImage(permitCard));
        }
        this.setImage(ImagesMaps.getInstance().getPermit(permitCard));
        myView.setImage(ImagesMaps.getInstance().getPermit(permitCard));
    }

    private Image permitImage(PermitCard permitCard) {
        ClassLoader loader = this.getClass().getClassLoader();
        Canvas canvas = new Canvas(232, 260);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image permitModel = new Image(loader.getResourceAsStream("permitCard.png"));
        gc.drawImage(permitModel, 0, 0, 232, 260);

        int bonusNumber = bonusSize(permitCard);
        int horizontalSpacing = calculateHorizontalSpacing(bonusNumber);

        for(int i = 0; i < bonusNumber; i++) {
            Image bonus = imageLoader(permitCard.getBonuses().get(i), loader);

            gc.drawImage(bonus, horizontalSpacing + 65 * i, 160, 60, 60);
        }

        int citiesNumber = permitCard.getCityPermits().size();
        gc.setFont(new Font("Verdana",60.0));

        horizontalSpacing = calculateHorizontalSpacing(citiesNumber);
        String citiesName = "";
        for(int i = 0; i < citiesNumber; i ++) {
            citiesName = citiesName + permitCard.getCityPermits().get(i).toString().substring(0,1);
            citiesName = (i!=citiesNumber-1) ? citiesName+"/":citiesName;
        }
        gc.fillText(citiesName,horizontalSpacing,100);

        WritableImage nobi = new WritableImage(232,260);
        canvas.snapshot(null,nobi);
        BufferedImage bi = SwingFXUtils.fromFXImage(nobi, null);
        return SwingFXUtils.toFXImage(bi, null);
    }

    private int bonusSize(PermitCard permitCard) {
        int i = 0;
        Iterator<Bonus> bonusIterator = permitCard.getBonusesIterator();
        while(bonusIterator.hasNext()) {
            bonusIterator.next();
            i++;
        }
        return i;
    }

    private Image imageLoader(Bonus bonus, ClassLoader loader) {
        String className = bonus.getClass().getName();
        String toLoad;
        toLoad = className.substring(className.lastIndexOf(".")+1).toLowerCase();
        toLoad = "BonusImages/" + toLoad + "_" + bonus.getValue() + ".png";
        return new Image(loader.getResourceAsStream(toLoad));
    }

    private int calculateHorizontalSpacing(int count) {
        return 16 + (232 - 2 * 12 -
                count * 60 - (count - 1) * 5) / 2;
    }

    PopOver getMyPopover() {
        return myPopover;
    }

    public PermitCard getPermitCard() {
        return permitCard;
    }
}
