package core;

/**
 * A <code>Subject</code> is a source of information. Represents the publisher in the <i>Publish/Subscribe</i> pattern
 * also known as Observer pattern. An <code>{@link Observer Observer}</code> can subscribe to a <code>Subject</code> to
 * be notified when the state of it has changed.
 */
public interface Subject {
    /**
     * Subscribes an <code>Observer</code> to be notified when <code>this</code> has changed
     * @param observer An Observer object interested in changes of this Subject
     */
    void registerObserver(Observer observer);

    /**
     * Removes an <code>Observer</code> from those interested in changes of this Subject
     * @param observer An Observer object that wants to un-subscribe from this Subject updates
     */
    void removeObserver(Observer observer);

    /**
     * Publish an update of state. All the <code>Observers</code> that subscribed to <code>this</code> Subject updates
     * are notified by having their {@link Observer#update(Subject)} method invoked.
     */
    void notifyObservers();
}
