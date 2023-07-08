package union.xenfork.xenmc.gradle.util;

import java.util.Locale;

import static union.xenfork.xenmc.gradle.util.OsSys.MAC;
import static union.xenfork.xenmc.gradle.util.OsSys.WIN;

public class GradleDir {
    protected static String getOsName() {
        return System.getProperty("os.name").toLowerCase(Locale.ROOT);
    }
}
