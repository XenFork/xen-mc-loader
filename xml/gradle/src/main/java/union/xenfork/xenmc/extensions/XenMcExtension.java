package union.xenfork.xenmc.extensions;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class XenMcExtension {
    public Project project;
    public int threadDownloadCount = 1;// thread download -> thread count
    public File cacheHome;
}
