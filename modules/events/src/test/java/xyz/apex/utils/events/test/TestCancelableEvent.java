package xyz.apex.utils.events.test;

import xyz.apex.utils.events.EventType;
import xyz.apex.utils.events.SimpleCancelableEvent;

public final class TestCancelableEvent extends SimpleCancelableEvent
{
    public static final EventType<TestCancelableEvent> EVENT_TYPE = EventType.register(TestCancelableEvent.class);
}
