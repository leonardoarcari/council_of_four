package core;

/**
 * An <code>Observer</code> is an entity interested in a {@link Subject Subject} changes. It can subscribe to those
 * updates by calling {@link Subject#registerObserver(Observer)} method on a <code>Subject</code>
 */
public interface Observer {
    /**
     * Reacts to a <code>Subject</code> change. This method is invoked by Subject(s) upon state changes.
     * @param subject A reference to the <code>Subject</code> object that published an update.
     */
    void update(Subject subject);
}
