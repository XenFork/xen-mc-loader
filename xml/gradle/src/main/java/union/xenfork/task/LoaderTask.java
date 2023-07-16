package union.xenfork.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.extensions.XenMcExtension;

public class LoaderTask extends DefaultTask {
    public final MinecraftExtension minecraft;
    public final XenMcExtension xenmc;
    public LoaderTask() {
        minecraft = getProject().getExtensions().getByType(MinecraftExtension.class);
        xenmc = minecraft.xenmc;
        setGroup("xenmc");
    }
}
