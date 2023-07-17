package union.xenfork.xenmc.step.s5.mapping;

import org.gradle.api.Project;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.util.Utils;

public class MappingPlugin implements BootstrappedPluginProject {

    @Override
    public void apply(Project project, MinecraftExtension minecraft) {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("test-mapping");
    }
}
