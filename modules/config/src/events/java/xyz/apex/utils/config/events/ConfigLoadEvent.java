package xyz.apex.utils.config.events;

import xyz.apex.utils.config.Config;

public final class ConfigLoadEvent extends BaseConfigEvent
{
    public ConfigLoadEvent(Config config)
    {
        super(config);
    }
}
