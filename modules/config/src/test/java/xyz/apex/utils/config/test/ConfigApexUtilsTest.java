package xyz.apex.utils.config.test;

import xyz.apex.utils.core.ApexUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class ConfigApexUtilsTest implements ApexUtils
{
    private final Path root = Paths.get("./run/testing/");
    private final Path configDir = root.resolve("config");

    @Override
    public Path rootPath()
    {
        return root;
    }

    @Override
    public Path configsDir()
    {
        return configDir;
    }
}
