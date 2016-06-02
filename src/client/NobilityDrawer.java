package client;

import core.gamemodel.NobilityPath;
import core.gamemodel.bonus.Bonus;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.util.List;


/**
 * Created by Matteo on 02/06/16.
 */
public class NobilityDrawer {
    private NobilityPath nobilityPath;
    private Image nobilityImage;

    public NobilityDrawer(NobilityPath nobilityPath, Image nobilityImage) {
        this.nobilityPath = nobilityPath;
        this.nobilityImage = nobilityImage;
    }

    public Image drawPath() {
        Canvas free = new Canvas(1705, 150);
        GraphicsContext gc = free.getGraphicsContext2D();
        gc.drawImage(nobilityImage, 0, 0, 1705, 150);
        List<List<Bonus>> listOBonus = nobilityPath.getBonusPath();
        int i = 0;
        for(List<Bonus> list : listOBonus) {
            if(list.size() != 0) {
                drawBonuses(gc, list, i);
            }
            i++;
        }
        WritableImage nobi = new WritableImage(1705,150);
        free.snapshot(null,nobi);
        BufferedImage bi = SwingFXUtils.fromFXImage(nobi, null);
        return SwingFXUtils.toFXImage(bi, null);
    }

    private void drawBonuses(GraphicsContext gc, List<Bonus> list, int index) {
        ClassLoader loader = this.getClass().getClassLoader();
        int ident = (index < 10) ? 23:20;
        int yPos = (list.size() == 1) ? 45:30;
        int i = 0;

        for(Bonus bonus : list) {
            String bonusName = getImageName(bonus);
            bonusName = "BonusImages/" + bonusName;
            System.out.println(bonusName + " ||| " + index);
            Image bonusImage = new Image(loader.getResourceAsStream(bonusName));
            gc.drawImage(bonusImage,ident+81*index,yPos+50*i,50,50);
            i++;
        }
    }

    private String getImageName(Bonus bonus) {
        String bonusname;
        String className = bonus.getClass().getName();
        bonusname = className.substring(className.lastIndexOf(".")+1);
        bonusname = bonusname + "_" + bonus.getValue() + ".png";
        return bonusname.toLowerCase();
    }
}
