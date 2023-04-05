package xyz.apex.utils.core.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.apex.utils.core.ApexUtils;

public final class ApexUtilsTests
{
    @Test
    void validateInstance()
    {
        Assertions.assertNotNull(ApexUtils.INSTANCE);
    }
}
