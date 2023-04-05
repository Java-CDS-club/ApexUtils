package xyz.apex.utils.config;

import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @see ConfigSerializer
 */
public interface ConfigSerializers
{
    /**
     * Used for String ConfigValue serialization
     */
    ConfigSerializer<String> STRING = primitive(
            (defaultValue, json) -> json.isString() ? json.getAsString() : defaultValue,
            JsonPrimitive::new
    );

    /**
     * Used for Integer ConfigValue serialization
     */
    ConfigSerializer<Integer> INTEGER = numeric(JsonPrimitive::getAsInt);

    /**
     * Used for Double ConfigValue serialization
     */
    ConfigSerializer<Double> DOUBLE = numeric(JsonPrimitive::getAsDouble);

    /**
     * Used for Float ConfigValue serialization
     */
    ConfigSerializer<Float> FLOAT = numeric(JsonPrimitive::getAsFloat);

    /**
     * Used for Long ConfigValue serialization
     */
    ConfigSerializer<Long> LONG = numeric(JsonPrimitive::getAsLong);

    /**
     * Used for Boolean ConfigValue serialization
     */
    ConfigSerializer<Boolean> BOOLEAN = primitive(
            (defaultValue, json) -> json.isBoolean() ? json.getAsBoolean() : defaultValue,
            JsonPrimitive::new
    );

    private static <T> ConfigSerializer<T> primitive(BiFunction<T, JsonPrimitive, T> deserializer, Function<T, JsonPrimitive> serializer)
    {
        return new ConfigSerializerImpl<>(
                (defaultValue, json) -> json instanceof JsonPrimitive prim ? deserializer.apply(defaultValue, prim) : defaultValue,
                serializer::apply
        );
    }

    private static <N extends Number> ConfigSerializer<N> numeric(Function<JsonPrimitive, N> deserializer)
    {
        return primitive(
                (defaultValue, json) -> json.isNumber() ? deserializer.apply(json) : defaultValue,
                JsonPrimitive::new
        );
    }

    @ApiStatus.Internal
    static void bootstrap() {}
}
