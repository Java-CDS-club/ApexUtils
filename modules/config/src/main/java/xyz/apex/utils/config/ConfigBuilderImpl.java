package xyz.apex.utils.config;

import java.util.function.Function;

final class ConfigBuilderImpl implements ConfigBuilder
{
    private final ConfigImpl config;

    ConfigBuilderImpl(String filePath)
    {
        config = new ConfigImpl(filePath);
    }

    @Override
    public <T, V extends ConfigValue<T>> V define(String key, Function<Config, V> configFactory)
    {
        if(config.containsKey(key)) throw new IllegalStateException("Duplicate config value registration: '%s:%s'".formatted(config.filePath(), key));
        var instance = configFactory.apply(config);
        config.registerFromBuilder(instance);
        return instance;
    }

    @Override
    public Config build()
    {
        return config;
    }
}
