package core.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class SocketCommunicator implements Communicator {
    public static final String SEPARATOR = "\0007";
    public static final String JSON_SEPARATOR = "*****";
    private ObjectOutputStream out;
    private Gson gson;


    public SocketCommunicator(Socket socket) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        gson = new GsonBuilder().create();
    }

    @Override
    public void sendInfo(Object info) {
        try {
            out.writeObject(info);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
