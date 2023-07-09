package union.xenfork.xenmc.gradle.util;

import java.util.Locale;

public class GradleDir {
    protected static String getOsName() {
        return System.getProperty("os.name").toLowerCase(Locale.ROOT);
    }
}
