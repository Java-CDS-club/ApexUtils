package xyz.apex.utils.config;

import com.google.gson.JsonElement;

import java.util.function.BiFunction;
import java.util.function.Function;

record ConfigSerializerImpl<T>(BiFunction<T, JsonElement, T> deserializer, Function<T, JsonElement> serializer) implements ConfigSerializer<T>
{
    @Override
    public T deserialize(T defaultValue, JsonElement json)
    {
        return deserializer.apply(defaultValue, json);
    }

    @Override
    public JsonElement serialize(T value)
    {
        return serializer.apply(value);
    }
}
