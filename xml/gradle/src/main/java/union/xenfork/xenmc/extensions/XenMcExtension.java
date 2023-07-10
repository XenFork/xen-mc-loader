package union.xenfork.xenmc.extensions;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XenMcExtension {
    public Project project;
    public int threadDownloadCount = 3;// thread download -> thread count, default 3 thread
    public File cacheHome;
    public boolean bpDownload = false;
}