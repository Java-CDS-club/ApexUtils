package xyz.apex.utils.events;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

final class EventResultImpl<E extends Event> implements EventResult<E>
{
    private final EventType<E> eventType;
    @Nullable
    private final E event;
    private final int type;

    EventResultImpl(EventType<E> eventType, @Nullable E event, int type)
    {
        this.eventType = eventType;
        this.event = event;
        this.type = type;
    }

    @Override
    public EventType<E> eventType()
    {
        return eventType;
    }

    @Override
    public void ifSuccess(Consumer<E> consumer)
    {
        if(wasSuccess()) consumer.accept(event);
    }

    @Override
    public void ifCancelled(Consumer<E> consumer)
    {
        if(wasCancelled()) consumer.accept(event);
    }

    @Override
    public void ifPass(Runnable runnable)
    {
        if(wasPassed()) runnable.run();
    }

    @Override
    public void any(Consumer<E> consumer, Runnable runnable)
    {
        if(event == null) runnable.run();
        else consumer.accept(event);
    }

    @Override
    public boolean wasSuccess()
    {
        return type == SUCCESS;
    }

    @Override
    public boolean wasCancelled()
    {
        return type == CANCELLED;
    }

    @Override
    public boolean wasPassed()
    {
        return type == PASS;
    }

    @Override
    public int getAsInt()
    {
        return type;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(!(obj instanceof EventResult<?> other)) return false;
        return eventType.equals(other.eventType()) && type == other.getAsInt();
    }
}
