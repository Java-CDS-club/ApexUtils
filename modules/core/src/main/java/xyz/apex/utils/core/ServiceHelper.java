package xyz.apex.utils.core;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Common utilities for ServiceLoaders
 */
public interface ServiceHelper
{
    /**
     * Loads a singleton service, throws exception if multiple implementations exist.
     *
     * @param serviceType Type of service to be loaded.
     * @param defaultImpl Default implementation if none could be found.
     * @return Loaded service instance.
     * @param <T> Type of service to be loaded
     */
    static <T> T singleton(Class<T> serviceType, Supplier<T> defaultImpl)
    {
        var providers = ServiceLoader.load(serviceType).stream().toList();
        if(providers.isEmpty()) return defaultImpl.get();
        else if(providers.size() != 1)
        {
            var names = providers.stream().map(ServiceLoader.Provider::type).map(Class::getName).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException("There should be exactly one implementation of %s on the classpath. Found: %s".formatted(serviceType.getName(), names));
        }
        else
        {
            var provider = providers.get(0);
            consume(serviceType, provider);
            return provider.get();
        }
    }

    /**
     * Loads all services matching the provided service type.
     *
     * @param serviceType Type of service to be loaded.
     * @return List of all services matching the provided service type.
     * @param <T> Type of service to be loaded.
     */
    static <T> List<T> loadAll(Class<T> serviceType)
    {
        return ServiceLoader.load(serviceType).stream().peek(serviceProvider -> consume(serviceType, serviceProvider)).map(ServiceLoader.Provider::get).toList();
    }

    private static <T> void consume(Class<T> serviceType, ServiceLoader.Provider<T> serviceProvider)
    {
        ApexUtils.LOGGER.debug("Instantiating {} for service {}", serviceProvider.type().getName(), serviceType.getName());
    }
}
