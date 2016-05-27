package core.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class SocketCommunicator implements Communicator {
    public static final String SEPARATOR = "\0007";
    private PrintWriter out;
    private Gson gson;


    public SocketCommunicator(Socket socket) throws IOException {
        out = new PrintWriter(socket.getOutputStream());
        gson = new GsonBuilder().create();
    }

    @Override
    public void sendInfo(Object info) {
        String jsonString = gson.toJson(info);
        String className = info.getClass().getName();
        jsonString = className + SEPARATOR + jsonString;
        System.out.println("Sending:\nClass: " + className + "\n" +
                jsonString);
        out.println(jsonString);
        out.flush();
    }
}
