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
 * A <code>SocketCommunicator</code> serializes an input <code>object</code> to send to another machine using a standard
 * TCP connection. Objects are serialized as JSON strings using Google's GSON library.
 */
public class SocketCommunicator implements Communicator {
    public static final String SEPARATOR = "çççççç";
    public static final String END_JSON = "******";
    private BufferedWriter out;
    private Socket socket;
    private Gson gson;

    /**
     * Initializes a <code>SocketCommunicator</code> and GSON library to be able to serialize Bonus and SellableItem
     * interfaces correctly.
     * @param socket TCP socket
     * @throws IOException
     */
    public SocketCommunicator(Socket socket) throws IOException {
        this.socket = socket;
        gson = new GsonBuilder()
                .registerTypeAdapter(Bonus.class, new InterfaceAdapter<Bonus>())
                .registerTypeAdapter(SellableItem.class, new InterfaceAdapter<SellableItem>())
                .create();
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Serializes <code>info</code> as a JSON string, prepends <code>info</code>'s dynamic class name and a separator
     * and appends a string finalizing sequence.
     * @param info Object to send
     */
    @Override
    public void sendInfo(Object info) {
        String jsonString = gson.toJson(info);
        String className = info.getClass().getName();
        jsonString = className + SEPARATOR + jsonString + END_JSON;
        try {
            out.write(jsonString+"\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
