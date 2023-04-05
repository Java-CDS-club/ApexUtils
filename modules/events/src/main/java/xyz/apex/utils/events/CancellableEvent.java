package xyz.apex.utils.events;

/**
 * Interface used to mark Events as being cancellable.
 * <p>
 * Use {@link #cancel()} to cancel the Event.
 */
public interface CancellableEvent extends Event
{
    /**
     * Used to cancel this Event.
     */
    void cancel();

    /**
     * @return True if this Event was cancelled.
     */
    boolean wasCancelled();
}
