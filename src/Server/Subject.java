package Server;

/**
 * Created by Leonardo Arcari on 14/05/2016.
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
