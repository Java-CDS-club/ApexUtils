package xyz.apex.utils.config.events;

import xyz.apex.utils.config.Config;
import xyz.apex.utils.events.Event;
import xyz.apex.utils.events.EventType;

public sealed interface ConfigEvent extends Event permits BaseConfigEvent
{
    EventType<ConfigLoadEvent> LOAD = EventType.register(ConfigLoadEvent.class, Config.class);
    EventType<ConfigSaveEvent> SAVE = EventType.register(ConfigSaveEvent.class, Config.class);

    Config config();
}
