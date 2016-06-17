package core.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.SellableItem;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class SocketCommunicator implements Communicator {
    public static final String SEPARATOR = "######";
    public static final String END_JSON = "******";
    private BufferedWriter out;
    private Socket socket;
    private Gson gson;


    public SocketCommunicator(Socket socket) throws IOException {
        this.socket = socket;
        gson = new GsonBuilder()
                .registerTypeAdapter(Bonus.class, new InterfaceAdapter<Bonus>())
                .registerTypeAdapter(SellableItem.class, new InterfaceAdapter<SellableItem>())
                .create();
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void sendInfo(Object info) {
        String jsonString = gson.toJson(info);
        String className = info.getClass().getName();
        jsonString = className + SEPARATOR + jsonString + END_JSON;
        //System.out.println("Sending:\nClass: " + className + "\n" + jsonString);
        try {
            out.write(jsonString+"\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
