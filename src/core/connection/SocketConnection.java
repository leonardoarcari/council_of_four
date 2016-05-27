package core.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class SocketConnection implements Connection, Runnable {
    private Socket socket;
    private InfoProcessor processor;
    private Communicator socketCommunicator;
    private BufferedReader in;
    private Gson gson;

    public SocketConnection(InfoProcessor processor, Socket socket) throws IOException {
        this.processor = processor;
        gson = new GsonBuilder().create();
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
            // Open stream reader
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Wait for messages in the socket
            while (true) {
                String info = in.readLine();
                if(info == null)
                    System.out.println("MERDA NULL");
                StringTokenizer tokenizer = new StringTokenizer(info, SocketCommunicator.SEPARATOR);
                String className = tokenizer.nextToken();
                String json = tokenizer.nextToken();
                Object data = gson.fromJson(json, Class.forName(className));
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
