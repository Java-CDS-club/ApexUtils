package xyz.apex.utils.config.events;

import xyz.apex.utils.config.Config;

non-sealed class BaseConfigEvent implements ConfigEvent
{
    protected final Config config;

    protected BaseConfigEvent(Config config)
    {
        this.config = config;
    }

    @Override
    public final Config config()
    {
        return config;
    }
}
