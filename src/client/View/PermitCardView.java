package client.View;

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
 * Created by Matteo on 06/06/16.
 */
public class PermitCardView extends ObjectImageView {
    ImageView myView;
    PermitCard permitCard;
    PopOver myPopover;

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

    public void setPermitCard(PermitCard permitCard) {
        this.permitCard = permitCard;
        this.setImage(permitImage(this.permitCard));
        myView.setImage(this.getImage());
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
        System.out.println(toLoad);
        return new Image(loader.getResourceAsStream(toLoad));
    }

    private int calculateHorizontalSpacing(int count) {
        return 16 + (232 - 2 * 12 -
                count * 60 - (count - 1) * 5) / 2;
    }

    public PopOver getMyPopover() {
        return myPopover;
    }
}