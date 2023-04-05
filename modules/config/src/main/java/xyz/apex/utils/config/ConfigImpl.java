package xyz.apex.utils.config;

import com.google.common.collect.Maps;
import com.google.gson.*;
import org.jetbrains.annotations.Nullable;
import xyz.apex.utils.core.ApexUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;

final class ConfigImpl implements Config
{
    static
    {
        // ensure config serializers have loaded before the config class
        ConfigSerializers.bootstrap();
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final String filePath;
    private final Path path;
    private final Map<String, ConfigValue<?>> configMap = Maps.newHashMap();
    private final Set<String> keys = Collections.unmodifiableSet(configMap.keySet());
    private final Collection<ConfigValue<?>> values = Collections.unmodifiableCollection(configMap.values());
    private final Set<Map.Entry<String, ConfigValue<?>>> entries = Collections.unmodifiableSet(configMap.entrySet());
    boolean canBeDirty = true;

    ConfigImpl(String filePath)
    {
        this.filePath = filePath;
        path = ApexUtils.INSTANCE.configsDir().resolve(filePath);
    }

    <T> void registerFromBuilder(ConfigValue<T> configValue)
    {
        configMap.put(configValue.key(), configValue);
    }

    @Override
    public String filePath()
    {
        return filePath;
    }

    @Override
    public Path path()
    {
        return path;
    }

    @Override
    public Set<String> keys()
    {
        return keys;
    }

    @Override
    public Collection<ConfigValue<?>> values()
    {
        return values;
    }

    @Override
    public Set<Map.Entry<String, ConfigValue<?>>> entries()
    {
        return entries;
    }

    @Nullable
    @Override
    public ConfigValue<?> get(String key)
    {
        return configMap.get(key);
    }

    @Override
    public ConfigValue<?> getOrThrow(String key)
    {
        return Objects.requireNonNull(configMap.get(key));
    }

    @Override
    public boolean containsKey(String key)
    {
        return configMap.containsKey(key);
    }

    @Override
    public void forEach(BiConsumer<String, ConfigValue<?>> consumer)
    {
        configMap.forEach(consumer);
    }

    @Override
    public boolean isDirty()
    {
        return canBeDirty && values.stream().anyMatch(ConfigValue::isDirty);
    }

    @Override
    public void load()
    {
        ApexUtils.LOGGER.info("Loading config file: '{}'", filePath);

        // if file does not exist, save defaults
        if(!Files.exists(path))
        {
            ApexUtils.LOGGER.info("Config file ({}) does not exist, saving defaults to disk!", filePath);
            save(true, true);
            // notify services
            ConfigService.consume(service -> service.onConfigLoaded(this));
            return;
        }

        // cache any unsaved changes, these will be merged later on
        var unsaved = Maps.<String, Object>newHashMap();
        if(isDirty()) values.stream().filter(ConfigValue::isDirty).forEach(configValue -> unsaved.put(configValue.key(), configValue.defaultValue()));

        // load config from disk
        JsonObject root;

        try(var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8))
        {
            var json = GSON.fromJson(reader, JsonElement.class);
            if(!json.isJsonObject()) throw new JsonParseException("Config file %s was not parsed as a valid JsonObject, ensure the contents are a valid JsonObject!".formatted(filePath));
            root = json.getAsJsonObject();
        }
        catch(IOException e)
        {
            ApexUtils.LOGGER.error("Error occurred while reading config file: {}", filePath, e);
            // notify services
            ConfigService.consume(service -> service.onConfigLoaded(this));
            return;
        }

        // load changes from json
        var parsed = Maps.<String, Object>newHashMap(); // map of none default config values deserialized from json
        var missing = Maps.<String, JsonElement>newHashMap(); // map of default values serialized to json (if missing from json) | these should be written to disk
        values.forEach(configValue -> processConfigValue(parsed, missing, configValue, root.get(configValue.key())));

        // merge unsaved changes into newly loaded changes, if any exist
        // this works by checking if loaded changes are default or not
        // if we loaded default changes from disk, use the unsaved changes
        // if we loaded none default changes, use the changes loaded from disk & drop the unsaved changes
        if(!unsaved.isEmpty()) values.forEach(configValue -> mergeValues(parsed, unsaved, configValue));

        // update configs with loaded/merged values
        canBeDirty = false;
        values.forEach(configValue -> updateValue(parsed, configValue));
        canBeDirty = true;

        // write missing configs to disk
        if(!missing.isEmpty())
        {
            ApexUtils.LOGGER.debug("Detected missing config entries! writing them to disk");

            var newJson = new JsonObject();
            missing.forEach(newJson::add);

            values.forEach(configValue -> {
                var serialized = serializeForMissing(parsed, missing, configValue);
                if(serialized.isJsonNull()) return;
                newJson.add(configValue.key(), serialized);
            });

            write(path, newJson);
        }

        // notify services
        ConfigService.consume(service -> service.onConfigLoaded(this));
    }

