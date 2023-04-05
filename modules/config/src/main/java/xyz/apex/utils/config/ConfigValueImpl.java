package xyz.apex.utils.config;

import org.apache.commons.lang3.Validate;

non-sealed class ConfigValueImpl<T> implements ConfigValue<T>
{
    protected final ConfigImpl config;
    protected final String key;
    private T value;
    protected final T defaultValue;
    protected boolean isDirty = false;
    protected final ConfigSerializer<T> serializer;

    protected ConfigValueImpl(Config config, String key, T initialValue,  T defaultValue, ConfigSerializer<T> serializer)
    {
        Validate.isInstanceOf(ConfigImpl.class, config);
        this.config = (ConfigImpl) config;
        this.key = key;
        value = initialValue;
        this.defaultValue = defaultValue;
        this.serializer = serializer;
    }

    @Override
    public final Config config()
    {
        return config;
    }

    @Override
    public final String key()
    {
        return key;
    }

    @Override
    public final T get()
    {
        return value;
    }

    @Override
    public final void set(T value)
    {
        if(this.value == value) return;
        this.value = value;
        if(config.canBeDirty) isDirty = true;
    }

    @Override
    public final T defaultValue()
    {
        return defaultValue;
    }

    @Override
    public final boolean isDefault()
    {
        return isDefault(value);
    }

    @Override
    public final boolean isDefault(T value)
    {
        return value == defaultValue;
    }

    @Override
    public final boolean isDirty()
    {
        return config.canBeDirty && isDirty;
    }

    @Override
    public final ConfigSerializer<T> serializer()
    {
        return serializer;
    }

    @Override
    public final boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(!(obj instanceof ConfigValue<?> other)) return false;
        return config.equals(other.config()) && key.equals(other.key()) && value == other.get();
    }

    @Override
    public final int hashCode()
    {
        return key.hashCode();
    }

    @Override
    public final String toString()
    {
        return "ConfigValue[%s=%s]".formatted(key, value);
    }

    static final class BooleanImpl extends ConfigValueImpl<java.lang.Boolean> implements Boolean
    {
        BooleanImpl(Config config, String key, boolean initialValue, boolean defaultValue)
        {
            super(config, key, initialValue, defaultValue, ConfigSerializers.BOOLEAN);
        }
    }

    static non-sealed class NumericImpl<N extends Number> extends ConfigValueImpl<N> implements Numeric<N>
    {
        protected final N minValue;
        protected final N maxValue;

        protected NumericImpl(Config config, String key, N initialValue, N defaultValue, N minValue, N maxValue, ConfigSerializer<N> serializer)
        {
            super(config, key, initialValue, defaultValue, serializer);

            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Override
        public final N minValue()
        {
            return minValue;
        }

        @Override
        public final N maxValue()
        {
            return maxValue;
        }

        @Override
        public final int getAsInt()
        {
            return Numeric.super.getAsInt();
        }

        @Override
        public final double getAsDouble()
        {
            return Numeric.super.getAsDouble();
        }

        @Override
        public final float getAsFloat()
        {
            return Numeric.super.getAsFloat();
        }

        @Override
        public final long getAsLong()
        {
            return Numeric.super.getAsLong();
        }

        @Override
        public final boolean getAsBoolean()
        {
            return Numeric.super.getAsBoolean();
        }
    }

    static final class IntegerImpl extends NumericImpl<java.lang.Integer> implements Integer
    {
        IntegerImpl(Config config, String key, int initialValue, int defaultValue, int minValue, int maxValue)
        {
            super(config, key, initialValue, defaultValue, minValue, maxValue, ConfigSerializers.INTEGER);
        }
    }

    static final class DoubleImpl extends NumericImpl<java.lang.Double> implements Double
    {
        DoubleImpl(Config config, String key, double initialValue, double defaultValue, double minValue, double maxValue)
        {
            super(config, key, initialValue, defaultValue, minValue, maxValue, ConfigSerializers.DOUBLE);
        }
    }

    static final class FloatImpl extends NumericImpl<java.lang.Float> implements Float
    {
        FloatImpl(Config config, String key, float initialValue, float defaultValue, float minValue, float maxValue)
        {
            super(config, key, initialValue, defaultValue, minValue, maxValue, ConfigSerializers.FLOAT);
        }
    }

    static final class LongImpl extends NumericImpl<java.lang.Long> implements Long
    {
        LongImpl(Config config, String key, long initialValue, long defaultValue, long minValue, long maxValue)
        {
            super(config, key, initialValue, defaultValue, minValue, maxValue, ConfigSerializers.LONG);
        }
    }
}
