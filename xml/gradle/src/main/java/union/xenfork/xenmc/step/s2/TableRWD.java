package union.xenfork.xenmc.step.s2;

import org.gradle.api.Project;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

public class TableRWD implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        Utils.setupMessagePrefix(project, minecraft);
    }
}
