package client;

import core.ModelInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class View {
    private ControllerUI controller;
    private BufferedReader in;

    public View(ControllerUI controller) {
        this.controller = controller;
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public void chooseOption() {
        System.out.println("Choose connection type:\n" +
                "1) Socket\n" +
                "2) RMI");
        try {
            String answer = in.readLine();
            if (Integer.valueOf(answer).equals(1)) {
                controller.socketConnection();
            } else if (Integer.valueOf(answer).equals(2)) {
                controller.rmiConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print(ModelInterface modelInterface) {
        System.out.println(modelInterface.getUsefulStuff());
    }
}
