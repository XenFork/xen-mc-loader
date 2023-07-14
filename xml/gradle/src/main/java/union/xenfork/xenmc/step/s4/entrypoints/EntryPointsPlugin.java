package union.xenfork.xenmc.step.s4.entrypoints;

import org.gradle.api.Project;
import org.gradle.api.plugins.PluginAware;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPlugin;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

/**
 * @author baka4n
 * @apiNote using asm to inject jar
 */
public class EntryPointsPlugin implements BootstrappedPluginProject {

    @Override
    public void apply(Project project, MinecraftExtension minecraft) {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("test-inject");
    }
}
