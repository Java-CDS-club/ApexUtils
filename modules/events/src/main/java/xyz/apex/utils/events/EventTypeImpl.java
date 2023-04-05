package xyz.apex.utils.events;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import xyz.apex.utils.core.ApexUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

final class EventTypeImpl<E extends Event> implements EventType<E>
{
    static final Map<Class<?>, EventType<?>> EVENT_TYPES = Maps.newHashMap();

    private final Class<E> classType;
    private final Constructor<E> constructor;
    private final List<Consumer<E>> listeners = Lists.newLinkedList();

    EventTypeImpl(Class<E> classType, Class<?>... argTypes)
    {
        this.classType = classType;

        try
        {
            constructor = classType.getConstructor(argTypes);
        }
        catch(NoSuchMethodException e)
        {
            throw new RuntimeException("Failed to find matching constructor for event class: '%s' (%s')".formatted(classType.getName(), Arrays.toString(argTypes)), e);
        }
    }

    @Override
    public void addListener(Consumer<E> listener)
    {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Consumer<E> listener)
    {
        listeners.remove(listener);
    }

    @Override
    public EventResult<E> post(Object... eventArgs)
    {
        try
        {
            if(listeners.isEmpty()) return EventResult.pass(this);
            var event = newInstance(eventArgs);

            for(var listener : listeners)
            {
                listener.accept(event);
            }

            if(wasCancelled(event)) return EventResult.cancelled(this, event);
            return EventResult.success(this, event);
        }
        catch(Throwable e)
        {
            ApexUtils.LOGGER.error("Error occurred while posting '{}'", this);
            throw e;
        }
    }

    @Override
    public E newInstance(Object... args)
    {
        try
        {
            return constructor.newInstance(args);
        }
        catch(InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException("Failed to construct new event instance for event type: '%s'".formatted(classType.getName()), e);
        }
    }

    @Override
    public Class<E> classType()
    {
        return classType;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(!(obj instanceof EventType<?> other)) return false;
        return classType == other.classType();
    }

    @Override
    public int hashCode()
    {
        return classType.hashCode();
    }

    @Override
    public String toString()
    {
        return "EventType[%s]".formatted(classType.getName());
    }

    private static <E extends Event> boolean wasCancelled(E event)
    {
        return event instanceof CancellableEvent cancellable && cancellable.wasCancelled();
    }
}
