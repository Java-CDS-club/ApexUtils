package xyz.apex.utils.config.events;

import xyz.apex.utils.config.Config;
import xyz.apex.utils.config.ConfigService;

public final class EventsConfigService implements ConfigService
{
    @Override
    public void onConfigLoaded(Config config)
    {
        ConfigEvent.LOAD.post(config);
    }

    @Override
    public void onConfigSaved(Config config)
    {
        ConfigEvent.SAVE.post(config);
    }
}
