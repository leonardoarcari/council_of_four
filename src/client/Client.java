package client;
import client.View.gui.GUI;
import javafx.application.Application;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class Client {
    public static final String debugOption = "-guiDebug";
    public static void main(String[] args) {
        Application.launch(GUI.class, args);
    }
}
