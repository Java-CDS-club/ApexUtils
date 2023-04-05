package xyz.apex.utils.events;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * A EventResult states whether an event was posted successfully, cancelled or passed.
 *
 * @param <E> Type of event the result is for.
 */
public sealed interface EventResult<E extends Event> extends IntSupplier permits EventResultImpl
{
    /**
     * Marker for cancelled EventResults
     */
    int CANCELLED = 0;

    /**
     * Marker for successful EventResults
     */
    int SUCCESS = 1;

    /**
     * Marker for pass EventResults
     */
    int PASS = 2;

    /**
     * @return The EventType this EventResult is for.
     */
    EventType<E> eventType();

    /**
     * Consume the Event only if it was a success.
     *
     * @param consumer Consumer to be processed if Event was successful.
     */
    void ifSuccess(Consumer<E> consumer);

    /**
     * Consume the Event only if it was cancelled.
     *
     * @param consumer Consumer to be processed if Event was cancelled.
     */
    void ifCancelled(Consumer<E> consumer);

    /**
     * Consume the Event only if it was passed.
     *
     * @param runnable Runnable to be processed if Event was passed.
     */
    void ifPass(Runnable runnable);

    /**
     * Consume the Event no matter the outcome.
     *
     * @param consumer Consumer to be processed if Event was successful or cancelled.
     * @param runnable Runnable to be processed if Event was passed.
     */
    void any(Consumer<E> consumer, Runnable runnable);

    /**
     * @return True if event posted successfully.
     */
    boolean wasSuccess();

    /**
     * @return True if a listener cancelled the event.
     */
    boolean wasCancelled();

    /**
     * Returns true if the event was passed while being posted.
     * <p>
     * Events are passed if no listeners are registered onto the EventBus.
     *
     * @return True if event was passed.
     */
    boolean wasPassed();

    /**
     * Creates a new successful EventResult for the given Event.
     *
     * @param eventType EventType to create this EventResult for.
     * @param event Event to create this EventResult for.
     * @return The newly constructed EventResult.
     * @param <E> Type of Event to create this EventResult for.
     */
    static <E extends Event> EventResult<E> success(EventType<E> eventType, E event)
    {
        return new EventResultImpl<>(eventType, event, SUCCESS);
    }

    /**
     * Creates a new cancelled EventResult for the given Event.
     *
     * @param eventType EventType to create this EventResult for.
     * @param event Event to create this EventResult for.
     * @return The newly constructed EventResult.
     * @param <E> Type of Event to create this EventResult for.
     */
    static <E extends Event> EventResult<E> cancelled(EventType<E> eventType, E event)
    {
        return new EventResultImpl<>(eventType, event, CANCELLED);
    }

    /**
     * Creates a new pass EventResult for the given Event.
     *
     * @param eventType EventType to create this EventResult for.
     * @return The newly constructed EventResult.
     * @param <E> Type of Event to create this EventResult for.
     */
    static <E extends Event> EventResult<E> pass(EventType<E> eventType)
    {
        return new EventResultImpl<>(eventType, null, PASS);
    }
}
