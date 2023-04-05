package xyz.apex.utils.events.test;

import xyz.apex.utils.events.Event;
import xyz.apex.utils.events.EventType;

public final class TestEvent implements Event
{
    public static final EventType<TestEvent> EVENT_TYPE = EventType.register(TestEvent.class);
}