    @Override
    public void save()
    {
        save(false, false);
    }

    @Override
    public void saveDefaults()
    {
        save(false, true);
    }

    private void save(boolean forced, boolean saveDefaults)
    {
        if(!forced && !isDirty()) return; // not dirty, no need to save to disk
        if(!forced) ApexUtils.LOGGER.info("Saving config file: '{}'", filePath);

        // serialize configs to json
        var root = new JsonObject();
        values.forEach(configValue -> {
            var serialized = serialize(configValue, saveDefaults);
            if(!serialized.isJsonNull()) root.add(configValue.key(), serialized);
        });

        // write to disk
        write(path, root);

        // notify services
        ConfigService.consume(service -> service.onConfigSaved(this));
    }

    @Override
    public Iterator<Map.Entry<String, ConfigValue<?>>> iterator()
    {
        return configMap.entrySet().iterator();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(!(obj instanceof Config other)) return false;
        return filePath.equals(other.filePath());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(filePath, configMap);
    }

    @Override
    public String toString()
    {
        return "Config[%s]".formatted(filePath);
    }

    private static <T> void processConfigValue(Map<String, Object> parsed, Map<String, JsonElement> missing, ConfigValue<T> configValue, @Nullable JsonElement json)
    {
        var defaultValue = configValue.defaultValue();
        var serializer = configValue.serializer();
        var key = configValue.key();

        if(json != null && !json.isJsonNull())
        {
            var deserialized = serializer.deserialize(defaultValue, json);
            if(!configValue.isDefault(deserialized)) parsed.put(key, deserialized);
        }
        else missing.put(key, serializer.serialize(defaultValue));
    }

    @SuppressWarnings("unchecked")
    private static <T> void mergeValues(Map<String, Object> parsed, Map<String, Object> unsaved, ConfigValue<T> configValue)
    {
        try
        {
            var key = configValue.key();
            if(!unsaved.containsKey(key)) return;
            var unsavedValue = unsaved.get(key);

            if(parsed.containsKey(key))
            {
                var parsedValue = (T) parsed.get(key);
                if(configValue.isDefault(parsedValue)) parsed.put(key, unsavedValue);
            }
            else parsed.put(key, unsavedValue);
        }
        catch(ClassCastException ignored)
        {
            // NOOP
            // If casting fails we somehow invalid objects stored in the maps
            // which should never be the case
            // 'parsed' contains values loaded from disk deserialized into correct type using the config values serializer
            // 'unsaved' contains values directly from the config value, thus should be the correct type
            // if exception is thrown, just break out and don't do any merging
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void updateValue(Map<String, Object> parsed, ConfigValue<T> configValue)
    {
        try
        {
            var key = configValue.key();
            if(!parsed.containsKey(key)) return;
            configValue.set((T) parsed.get(key));
        }
        catch(ClassCastException ignored)
        {
            // NOOP | See mergeValues() for more information
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> JsonElement serializeForMissing(Map<String, Object> parsed, Map<String, JsonElement> missing, ConfigValue<T> configValue)
    {
        try
        {
            var key = configValue.key();
            if(missing.containsKey(key)) return JsonNull.INSTANCE;
            var value = (T) parsed.get(key);
            return configValue.serializer().serialize(value);
        }
        catch(ClassCastException ignored)
        {
            // NOOP | See mergeValues() for more information
            return JsonNull.INSTANCE;
        }
    }

    private static <T> JsonElement serialize(ConfigValue<T> configValue, boolean saveDefaults) // cause generics
    {
        var value = saveDefaults ? configValue.defaultValue() : configValue.get();
        return configValue.serializer().serialize(value);
    }

    private static void write(Path path, JsonElement json)
    {
        try
        {
            if(Files.isDirectory(path)) return;
            Files.createDirectories(path.getParent());
            Files.deleteIfExists(path);

            try(var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
            {
                GSON.toJson(json, writer);
                writer.newLine();
            }
        }
        catch(IOException e)
        {
            ApexUtils.LOGGER.error("Error occurred while writing file: '{}'", path, e);
        }
    }
}
