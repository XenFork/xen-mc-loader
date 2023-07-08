package union.xenfork.xenmc.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import union.xenfork.xenmc.gradle.XenMcGradleExtension;

public class CTask extends DefaultTask {
    public final XenMcGradleExtension extension;
    public CTask() {
        extension = getProject().getExtensions().getByType(XenMcGradleExtension.class);
        setGroup("xenmc");
    }
}
