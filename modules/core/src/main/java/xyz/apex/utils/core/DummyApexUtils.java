package xyz.apex.utils.core;

import java.nio.file.Path;
import java.nio.file.Paths;

final class DummyApexUtils implements ApexUtils
{
    private final Path path = Paths.get(".");
    private final Path configs = path.resolve("config");

    @Override
    public Path rootPath()
    {
        return path;
    }

    @Override
    public Path configsDir()
    {
        return configs;
    }
}
