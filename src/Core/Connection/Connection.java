package Core.Connection;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public interface Connection extends Communicator, InfoProcessor {
    void setInfoProcessor(InfoProcessor processor);
}
