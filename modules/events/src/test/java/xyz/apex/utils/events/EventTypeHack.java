package xyz.apex.utils.events;

import java.util.List;
import java.util.function.Consumer;

public interface EventTypeHack
{
    static <E extends Event> List<Consumer<E>> listeners(EventType<E> eventType)
    {
        return ((EventTypeImpl<E>) eventType).listeners;
    }
}
