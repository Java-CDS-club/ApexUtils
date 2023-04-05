package xyz.apex.utils.config.events;

import xyz.apex.utils.config.Config;

public final class ConfigSaveEvent extends BaseConfigEvent
{
    public ConfigSaveEvent(Config config)
    {
        super(config);
    }
}
