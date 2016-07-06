package client;
import client.View.cli.CLI;
import client.View.gui.GUI;
import javafx.application.Application;

/**
 * Main method of Client application. You can run it from your console by typing<p>
 * <code>binaryName -ui {cli | gui}</code><p>
 * <b>cli</b> option will run the client using a Command Line Interface<p>
 * <b>gui</b> option will run the client showing a Graphical User Interface
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
