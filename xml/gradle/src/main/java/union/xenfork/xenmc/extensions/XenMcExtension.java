package union.xenfork.xenmc.extensions;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class XenMcExtension {
    public Project project;
    public Integer threadDownloadCount;// thread download -> thread count, default 3 thread
    public File cacheHome;
    public File projectHome;
    public String remapTypesOf;
    public File remapTypesDir;
    public AtomicBoolean isLoader;

    public void setIsLoader(boolean isLoader) {
        if (this.isLoader == null) {
            this.isLoader = new AtomicBoolean();
        }
        this.isLoader.set(isLoader);
    }

    public boolean getIsLoader() {
        if (this.isLoader == null) {
            return false;
        }
        return isLoader.get();
    }

    public File resourcesHome;
}
