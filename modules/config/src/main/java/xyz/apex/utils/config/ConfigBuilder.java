package xyz.apex.utils.config;

import xyz.apex.utils.core.ApexUtils;

import java.util.function.Function;

/**
 * ConfigBuilder - Used to build new Configs
 */
public sealed interface ConfigBuilder permits ConfigBuilderImpl
{
    /**
     * Builds and registers the ConfigValue to the Config.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param configFactory Factory used to construct this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     * @param <T> Data type of ConfigValue.
     * @param <V> Type of ConfigValue.
     */
    <T, V extends ConfigValue<T>> V define(String key, Function<Config, V> configFactory);

    // region: String

    /**
     * Builds and registers a new String ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue<String> defineString(String key, String initialValue, String defaultValue)
    {
        return define(key, config -> new ConfigValueImpl<>(config, key, initialValue, defaultValue, ConfigSerializers.STRING));
    }

    /**
     * Builds and registers a new String ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue<String> defineString(String key, String defaultValue)
    {
        return defineString(key, defaultValue, defaultValue);
    }
    // endregion

    // region: Numeric
    // region: Integer
    /**
     * Builds and registers a new Integer ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @param minValue Minimum value for this ConfigValue.
     * @param maxValue Maximum value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Integer defineInteger(String key, int initialValue, int defaultValue, int minValue, int maxValue)
    {
        return define(key, config -> new ConfigValueImpl.IntegerImpl(config, key, initialValue, defaultValue, minValue, maxValue));
    }

    /**
     * Builds and registers a new Integer ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @param minValue Minimum value for this ConfigValue.
     * @param maxValue Maximum value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Integer defineInteger(String key, int defaultValue, int minValue, int maxValue)
    {
        return defineInteger(key, defaultValue, defaultValue, minValue, maxValue);
    }

    /**
     * Builds and registers a new Integer ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Integer defineInteger(String key, int initialValue, int defaultValue)
    {
        return defineInteger(key, initialValue, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Builds and registers a new Integer ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Integer defineInteger(String key, int defaultValue)
    {
        return defineInteger(key, defaultValue, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    // endregion

    // region: Double
    /**
     * Builds and registers a new Double ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @param minValue Minimum value for this ConfigValue.
     * @param maxValue Maximum value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Double defineDouble(String key, double initialValue, double defaultValue, double minValue, double maxValue)
    {
        return define(key, config -> new ConfigValueImpl.DoubleImpl(config, key, initialValue, defaultValue, minValue, maxValue));
    }

    /**
     * Builds and registers a new Double ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @param minValue Minimum value for this ConfigValue.
     * @param maxValue Maximum value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Double defineInteger(String key, double defaultValue, double minValue, double maxValue)
    {
        return defineDouble(key, defaultValue, defaultValue, minValue, maxValue);
    }

    /**
     * Builds and registers a new Double ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Double defineInteger(String key, double initialValue, double defaultValue)
    {
        return defineDouble(key, initialValue, defaultValue, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
     * Builds and registers a new Double ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Double defineDouble(String key, double defaultValue)
    {
        return defineDouble(key, defaultValue, defaultValue, Double.MIN_VALUE, Double.MAX_VALUE);
    }
    // endregion

    // region: Float
    /**
     * Builds and registers a new Float ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @param minValue Minimum value for this ConfigValue.
     * @param maxValue Maximum value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Float defineFloat(String key, float initialValue, float defaultValue, float minValue, float maxValue)
    {
        return define(key, config -> new ConfigValueImpl.FloatImpl(config, key, initialValue, defaultValue, minValue, maxValue));
    }

    /**
     * Builds and registers a new Float ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @param minValue Minimum value for this ConfigValue.
     * @param maxValue Maximum value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Float defineFloat(String key, float defaultValue, float minValue, float maxValue)
    {
        return defineFloat(key, defaultValue, defaultValue, minValue, maxValue);
    }

    /**
     * Builds and registers a new Float ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Float defineFloat(String key, float initialValue, float defaultValue)
    {
        return defineFloat(key, initialValue, defaultValue, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    /**
     * Builds and registers a new Float ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Float defineFloat(String key, float defaultValue)
    {
        return defineFloat(key, defaultValue, defaultValue, Float.MIN_VALUE, Float.MAX_VALUE);
    }
    // endregion

    // region: Long
    /**
     * Builds and registers a new Long ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @param minValue Minimum value for this ConfigValue.
     * @param maxValue Maximum value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Long defineLong(String key, long initialValue, long defaultValue, long minValue, long maxValue)
    {
        return define(key, config -> new ConfigValueImpl.LongImpl(config, key, initialValue, defaultValue, minValue, maxValue));
    }

    /**
     * Builds and registers a new Long ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @param minValue Minimum value for this ConfigValue.
     * @param maxValue Maximum value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Long defineLong(String key, long defaultValue, long minValue, long maxValue)
    {
        return defineLong(key, defaultValue, defaultValue, minValue, maxValue);
    }

    /**
     * Builds and registers a new Long ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Long defineLong(String key, long initialValue, long defaultValue)
    {
        return defineLong(key, initialValue, defaultValue, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * Builds and registers a new Long ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Long defineLong(String key, long defaultValue)
    {
        return defineLong(key, defaultValue, defaultValue, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    // endregion
    // endregion

    // region: Boolean
    /**
     * Builds and registers a new Boolean ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param initialValue Initial value of this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Boolean defineBoolean(String key, boolean initialValue, boolean defaultValue)
    {
        return define(key, config -> new ConfigValueImpl.BooleanImpl(config, key, initialValue, defaultValue));
    }

    /**
     * Builds and registers a new Boolean ConfigValue.
     *
     * @param key Key to be bound to this ConfigValue.
     * @param defaultValue Default value for this ConfigValue.
     * @return Newly built &#38; registered ConfigValue.
     */
    default ConfigValue.Boolean defineBoolean(String key, boolean defaultValue)
    {
        return defineBoolean(key, defaultValue, defaultValue);
    }
    // endregion

    /**
     * @return The built Config instance.
     */
    Config build();

    /**
     * Constructs a new ConfigBuilder instance.
     *
     * @param filePath Relative file path from {@link ApexUtils#rootPath()} leading to the config file.
     * @return Newly constructed ConfigBuilder.
     */
    static ConfigBuilder builder(String filePath)
    {
        return new ConfigBuilderImpl(filePath);
    }
}
