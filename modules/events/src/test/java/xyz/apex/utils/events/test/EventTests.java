package xyz.apex.utils.events.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.apex.utils.events.EventTypeHack;
import xyz.apex.utils.events.SimpleCancelableEvent;

import java.util.function.Consumer;

public final class EventTests
{
    @Test
    void passes()
    {
        var result = TestEvent.EVENT_TYPE.post();
        Assertions.assertTrue(result::wasPassed, "Event was not passed, It should have been");
    }

    @Test
    void listeners()
    {
        Consumer<TestEvent> listener = event -> { };
        Assertions.assertFalse(() -> EventTypeHack.listeners(TestEvent.EVENT_TYPE).contains(listener), "Test event listener exists, when its expected not to");
        TestEvent.EVENT_TYPE.addListener(listener);
        Assertions.assertTrue(() -> EventTypeHack.listeners(TestEvent.EVENT_TYPE).contains(listener), "Missing test event listener, expected to exist");
        TestEvent.EVENT_TYPE.removeListener(listener);
        Assertions.assertFalse(() -> EventTypeHack.listeners(TestEvent.EVENT_TYPE).contains(listener), "Test event listener exists, when its expected not to");
    }

    @Test
    void successful()
    {
        Consumer<TestEvent> listener = event -> { };
        TestEvent.EVENT_TYPE.addListener(listener);
        var result = TestEvent.EVENT_TYPE.post();
        Assertions.assertTrue(result::wasSuccess, "Event failed to post, expected success");
        TestEvent.EVENT_TYPE.removeListener(listener);
    }

    @Test
    void cancelled()
    {
        var result = TestEvent.EVENT_TYPE.post();
        Assertions.assertFalse(result::wasCancelled, "None cancelable event was cancelled, this should never happen");
        TestCancelableEvent.EVENT_TYPE.addListener(SimpleCancelableEvent::cancel);
        var result1 = TestCancelableEvent.EVENT_TYPE.post();
        Assertions.assertTrue(result1::wasCancelled, "Cancelable event was not cancelled, was expected to have been");
    }
}
