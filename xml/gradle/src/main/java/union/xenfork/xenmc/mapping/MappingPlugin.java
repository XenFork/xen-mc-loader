package union.xenfork.xenmc.mapping;

import org.gradle.api.Project;
import org.gradle.api.plugins.PluginAware;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPlugin;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

public class MappingPlugin implements BootstrappedPlugin, BootstrappedPluginProject {
    @Override
    public void apply(PluginAware target, MinecraftExtension minecraft) {
        if (target instanceof Project project) {
            apply(project, minecraft);
        }
    }

    @Override
    public void apply(Project project, MinecraftExtension minecraft) {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("test-mapping");
    }
}
