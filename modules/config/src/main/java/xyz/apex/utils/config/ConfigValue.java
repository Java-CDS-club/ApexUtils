package xyz.apex.utils.config;

import java.util.function.*;

/**
 * Base interface for all ConfigValues
 *
 * @param <T> Data type of ConfigValue
 */
public sealed interface ConfigValue<T> extends Supplier<T> permits ConfigValue.Boolean, ConfigValue.Numeric, ConfigValueImpl
{
    /**
     * @return Config this ConfigValue is bound to.
     */
    Config config();

    /**
     * @return Key this ConfigValue is bound to.
     */
    String key();

    /**
     * @return The current value associated with this ConfigValue.
     */
    @Override T get();

    /**
     * Sets the current value associated with this ConfigValue.
     *
     * @param value Value to be set.
     */
    void set(T value);

    /**
     * @return Default value for this ConfigValue.
     */
    T defaultValue();

    /**
     * @return True if this ConfigValue has unsaved changes.
     */
    boolean isDirty();

    /**
     * @return True if this ConfigValue is current holding a defaultValue.
     */
    boolean isDefault();

    /**
     * Returns true if the given value is a defaultValue.
     *
     * @param value Value to be checked.
     * @return True if the given value is a defaultValue.
     */
    boolean isDefault(T value);

    /**
     * @return ConfigSerializer associated with this ConfigValue.
     */
    ConfigSerializer<T> serializer();

    sealed interface Boolean extends ConfigValue<java.lang.Boolean>, BooleanSupplier, IntSupplier permits ConfigValueImpl.BooleanImpl
    {
        int TRUE_I = 1;
        int FALSE_I = 0;

        @Override
        default boolean getAsBoolean()
        {
            return get();
        }

        @Override
        default int getAsInt()
        {
            return get() ? TRUE_I : FALSE_I;
        }
    }

    sealed interface Numeric<N extends Number> extends ConfigValue<N>, IntSupplier, DoubleSupplier, LongSupplier, BooleanSupplier permits Double, Float, Integer, Long, ConfigValueImpl.NumericImpl
    {
        /**
         * @return Minimum value for this ConfigValue.
         */
        N minValue();

        /**
         * @return Maximum value for this ConfigValue.
         */
        N maxValue();

        @Override
        default int getAsInt()
        {
            return get().intValue();
        }

        @Override
        default double getAsDouble()
        {
            return get().doubleValue();
        }

        default float getAsFloat()
        {
            return get().floatValue();
        }

        @Override
        default long getAsLong()
        {
            return get().longValue();
        }

        @Override
        default boolean getAsBoolean()
        {
            return getAsInt() >= Boolean.TRUE_I;
        }
    }

    sealed interface Integer extends Numeric<java.lang.Integer> permits ConfigValueImpl.IntegerImpl
    {
        /**
         * @return Minimum value for this ConfigValue.
         */
        default int min()
        {
            return minValue();
        }

        /**
         * @return Maximum value for this ConfigValue.
         */
        default int max()
        {
            return maxValue();
        }
    }

    sealed interface Double extends Numeric<java.lang.Double> permits ConfigValueImpl.DoubleImpl
    {
        /**
         * @return Minimum value for this ConfigValue.
         */
        default double min()
        {
            return minValue();
        }

        /**
         * @return Maximum value for this ConfigValue.
         */
        default double max()
        {
            return maxValue();
        }
    }

    sealed interface Float extends Numeric<java.lang.Float> permits ConfigValueImpl.FloatImpl
    {
        /**
         * @return Minimum value for this ConfigValue.
         */
        default float min()
        {
            return minValue();
        }

        /**
         * @return Maximum value for this ConfigValue.
         */
        default float max()
        {
            return maxValue();
        }
    }

    sealed interface Long extends Numeric<java.lang.Long> permits ConfigValueImpl.LongImpl
    {
        /**
         * @return Minimum value for this ConfigValue.
         */
        default long min()
        {
            return minValue();
        }

        /**
         * @return Maximum value for this ConfigValue.
         */
        default long max()
        {
            return maxValue();
        }
    }
}
