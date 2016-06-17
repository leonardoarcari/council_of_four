package client;
import client.View.cli.CLI;
import client.View.gui.GUI;
import javafx.application.Application;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class Client {
    public static final String uiOption = "-ui";
    public static final String cliOption = "cli";
    public static final String guiOption = "gui";
    private static final String help = "Usage: binaryName -ui {cli | gui}";
    public static void main(String[] args) {
        if (args.length < 2) System.out.println("Missing ui option.\n" +
                help);
        else if (!args[0].equals(uiOption)) System.out.println("Unknown option." +
                help);
        else if (!args[1].equals(cliOption) && !args[1].equals(guiOption)) System.out.println("Unknown ui parameter.\n" +
                help);
        else {
            if (args[1].equals(cliOption)) {
                CLI cli = new CLI();
                cli.run();
            } else Application.launch(GUI.class, args);
        }
    }
}
