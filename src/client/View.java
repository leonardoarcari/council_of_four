package client;

import core.ModelInterface;
import core.Player;
import core.connection.Communicator;
import core.gamelogic.actions.Action;
import core.gamelogic.actions.HireServantAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class View {
    private ControllerUI controller;
    private Communicator socketCommunicator;
    private Player player;
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

    public void setPlayer(Player player) {
        this.player = player;
        System.out.println("Mi sono ricevuto");
        sendFakeAsshole();
    }

    public void sendFakeAsshole() {
        System.out.println("Send fake ass action, press 5");
        try {
            String answer = in.readLine();
            if (Integer.valueOf(answer).equals(5)) {
                Action fakeAss = new HireServantAction(player);
                controller.getSocketConnection().sendInfo(fakeAss);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void print(ModelInterface modelInterface) {
        System.out.println(modelInterface.getUsefulStuff());
    }
}
