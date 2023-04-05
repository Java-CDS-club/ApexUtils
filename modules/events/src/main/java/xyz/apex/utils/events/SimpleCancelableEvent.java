package xyz.apex.utils.events;

public class SimpleCancelableEvent implements CancellableEvent
{
    private boolean cancelled = false;

    @Override
    public final void cancel()
    {
        cancelled = true;
    }

    @Override
    public final boolean wasCancelled()
    {
        return cancelled;
    }
}
