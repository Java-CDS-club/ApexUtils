package xyz.apex.utils.config;

import com.google.gson.JsonElement;

/**
 * ConfigSerializer - Used to serializer and deserialize ConfigValues from disk.
 *
 * @param <T> Data type to be serialized.
 */
public sealed interface ConfigSerializer<T> permits ConfigSerializerImpl
{
    /**
     * Returns deserialized config value from given Json input, or default if could not deserialize.
     *
     * @param defaultValue Default value to be returned if it could not deserialize.
     * @param json Json to deserialize from.
     * @return Deserialized value or default if could not deserialize.
     */
    T deserialize(T defaultValue, JsonElement json);

    /**
     * Serializes given value to Json.
     *
     * @param value Value to be serialized.
     * @return Serialized Json.
     */
    JsonElement serialize(T value);
}
