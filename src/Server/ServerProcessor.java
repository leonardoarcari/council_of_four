package Server;

import Core.Connection.InfoProcessor;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class ServerProcessor implements InfoProcessor {
    private Model model;

    public ServerProcessor(Model model) {
        this.model = model;
    }

    @Override
    public void processInfo(Object info) {
        //TODO: Do something on the model
    }
}
