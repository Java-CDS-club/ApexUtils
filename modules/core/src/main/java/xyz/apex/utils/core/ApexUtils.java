package xyz.apex.utils.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

/**
 * Main interface for ApexUtils
 * <p>
 * Mainly used for internal related stuff, should rarely be used by others.
 */
public interface ApexUtils
{
    ApexUtils INSTANCE = ServiceHelper.singleton(ApexUtils.class, DummyApexUtils::new);
    Logger LOGGER = LogManager.getLogger();

    /**
     * Returns the root path for ApexUtils.
     * <p>
     * When running with Minecraft this should be the root game directory.<br>
     * In other use cases this will be the directory the application containing ApexUtils was run from.
     *
     * @return Root path for ApexUtils
     */
    Path rootPath();

    /**
     * @return Config directory path.
     * @see #rootPath()
     */
    Path configsDir();
}
