package core.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class SocketConnection implements Connection, Runnable {
    private Socket socket;
    private InfoProcessor processor;
    private Communicator socketCommunicator;
    private ObjectInputStream in;

    public SocketConnection(InfoProcessor processor, Socket socket) throws IOException {
        this.processor = processor;
        this.socket = socket;
        socketCommunicator = new SocketCommunicator(socket);
    }

    @Override
    public void setInfoProcessor(InfoProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void sendInfo(Object info) {
        if (socketCommunicator != null) {
            socketCommunicator.sendInfo(info);
        }
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Object data = in.readObject();
                processor.processInfo(data);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("connection closed from the other side");
            e.printStackTrace();
            try {
                in.close();
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
