package xyz.apex.utils.config;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.utils.core.ServiceHelper;

import java.util.List;
import java.util.function.Consumer;

/**
 * Service used to notify listeners of various config events.
 */
public interface ConfigService
{
    /**
     * Internal list of all services, should never need this.
     */
    @ApiStatus.Internal
    List<ConfigService> SERVICES = ServiceHelper.loadAll(ConfigService.class);

    /**
     * Method invoked when Config is loaded.
     *
     * @param config Config which was loaded.
     */
    default void onConfigLoaded(Config config)
    {
    }

    /**
     * Method invoked when Config was saved.
     *
     * @param config Config which was saved.
     */
    default void onConfigSaved(Config config)
    {
    }

    @ApiStatus.Internal
    static void consume(Consumer<ConfigService> consumer)
    {
        SERVICES.forEach(consumer);
    }
}
