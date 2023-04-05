package xyz.apex.utils.events;

import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * Base interface of all EventType instances.
 * <p>
 * Used to register, unregister &#38; post Events to EventBuses.
 *
 * @param <E> Type of Event this EventType is for.
 */
public sealed interface EventType<E extends Event> permits EventTypeImpl
{
    /**
     * Registers a new listener.
     *
     * @param listener Listener to be invoked when event of given EventType is posted.
     */
    void addListener(Consumer<E> listener);

    /**
     * Unregisters the given event listener.
     *
     * @param listener Listener to be unregistered.
     */
    void removeListener(Consumer<E> listener);

    /**
     * Posts the Event to all registered listeners.
     * <p>
     * Register a new listener using {@link #addListener(Consumer)}.
     * <p>
     * Returns whether this event was posted successfully, cancelled or passed.
     *
     * @param eventArgs Args passed along to EventType to construct a new event instance.
     * @return whether this event was posted successfully, cancelled or passed.
     * @see EventResult
     */
    EventResult<E> post(Object... eventArgs);

    /**
     * Constructs a new Event instance.
     * <p>
     * {@code args} <b>MUST</b> match the {@code argTypes} passed to {@link #register(Class, Class[])}.
     * <p>
     * Internal method, should not be invoked manually, called during {@link EventType#post(Object...)}.
     *
     * @param args Event args used to construct the new EventInstance.
     * @return Newly constructed Event instance.
     */
    @ApiStatus.Internal
    E newInstance(Object... args);

    /**
     * @return Class type this EventType is bound to.
     */
    Class<E> classType();

    /**
     * Registers a new EventType, Only 1 EventType may exist per Event.
     *
     * @param eventType Type of Event to create the EventType for.
     * @param argTypes Argument types to be used to look up a matching constructor.
     * @return Newly registered EventType.
     * @param <E> Type of Event for this EventType.
     */
    static <E extends Event> EventType<E> register(Class<E> eventType, Class<?>... argTypes)
    {
        var instance = new EventTypeImpl<>(eventType, argTypes);
        if(EventTypeImpl.EVENT_TYPES.put(eventType, instance) != null) throw new IllegalStateException("Duplicate event type registration: '%s'".formatted(eventType.getName()));
        return instance;
    }
}
