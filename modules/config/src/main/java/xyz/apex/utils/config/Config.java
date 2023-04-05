package xyz.apex.utils.config;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Base interface for all configs.
 */
public sealed interface Config extends Iterable<Map.Entry<String, ConfigValue<?>>> permits ConfigImpl
{
    /**
     * @return The relative file path.
     */
    String filePath();

    /**
     * @return The path leading to the config file.
     */
    Path path();

    /**
     * @return Set of all config keys.
     */
    Set<String> keys();

    /**
     * @return Collection of all config values.
     */
    Collection<ConfigValue<?>> values();

    /**
     * @return Set of all config entries.
     */
    Set<Map.Entry<String, ConfigValue<?>>> entries();

    /**
     * Returns config value bound to given key or null if none exists.
     *
     * @param key Config value key.
     * @return Config value if exists or null.
     */
    @Nullable ConfigValue<?> get(String key);

    /**
     * Returns config value bound to given key, throws exception if none exists.
     *
     * @param key Config value key.
     * @return Config value.
     */
    ConfigValue<?> getOrThrow(String key);

    /**
     * Returns true if config exists with given key.
     *
     * @param key Config value key.
     * @return True if config exists with given key.
     */
    boolean containsKey(String key);

    /**
     * Invokes the given consumer for each config entry.
     *
     * @param consumer Consumer to invoke per config entry.
     */
    void forEach(BiConsumer<String, ConfigValue<?>> consumer);

    /**
     * @return True if config has unsaved changes.
     */
    boolean isDirty();

    /**
     * Attempts to load config from disk, merging any unsaved changes with loaded changes.
     * <p>
     * Merging is done by checking if the loaded changes are defaults.<br>
     * If defaults are loaded from disk, the unsaved changes are used.<br>
     * If loaded changes are not defaults, those loaded changes are used.
     */
    void load();

    /**
     * Attempts to save config to disk.
     */
    void save();

    /**
     * Attempts to save config to disk, using default values.
     */
    void saveDefaults();
}
